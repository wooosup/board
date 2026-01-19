package hello.board.infrastructure.persistence.repository.view;

import hello.board.domain.view.ViewLog;
import org.springframework.data.repository.CrudRepository;

public interface ViewLogRepository extends CrudRepository<ViewLog, String> {
}
