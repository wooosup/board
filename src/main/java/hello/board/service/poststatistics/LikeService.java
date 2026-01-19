package hello.board.service.poststatistics;

import hello.board.domain.like.Like;
import hello.board.domain.like.LikeRepository;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.infrastructure.web.like.response.LikeResponse;
import hello.board.service.EntityFinder;
import jakarta.persistence.OptimisticLockException;
import java.sql.SQLTransactionRollbackException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final EntityFinder entityFinder;

    @Retryable(
            retryFor = {
                    OptimisticLockException.class,
                    SQLTransactionRollbackException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100))
    @Transactional
    public LikeResponse likePost(Long postId, String username) {
        Post post = entityFinder.getPost(postId);
        User user = entityFinder.getLoginUser(username);

        Optional<Like> like = likeRepository.findByPostIdAndUsername(postId, username);

        if (like.isPresent()) {
            return unLike(like.get(), post);
        }
        return addLike(user, post);
    }

    private LikeResponse addLike(User user, Post post) {
        Like newLike = Like.create(user, post);

        likeRepository.save(newLike);
        return LikeResponse.of(post.getLikeCount(), true);
    }

    private LikeResponse unLike(Like like, Post post) {
        like.disconnect();

        likeRepository.delete(like);
        return LikeResponse.of(post.getLikeCount(), false);
    }

}
