package hello.board.service.post.dto;

import hello.board.domain.entity.post.Post;
import hello.board.service.image.dto.ImageDto;
import hello.board.domain.entity.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Setter
public class PostForm {

    private Long id;

    @NotEmpty(message = "제목은 필수입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    private List<MultipartFile> images;

    private List<ImageDto> existingImages;

    public PostForm() {
    }

    @Builder
    private PostForm(String title, String content, List<MultipartFile> images, List<ImageDto> existingImages) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.existingImages = existingImages;
    }

    public Post toEntity(User user) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .build();
    }

    public static PostForm of(Post post) {
        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());
        form.setExistingImages(post.getImages().stream().map(ImageDto::of).toList());
        return form;
    }

}
