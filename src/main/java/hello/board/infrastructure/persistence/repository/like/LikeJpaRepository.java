package hello.board.infrastructure.persistence.repository.like;

import hello.board.domain.like.Like;
import hello.board.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l FROM Like l WHERE l.post.id = :postId AND l.user.username = :username")
    Optional<Like> findByPostIdAndUsername(@Param("postId") Long postId, @Param("username") String username);

    List<Like> findByUserOrderByCreateDateTimeDesc(User user);

    void deleteAllByUser(User user);
}
