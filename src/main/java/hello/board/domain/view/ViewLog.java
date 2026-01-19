package hello.board.domain.view;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "view_log", timeToLive = 86400)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewLog {

    @Id
    private String id;

    @Builder
    private ViewLog(Long postId, String clientIp) {
        this.id = generateId(postId, clientIp);
    }

    public static ViewLog create(Long postId, String clientIp) {
        return ViewLog.builder()
                .postId(postId)
                .clientIp(clientIp)
                .build();
    }

    private String generateId(Long postId, String clientIp) {
        return postId + ":" + clientIp;
    }

}
