package hello.board.infrastructure.persistence.repository.image;

import hello.board.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {
}
