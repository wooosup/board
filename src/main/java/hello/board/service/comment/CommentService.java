package hello.board.service.comment;

import hello.board.domain.comment.Comment;
import hello.board.domain.comment.CommentRepository;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.global.annotation.CheckCommentOwner;
import hello.board.infrastructure.web.comment.request.CommentForm;
import hello.board.infrastructure.web.comment.response.CommentDto;
import hello.board.infrastructure.web.comment.response.CommentResponse;
import hello.board.service.EntityFinder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EntityFinder entityFinder;

    public CommentResponse saveComment(CommentForm form, String loginId) {
        User user = entityFinder.getLoginUser(loginId);
        Post post = entityFinder.getPost(form.getPostId());

        Comment parent = null;
        if (form.getParentId() != null) {
            parent = entityFinder.getComment(form.getParentId());
        }

        Comment comment = Comment.create(form.getContent(), post, user, parent);

        return CommentResponse.of(commentRepository.save(comment));
    }

    @CheckCommentOwner
    public void updateComment(String content, Long commentId) {
        Comment comment = entityFinder.getComment(commentId);

        comment.updateComment(content);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAll(Long postId) {
        Post post = entityFinder.getPost(postId);

        return CommentDto.listOf(post);
    }

    @CheckCommentOwner
    public void deleteComment(Long commentId) {
        Comment comment = entityFinder.getComment(commentId);

        comment.delete();

        deleteDeletableComments(comment.getParent());
    }

    private void deleteDeletableComments(Comment comment) {
        Comment current = comment;

        while (current != null && current.isPhysicallyDeletable()) {
            Comment parent = current.getParent();

            if (parent != null) {
                parent.removeChild(current);
            }
            commentRepository.delete(current);

            current = parent;
        }
    }

}
