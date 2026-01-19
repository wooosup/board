package hello.board.infrastructure.web.like.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeResponse {
    private final int likeCount;
    private final boolean liked;

    @Builder
    private LikeResponse(int updatedLikeCount, boolean liked) {
        this.likeCount = updatedLikeCount;
        this.liked = liked;
    }

    public static LikeResponse of(int likeCount, boolean liked) {
        return LikeResponse.builder()
                .updatedLikeCount(likeCount)
                .liked(liked)
                .build();
    }

}
