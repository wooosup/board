//package hello.board;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import hello.board.controller.post.PostController;
//import hello.board.controller.user.UserController;
//import hello.board.service.comment.CommentService;
//import hello.board.service.image.FileStore;
//import hello.board.service.post.PostService;
//import hello.board.service.user.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
////@AutoConfigureMockMvc
////@SpringBootTest
////@Transactional
////@ActiveProfiles("test")
//@WebMvcTest(controllers = {
//        PostController.class,
//        UserController.class
//})
//public abstract class ControllerTestSupport {
//
//    @Autowired
//    protected MockMvc mockMvc;
//
//    @Autowired
//    protected ObjectMapper objectMapper;
//
//    @MockBean
//    protected PostService postService;
//
//    @MockBean
//    protected UserService userService;
//
//    @MockBean
//    protected CommentService commentService;
////    @Autowired
////    protected PostService postService;
////
////    @Autowired
////    protected UserService userService;
////
////    @Autowired
////    protected UserRepository userRepository;
////
////    @Autowired
////    protected PostRepository postRepository;
////
////    @Autowired
////    protected MessageRepository messageRepository;
//
//    @MockBean
//    protected FileStore fileStore;
//}
