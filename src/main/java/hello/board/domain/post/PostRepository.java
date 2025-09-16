package hello.board.domain.post;

import hello.board.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAll();

    void delete(Post post);

    List<Post> findByUserOrderByCreateDateTimeDesc(User user);

    void flush();

    void deleteAllByUser(User user);
}
