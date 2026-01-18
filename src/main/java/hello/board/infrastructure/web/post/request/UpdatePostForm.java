package hello.board.infrastructure.web.post.request;

import hello.board.infrastructure.web.image.response.ImageDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdatePostForm {
    private final String title;
    private final String content;
    private final List<MultipartFile> images;
    private final List<ImageDto> existingImages;

    @Builder
    private UpdatePostForm(String title, String content, List<MultipartFile> images, List<ImageDto> existingImages) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.existingImages = existingImages;
    }
}
