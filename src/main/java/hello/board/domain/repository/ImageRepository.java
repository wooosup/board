package hello.board.domain.repository;

import hello.board.domain.entity.post.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
