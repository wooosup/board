package hello.board.domain.image;

import java.util.Optional;

public interface ImageRepository {

    Image save(Image image);

    Optional<Image> findById(Long id);

    void delete(Image image);
}
