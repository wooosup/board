package hello.board.infrastructure.persistence.repository.like;

import hello.board.domain.like.Like;
import hello.board.domain.like.LikeRepository;
import hello.board.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Like save(Like like) {
        return likeJpaRepository.save(like);
    }

    @Override
    public void delete(Like like) {
        likeJpaRepository.delete(like);
    }

    @Override
    public Optional<Like> findByPostIdAndUsername(Long postId, String username) {
        return likeJpaRepository.findByPostIdAndUsername(postId, username);
    }

    @Override
    public List<Like> findByUserOrderByCreateDateTimeDesc(User user) {
        return likeJpaRepository.findByUserOrderByCreateDateTimeDesc(user);
    }

    @Override
    public void deleteAllByUser(User user) {
        likeJpaRepository.deleteAllByUser(user);
    }
}
