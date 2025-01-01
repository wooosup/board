package hello.board.domain.like;

import lombok.Getter;

@Getter
public class LikeResponse {
    private Long likeCount;

    public LikeResponse(int updatedLikeCount) {
        this.likeCount = (long) updatedLikeCount;
    }
}
