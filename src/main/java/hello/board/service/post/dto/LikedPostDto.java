package hello.board.service.post.dto;

import hello.board.domain.entity.like.Like;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LikedPostDto {

    private final Long id;
    private final String title;
    private final LocalDateTime likedAt;

    @Builder
    private LikedPostDto(Long id, String title, LocalDateTime likedAt) {
        this.id = id;
        this.title = title;
        this.likedAt = likedAt;
    }

    public static LikedPostDto of(Like like) {
        return LikedPostDto.builder()
                .id(like.getPost().getId())
                .title(like.getPost().getTitle())
                .likedAt(like.getCreateDateTime())
                .build();
    }
}