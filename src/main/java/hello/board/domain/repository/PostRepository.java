package hello.board.domain.repository;


import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserOrderByCreateDateTimeDesc(User user);
}
