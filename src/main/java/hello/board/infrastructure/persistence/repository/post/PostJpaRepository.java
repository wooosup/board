package hello.board.infrastructure.persistence.repository.post;


import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostJpaRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserOrderByCreateDateTimeDesc(User user);
}
