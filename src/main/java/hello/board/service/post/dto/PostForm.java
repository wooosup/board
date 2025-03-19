package hello.board.service.post.dto;

import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
public class PostForm {

    @NotEmpty(message = "제목은 필수입니다.")
    private final String title;

    @NotEmpty(message = "내용은 필수입니다.")
    private final String content;

    private final List<MultipartFile> images;


    @Builder
    private PostForm(String title, String content, List<MultipartFile> images) {
        this.title = title;
        this.content = content;
        this.images = images;
    }

    public Post toEntity(User user) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .build();
    }
}
