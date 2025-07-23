package hello.board.infrastructure.persistence.repository.post;

import hello.board.domain.post.Post;
import hello.board.domain.post.PostRepository;
import hello.board.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return postJpaRepository.findAll();
    }

    @Override
    public void delete(Post post) {
        postJpaRepository.delete(post);
    }

    @Override
    public List<Post> findByUserOrderByCreateDateTimeDesc(User user) {
        return postJpaRepository.findByUserOrderByCreateDateTimeDesc(user);
    }

    @Override
    public void flush() {
        postJpaRepository.flush();
    }
}
