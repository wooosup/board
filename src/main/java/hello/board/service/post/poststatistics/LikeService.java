package hello.board.service.post.poststatistics;

import hello.board.domain.like.Like;
import hello.board.domain.like.LikeResponse;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.repository.LikeRepository;
import hello.board.service.EntityFinder;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLTransactionRollbackException;
import java.util.Optional;

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
            // 좋아요 취소
            likeRepository.delete(like.get());
            post.decrementLikeCount();
            int likeCount = post.getLikeCount();
            return new LikeResponse(likeCount, false);
        } else {
            // 좋아요 추가
            Like newLike = Like.builder()
                    .user(user)
                    .post(post)
                    .build();
            likeRepository.save(newLike);
            post.incrementLikeCount();
            int likeCount = post.getLikeCount();
            return new LikeResponse(likeCount, true);
        }
    }
}
