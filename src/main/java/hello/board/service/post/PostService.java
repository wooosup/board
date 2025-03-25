package hello.board.service.post;

import hello.board.controller.post.response.PostResponse;
import hello.board.controller.post.response.PostWithCommentsDto;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.post.PostStatistics;
import hello.board.domain.entity.user.User;
import hello.board.domain.repository.PostQueryRepository;
import hello.board.domain.repository.PostRepository;
import hello.board.domain.repository.PostStatisticsRepository;
import hello.board.service.EntityFinder;
import hello.board.service.comment.CommentService;
import hello.board.service.comment.dto.CommentDto;
import hello.board.service.image.ImageService;
import hello.board.service.image.PostImageManager;
import hello.board.service.image.dto.ImageDto;
import hello.board.service.post.dto.*;
import hello.board.service.post.poststatistics.ViewService;
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
    private final ImageService imageService;
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

        if (postImageManager.hasImageFiles(imageFiles)) {
            postImageManager.saveImagesToPost(imageFiles, post);
        }

        createPostStatistics(savedPost.getId());

        return PostResponse.of(savedPost);
    }

    public PostDetailDto findByPostId(Long postId, HttpServletRequest request) {
        Post post = entityFinder.getPost(postId);

        viewService.handleView(postId, request);

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

        checkAuthorization(post.getUser(), loginUser);

        if (postImageManager.isImageDeleteBy(imageIdsToDelete)) {
            postImageManager.deleteSelectedImages(post, imageIdsToDelete);
        }
        if (postImageManager.hasImageFiles(imageFiles)) {
            postImageManager.saveImagesToPost(imageFiles, post);
        }
        post.updatePost(form.getTitle(), form.getContent());

        return PostResponse.of(post);
    }

    @Transactional
    public void deletePost(Long id, String loginId) {
        Post post = entityFinder.getPost(id);
        User loginUser = entityFinder.getLoginUser(loginId);

        checkAuthorization(post.getUser(), loginUser);

        postImageManager.deleteAllImages(post);
        postRepository.delete(post);
    }

    public Page<MainPostDto> searchPosts(PostSearch search, Pageable page) {
        return postQueryRepository.searchPosts(search, page);
    }

    private void createPostStatistics(Long postId) {
        PostStatistics statistics = PostStatistics.builder()
                .postId(String.valueOf(postId))
                .viewCount(0)
                .build();
        postStatisticsRepository.save(statistics);
    }

    private void checkAuthorization(User postOwner, User loginUser) {
        if (!postOwner.equals(loginUser)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}