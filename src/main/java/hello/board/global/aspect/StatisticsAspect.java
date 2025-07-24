package hello.board.global.aspect;

import hello.board.domain.post.PostStatistics;
import hello.board.infrastructure.persistence.repository.PostStatisticsRepository;
import hello.board.infrastructure.web.post.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class StatisticsAspect {

    private final PostStatisticsRepository postStatisticsRepository;

    @AfterReturning(pointcut = "@annotation(hello.board.global.annotation.CreateStatistics)", returning = "postResponse")
    public void createPostStatistics(PostResponse postResponse) {
        if (postResponse == null) return;

        postStatisticsRepository.save(PostStatistics.builder()
                .postId(String.valueOf(postResponse.getId()))
                .viewCount(0)
                .build());
    }
}
