package hello.board.service.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdatePostForm {
    private final String title;
    private final String content;

    @Builder
    private UpdatePostForm(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
