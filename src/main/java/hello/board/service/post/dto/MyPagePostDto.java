package hello.board.service.post.dto;

import hello.board.domain.entity.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyPagePostDto {
    private final Long id;
    private final String title;
    private final LocalDateTime createdDateTime;

    @Builder
    private MyPagePostDto(Long id, String title, LocalDateTime createdDateTime) {
        this.id = id;
        this.title = title;
        this.createdDateTime = createdDateTime;
    }

    public static MyPagePostDto of(Post post) {
        return MyPagePostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .createdDateTime(post.getCreateDateTime())
                .build();
    }
}
