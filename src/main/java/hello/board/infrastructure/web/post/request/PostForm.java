package hello.board.infrastructure.web.post.request;

import hello.board.domain.post.Post;
import hello.board.domain.user.User;
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
