package hello.board.infrastructure.persistence.repository.view;

import hello.board.domain.view.PostStatistics;
import org.springframework.data.repository.CrudRepository;

public interface PostStatisticsRepository extends CrudRepository<PostStatistics, String> {
}
