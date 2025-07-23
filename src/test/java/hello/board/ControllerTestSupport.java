package hello.board;

import hello.board.domain.message.MessageRepository;
import hello.board.domain.post.PostRepository;
import hello.board.domain.user.UserRepository;
import hello.board.service.image.FileStore;
import hello.board.service.post.PostService;
import hello.board.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Testcontainers
public abstract class ControllerTestSupport {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:latest").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379).toString());
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected PostService postService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected MessageRepository messageRepository;

    @MockBean
    protected FileStore fileStore;
}
