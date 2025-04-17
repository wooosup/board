package hello.board.service.image;

import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.post.image.Image;
import hello.board.exception.ImageUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostImageManager {

    private final ImageService imageService;

    public boolean isImageDeleteBy(List<Long> imageIdsToDelete) {
        return imageIdsToDelete != null && !imageIdsToDelete.isEmpty();
    }

    public boolean hasImageFiles(List<MultipartFile> imageFiles) {
        return imageFiles != null && !imageFiles.isEmpty();
    }

    public void saveImagesToPost(List<MultipartFile> imageFiles, Post post) {
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                try {
                    Image image = imageService.saveImage(file);
                    image.setPost(post);
                    post.addImage(image);
                } catch (IOException e) {
                    throw new ImageUploadException("이미지 저장에 실패했습니다." + file.getOriginalFilename(), e);
                }
            }
        }
    }

    public void deleteSelectedImages(Post post, List<Long> imageIdsToDelete) {
        post.getImages().removeIf(image -> {
            boolean isToDelete = imageIdsToDelete.contains(image.getId());
            if (isToDelete) {
                deleteImageSafely(image.getId());
            }
            return isToDelete;
        });
    }

    public void deleteAllImages(Post post) {
        post.getImages().forEach(image -> deleteImageSafely(image.getId()));
        post.getImages().clear();
    }

    private void deleteImageSafely(Long imageId) {
        imageService.deleteImage(imageId);
    }
}
