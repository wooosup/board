package hello.board.domain.like;

import hello.board.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface LikeRepository {

    Like save(Like like);

    void delete(Like like);

    Optional<Like> findByPostIdAndUsername(Long postId, String username);

    List<Like> findByUserOrderByCreateDateTimeDesc(User user);

    void deleteAllByUser(User user);
}
