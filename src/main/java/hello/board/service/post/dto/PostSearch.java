package hello.board.service.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class PostSearch {

    private final String keyword;
    private final String searchField;

    @Builder
    private PostSearch(String keyword, String searchField) {
        this.keyword = Objects.requireNonNullElse(keyword, "");
        this.searchField = Objects.requireNonNullElse(searchField, "");
    }
}
