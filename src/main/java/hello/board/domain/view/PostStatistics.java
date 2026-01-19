package hello.board.domain.view;

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
    private Integer viewCount;

    @Builder
    private PostStatistics(Long postId, Integer viewCount) {
        this.postId = String.valueOf(postId);
        this.viewCount = (viewCount == null) ? 0 : viewCount;
    }

    public static PostStatistics create(Long postId) {
        return PostStatistics.builder()
                .postId(postId)
                .viewCount(0)
                .build();
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

}