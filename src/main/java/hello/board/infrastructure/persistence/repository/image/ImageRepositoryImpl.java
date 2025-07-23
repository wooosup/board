package hello.board.infrastructure.persistence.repository.image;

import hello.board.domain.image.Image;
import hello.board.domain.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

    private final ImageJpaRepository imageJpaRepository;

    @Override
    public Image save(Image image) {
        return imageJpaRepository.save(image);
    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageJpaRepository.findById(id);
    }

    @Override
    public void delete(Image image) {
        imageJpaRepository.delete(image);
    }
}
