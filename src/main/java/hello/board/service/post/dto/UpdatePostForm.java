package hello.board.service.post.dto;

import hello.board.service.image.dto.ImageDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
