package hello.board.service.comment;

import hello.board.domain.entity.comment.Comment;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import hello.board.domain.repository.CommentRepository;
import hello.board.service.EntityFinder;
import hello.board.service.comment.dto.CommentDto;
import hello.board.service.comment.dto.CommentForm;
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

    public Long saveComment(CommentForm form, String loginId) {
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

        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAll(Long postId) {
        Post post = entityFinder.getPost(postId);

        return getCommentDto(post);
    }

    public void updateComment(String content, Long commentId, String username) {
        Comment comment = entityFinder.getComment(commentId);
        User user = entityFinder.getLoginUser(username);

        checkAuthorization(comment, user);
        comment.updateComment(content);
    }

    public void deleteComment(Long commentId, String loginId) {
        Comment findComment = entityFinder.getComment(commentId);
        User user = entityFinder.getLoginUser(loginId);

        checkAuthorization(findComment, user);

        if (hasChildren(findComment)) {
            findComment.markAsDeleted();
            removeParentIfPossible(findComment);
        } else {
            commentRepository.delete(findComment);
            removeParentIfPossible(findComment.getParent());
        }
    }

    private void removeParentIfPossible(Comment parent) {
        if (parent == null) {
            return;
        }

        if (!parent.isDeleted()) {
            return;
        }

        if (!parent.getChildren().isEmpty()) {
            return;
        }

        commentRepository.delete(parent);

        removeParentIfPossible(parent.getParent());
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

    private static void checkAuthorization(Comment findComment, User user) {
        if (!findComment.getUser().equals(user)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
