package hello.board.service.post;

import hello.board.infrastructure.web.post.request.PostForm;
import hello.board.infrastructure.web.post.request.UpdatePostForm;
import hello.board.infrastructure.web.post.response.*;
import hello.board.domain.post.Post;
import hello.board.domain.post.PostStatistics;
import hello.board.domain.user.User;
import hello.board.infrastructure.persistence.repository.PostQueryRepository;
import hello.board.infrastructure.persistence.repository.PostStatisticsRepository;
import hello.board.domain.post.PostRepository;
import hello.board.service.EntityFinder;
import hello.board.service.comment.CommentService;
import hello.board.infrastructure.web.comment.response.CommentDto;
import hello.board.service.image.PostImageManager;
import hello.board.service.image.dto.ImageDto;
import hello.board.service.poststatistics.ViewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final EntityFinder entityFinder;
    private final PostQueryRepository postQueryRepository;
    private final PostStatisticsRepository postStatisticsRepository;
    private final ViewService viewService;
    private final CommentService commentService;
    private final PostImageManager postImageManager;

    @Transactional
    public PostResponse savePost(PostForm form, List<MultipartFile> imageFiles, String loginId) {
        User loginUser = entityFinder.getLoginUser(loginId);
        Post post = form.toEntity(loginUser);
        Post savedPost = postRepository.save(post);

        processImages(imageFiles, post);
        createPostStatistics(savedPost.getId());

        return PostResponse.of(savedPost);
    }

    public PostDetailDto findByPostId(Long postId, HttpServletRequest request) {
        Post post = entityFinder.getPost(postId);
        updateViewCount(postId, request);

        return createPostDetailDto(postId, post);
    }

    private PostDetailDto createPostDetailDto(Long postId, Post post) {
        Integer likeCount = post.getLikeCount();
        Integer viewCount = viewService.getViewCount(postId);

        return PostDetailDto.of(post, viewCount, likeCount);
    }

    public PostWithCommentsDto findPostWithComments(Long postId, HttpServletRequest request) {
        PostDetailDto postDetail = findByPostId(postId, request);
        List<CommentDto> comments = commentService.findAll(postId);

        return PostWithCommentsDto.of(postDetail, comments);
    }

    public UpdatePostForm findPostForm(Long postId) {
        Post post = entityFinder.getPost(postId);
        List<ImageDto> images = post.getImages().stream()
                .map(ImageDto::of)
                .toList();

        return UpdatePostForm.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .existingImages(images)
                .build();
    }

    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostForm form, List<MultipartFile> imageFiles, String loginId, List<Long> imageIdsToDelete) {
        Post post = entityFinder.getPost(postId);
        User loginUser = entityFinder.getLoginUser(loginId);

        validatePostOwnership(post.getUser(), loginUser);
        processImageUpdates(imageFiles, imageIdsToDelete, post);
        updatePostContent(form, post);

        return PostResponse.of(post);
    }

    @Transactional
    public void deletePost(Long id, String loginId) {
        Post post = entityFinder.getPost(id);
        User loginUser = entityFinder.getLoginUser(loginId);

        validatePostOwnership(post.getUser(), loginUser);
        postImageManager.deleteAllImages(post);
        postRepository.delete(post);
    }

    public Page<MainPostDto> searchPosts(PostSearch search, Pageable page) {
        return postQueryRepository.searchPosts(search, page);
    }

    private void processImages(List<MultipartFile> imageFiles, Post post) {
        if (postImageManager.hasImageFiles(imageFiles)) {
            postImageManager.saveImagesToPost(imageFiles, post);
        }
    }

    private void updateViewCount(Long postId, HttpServletRequest request) {
        viewService.handleView(postId, request);
    }

    private static void updatePostContent(UpdatePostForm form, Post post) {
        post.updatePost(form.getTitle(), form.getContent());
    }

    private void processImageUpdates(List<MultipartFile> imageFiles, List<Long> imageIdsToDelete, Post post) {
        if (postImageManager.isImageDeleteBy(imageIdsToDelete)) {
            postImageManager.deleteSelectedImages(post, imageIdsToDelete);
        }
        if (postImageManager.hasImageFiles(imageFiles)) {
            postImageManager.saveImagesToPost(imageFiles, post);
        }
    }

    private void createPostStatistics(Long postId) {
        PostStatistics statistics = PostStatistics.builder()
                .postId(String.valueOf(postId))
                .viewCount(0)
                .build();
        postStatisticsRepository.save(statistics);
    }

    private void validatePostOwnership(User postOwner, User currentUser) {
        if (!postOwner.equals(currentUser)) {
            throw new IllegalArgumentException("이 게시글을 수정할 권한이 없습니다.");
        }
    }
}