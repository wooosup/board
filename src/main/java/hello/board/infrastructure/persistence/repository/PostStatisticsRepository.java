package hello.board.infrastructure.persistence.repository;

import hello.board.domain.post.PostStatistics;
import org.springframework.data.repository.CrudRepository;

public interface PostStatisticsRepository extends CrudRepository<PostStatistics, Long> {
}
