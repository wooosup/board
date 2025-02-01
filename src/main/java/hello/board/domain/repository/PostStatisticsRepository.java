package hello.board.domain.repository;

import hello.board.domain.entity.post.PostStatistics;
import org.springframework.data.repository.CrudRepository;

public interface PostStatisticsRepository extends CrudRepository<PostStatistics, Long> {
}
