package hello.board.domain.entity.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("PostStatistics")
public class PostStatistics implements Serializable {

    @Id
    private String postId;
    private Integer viewCount = 0;

    @Builder
    private PostStatistics(String postId, Integer viewCount) {
        this.postId = postId;
        this.viewCount = (viewCount == null) ? 0 : viewCount;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}