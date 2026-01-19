package hello.board.service.poststatistics;

import hello.board.domain.view.PostStatistics;
import hello.board.domain.view.ViewLog;
import hello.board.infrastructure.persistence.repository.view.PostStatisticsRepository;
import hello.board.infrastructure.persistence.repository.view.ViewLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewService {

    private final PostStatisticsRepository postStatisticsRepository;
    private final ViewLogRepository viewLogRepository;

    public void increaseViewCount(Long postId, String clientIp) {
        String logId = postId + ":" + clientIp;

        if (viewLogRepository.existsById(logId)) {
            return;
        }

        ViewLog viewLog = ViewLog.create(postId, clientIp);
        viewLogRepository.save(viewLog);

        PostStatistics statistics = postStatisticsRepository.findById(String.valueOf(postId))
                .orElseGet(() -> PostStatistics.create(postId));

        statistics.increaseViewCount();
        postStatisticsRepository.save(statistics);
    }

    public Integer getViewCount(Long postId) {
        return postStatisticsRepository.findById(String.valueOf(postId))
                .map(PostStatistics::getViewCount)
                .orElse(0);
    }

}
