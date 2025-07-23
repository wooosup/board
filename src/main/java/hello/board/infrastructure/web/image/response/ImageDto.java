package hello.board.service.image.dto;

import hello.board.domain.image.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageDto {

    private final Long id;
    private final String imgUrl;

    @Builder
    private ImageDto(Long id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
    }

    public static ImageDto of(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .imgUrl(image.getImgUrl())
                .build();
    }
}
