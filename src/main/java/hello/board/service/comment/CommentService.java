package hello.board.service.comment;

import hello.board.global.annotation.CheckCommentOwner;
import hello.board.infrastructure.web.comment.response.CommentResponse;
import hello.board.domain.comment.Comment;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.domain.comment.CommentRepository;
import hello.board.service.EntityFinder;
import hello.board.infrastructure.web.comment.response.CommentDto;
import hello.board.infrastructure.web.comment.request.CommentForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EntityFinder entityFinder;

    public CommentResponse saveComment(CommentForm form, String loginId) {
        User loginUser = entityFinder.getLoginUser(loginId);
        Post post = entityFinder.getPost(form.getPostId());

        Comment parent = null;
        if (form.getParentId() != null) {
            parent = entityFinder.getComment(form.getParentId());
        }

        Comment comment = form.toEntity(loginUser, post, parent);

        if (parent != null) {
            parent.getChildren().add(comment);
        }

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.of(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAll(Long postId) {
        Post post = entityFinder.getPost(postId);

        return getCommentDto(post);
    }

    @CheckCommentOwner
    public void updateComment(String content, Long commentId) {
        Comment comment = entityFinder.getComment(commentId);

        comment.updateComment(content);
    }

    @CheckCommentOwner
    public void deleteComment(Long commentId) {
        Comment findComment = entityFinder.getComment(commentId);

        if (hasChildren(findComment)) {
            findComment.markAsDeleted();
            removeParentIfPossible(findComment);
            return;
        }

        commentRepository.delete(findComment);
        removeParentIfPossible(findComment.getParent());
    }

    private void removeParentIfPossible(Comment parent) {
        if (parent == null || !parent.isDeleted()) {
            return;
        }

        long activeChildrenCount = commentRepository.countByParentIdAndDeletedFalse(parent.getId());

        if (activeChildrenCount == 0) {
            Comment parentOfParent = parent.getParent();
            commentRepository.delete(parent);
            removeParentIfPossible(parentOfParent);
        }
    }

    private static boolean hasChildren(Comment comment) {
        return !comment.getChildren().isEmpty();
    }

    private static List<CommentDto> getCommentDto(Post post) {
        List<Comment> comments = post.getComments().stream()
                .filter(CommentService::hasNotParent)
                .toList();

        return comments.stream()
                .map(CommentDto::of)
                .toList();
    }

    private static boolean hasNotParent(Comment comment) {
        return comment.getParent() == null;
    }
}
