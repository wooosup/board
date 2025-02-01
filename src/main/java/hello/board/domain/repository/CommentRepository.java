package hello.board.domain.repository;

import hello.board.domain.entity.comment.Comment;
import hello.board.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserOrderByCreateDateTimeDesc(User user);
}
