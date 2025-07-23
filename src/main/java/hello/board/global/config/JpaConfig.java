package hello.board.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@EnableJpaAuditing
@EnableRedisRepositories
@EnableJpaRepositories(basePackages = "hello.board.infrastructure.persistence.repository")
@Configuration
public class JpaConfig {
}
