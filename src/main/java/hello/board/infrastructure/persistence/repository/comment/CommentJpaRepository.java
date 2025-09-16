package hello.board.infrastructure.persistence.repository.comment;

import hello.board.domain.comment.Comment;
import hello.board.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserOrderByCreateDateTimeDesc(User user);

    long countByParentIdAndDeletedFalse(Long parentId);

    void deleteAllByUser(User user);
}
