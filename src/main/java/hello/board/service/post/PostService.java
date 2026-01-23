package hello.board.service.post;

import hello.board.domain.post.Post;
import hello.board.domain.post.PostRepository;
import hello.board.domain.user.User;
import hello.board.global.annotation.CheckPostOwner;
import hello.board.global.annotation.CreateStatistics;
import hello.board.infrastructure.persistence.repository.PostQueryRepository;
import hello.board.infrastructure.web.comment.response.CommentDto;
import hello.board.infrastructure.web.image.response.ImageDto;
import hello.board.infrastructure.web.post.request.PostForm;
import hello.board.infrastructure.web.post.request.UpdatePostForm;
import hello.board.infrastructure.web.post.response.MainPostDto;
import hello.board.infrastructure.web.post.response.PostDetailDto;
import hello.board.infrastructure.web.post.response.PostResponse;
import hello.board.infrastructure.web.post.response.PostSearch;
import hello.board.infrastructure.web.post.response.PostWithCommentsDto;
import hello.board.service.EntityFinder;
import hello.board.service.comment.CommentService;
import hello.board.service.image.PostImageManager;
import hello.board.service.poststatistics.ViewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final EntityFinder entityFinder;
    private final PostQueryRepository postQueryRepository;
    private final CommentService commentService;
    private final ViewService viewService;
    private final PostImageManager postImageManager;

    @CreateStatistics
    @Transactional
    public PostResponse savePost(PostForm form, List<MultipartFile> imageFiles, String username) {
        User writer = entityFinder.getLoginUser(username);
        Post post = form.toEntity(writer);

        processImages(imageFiles, post);

        return PostResponse.of(postRepository.save(post));
    }

    @Transactional
    public PostWithCommentsDto readPost(Long postId, String clientIp) {
        Post post = entityFinder.getPost(postId);

        viewService.increaseViewCount(postId, clientIp);
        Integer viewCount = viewService.getViewCount(postId);
        List<CommentDto> comments = commentService.findAll(postId);

        PostDetailDto postDetail = PostDetailDto.of(post, viewCount, post.getLikeCount());
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

    @CheckPostOwner
    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostForm form, List<MultipartFile> imageFiles, List<Long> imageIdsToDelete) {
        Post post = entityFinder.getPost(postId);

        processImageUpdates(imageFiles, imageIdsToDelete, post);
        post.updatePost(form.getTitle(), form.getContent());

        return PostResponse.of(post);
    }

    @CheckPostOwner
    @Transactional
    public void deletePost(Long id) {
        Post post = entityFinder.getPost(id);

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

    private void processImageUpdates(List<MultipartFile> imageFiles, List<Long> imageIdsToDelete, Post post) {
        if (postImageManager.isImageDeleteBy(imageIdsToDelete)) {
            postImageManager.deleteSelectedImages(post, imageIdsToDelete);
        }
        if (postImageManager.hasImageFiles(imageFiles)) {
            postImageManager.saveImagesToPost(imageFiles, post);
        }
    }

}