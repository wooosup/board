package hello.board;

import hello.board.infrastructure.persistence.repository.PostQueryRepository;
import hello.board.domain.comment.CommentRepository;
import hello.board.domain.post.PostRepository;
import hello.board.domain.user.UserRepository;
import hello.board.service.comment.CommentService;
import hello.board.service.image.FileStore;
import hello.board.service.message.MessageService;
import hello.board.service.post.PostService;
import hello.board.service.poststatistics.LikeService;
import hello.board.service.user.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@EntityScan(basePackages = {"hello.board.domain"})
@Testcontainers
public class IntegrationTestSupport  {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:latest").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379).toString());
    }

    @Autowired
    protected PostService postService;

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected LikeService likeService;

    @Autowired
    protected MessageService messageService;

    @Autowired
    protected CommentService commentService;

    @Autowired
    protected EntityManager em;

    @Autowired
    protected PostQueryRepository postQueryRepository;

    @MockBean
    private FileStore fileStore;
}
