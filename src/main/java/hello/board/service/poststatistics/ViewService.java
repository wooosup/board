package hello.board.service.poststatistics;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ViewService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String VIEW_COUNT_KEY_PREFIX = "post:viewCount:";
    private static final String VIEWED_KEY_PREFIX = "post:viewed:";

    @Transactional
    public void handleView(Long postId, HttpServletRequest request) {
        String clientIdentifier = getClientIdentifier(request);
        String redisKey = VIEWED_KEY_PREFIX + postId + ":" + clientIdentifier;

        Boolean hasViewed = redisTemplate.hasKey(redisKey);
        if (Boolean.FALSE.equals(hasViewed)) {
            // 조회수 증가
            redisTemplate.opsForZSet().incrementScore(VIEW_COUNT_KEY_PREFIX, postId.toString(), 1);

            // Redis에 키 저장 및 만료 시간 설정
            redisTemplate.opsForValue().set(redisKey, "true");
            redisTemplate.expire(redisKey, Duration.ofDays(1));
        }
    }

    public Integer getViewCount(Long postId) {
        Double viewCount = redisTemplate.opsForZSet().score(VIEW_COUNT_KEY_PREFIX, postId.toString());
        return (viewCount != null) ? viewCount.intValue() : 0;
    }


    private String getClientIdentifier(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String sessionId = request.getSession().getId();

        return ipAddress + ":" + sessionId;
    }
}
