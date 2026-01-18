package hello.board.infrastructure.web.like.response;

import lombok.Getter;

@Getter
public class LikeResponse {
    private final int likeCount;
    private final boolean liked;

    public LikeResponse(int updatedLikeCount, boolean liked) {
        this.likeCount = updatedLikeCount;
        this.liked = liked;
    }
}
