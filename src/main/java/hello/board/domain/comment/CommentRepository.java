package hello.board.domain.comment;

import hello.board.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> findByUserOrderByCreateDateTimeDesc(User user);

    long countByParentIdAndDeletedFalse(Long parentId);

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    void delete(Comment comment);

    List<Comment> saveAll(List<Comment> comments);

    void flush();
}
