package hello.board.service.comment;

import hello.board.domain.entity.comment.Comment;
import hello.board.service.comment.dto.CommentDto;
import hello.board.service.comment.dto.CommentForm;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import hello.board.domain.repository.CommentRepository;
import hello.board.service.EntityFinder;
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

        if (findComment.getParent() == null) {
            // 최상위 댓글인 경우 -> 소프트 삭제
            findComment.markAsDeleted();
            // 자식이 모두 삭제되면 부모도 물리 삭제
            removeParentIfPossible(findComment);
        } else {
            // 대댓글인 경우 -> 즉시 물리 삭제
            commentRepository.delete(findComment);
            // 부모 댓글이 "소프트 삭제 + 남은 자식 없음" 이면 부모도 삭제
            removeParentIfPossible(findComment.getParent());
        }
    }

    /**
     * 상위 부모 댓글을 재귀적으로 삭제할 수 있는지 체크
     */
    private void removeParentIfPossible(Comment parent) {
        if (parent == null) {
            return;
        }
        
        // 부모가 아직 소프트 삭제되지 않았다면 물리 삭제 불가
        if (!parent.isDeleted()) {
            return;
        }

        // 자식 댓글이 하나라도 남아 있으면 물리 삭제 불가
        if (!parent.getChildren().isEmpty()) {
            return;
        }

        // 위 두 조건을 모두 만족하면 부모도 물리 삭제
        commentRepository.delete(parent);

        // 재귀 검사
        removeParentIfPossible(parent.getParent());
    }

    private static List<CommentDto> getCommentDto(Post post) {
        List<Comment> comments = post.getComments().stream()
                .filter(c -> c.getParent() == null)
                .toList();

        return comments.stream()
                .map(CommentDto::of)
                .toList();
    }

    private static void checkAuthorization(Comment findComment, User user) {
        if (!findComment.getUser().equals(user)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
