package hello.board;

import hello.board.domain.repository.CommentRepository;
import hello.board.domain.repository.PostQueryRepository;
import hello.board.domain.repository.PostRepository;
import hello.board.domain.repository.UserRepository;
import hello.board.service.comment.CommentService;
import hello.board.service.image.FileStore;
import hello.board.service.message.MessageService;
import hello.board.service.post.PostService;
import hello.board.service.post.poststatistics.LikeService;
import hello.board.service.user.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@EntityScan(basePackages = {"hello.board.domain"})
public class IntegrationTestSupport  {

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
