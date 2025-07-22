//package hello.board.domain.repository;
//
//import hello.board.domain.Role;
//import hello.board.domain.entity.user.User;
//import jakarta.persistence.EntityManager;
//import org.hibernate.Session;
//import org.hibernate.stat.Statistics;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Transactional
//@SpringBootTest
//@ActiveProfiles("test")
//class RepositoryNPlusOneTest {
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private MessageRepository messageRepository;
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    private Statistics statistics;
//
//    @BeforeEach
//    void setUp() {
//        Session session = em.unwrap(Session.class);
//        statistics = session.getSessionFactory().getStatistics();
//        statistics.setStatisticsEnabled(true);
//    }
//
//    private void clearStatistics() {
//        statistics.clear();
//    }
//
//    private void assertQueryCount(int expectedCount) {
//        assertThat(statistics.getQueryExecutionCount()).isEqualTo(expectedCount);
//    }
//
//    private User persistUser() {
//        User user = User.builder()
//                .username("testUser")
//                .nickname("nickname")
//                .password("password")
//                .grade(Role.USER)
//                .build();
//        em.persist(user);
//        em.flush();
//        clearStatistics();
//        return user;
//    }
//
//    @Test
//    @DisplayName("게시글 조회 - 사용자별 게시글 정렬 조회")
//    void test_findByUserOrderByPostDateDesc() {
//        // given
//        User user = persistUser();
//
//        // when
//        postRepository.findByUserOrderByCreateDateTimeDesc(user);
//
//        // then
//        assertQueryCount(1); // 단일 쿼리 실행 확인
//    }
//
//    @Test
//    @DisplayName("받은 메시지 조회")
//    void test_receiver_message() {
//        // given
//        User receiver = persistUser();
//
//        // when
//        messageRepository.findActiveReceivedMessages(receiver);
//
//        // then
//        assertQueryCount(1); // 단일 쿼리 실행 확인
//    }
//
//    @Test
//    @DisplayName("보낸 메시지 조회")
//    void test_sender_message() {
//        // given
//        User sender = persistUser();
//
//        // when
//        messageRepository.findActiveSentMessages(sender);
//
//        // then
//        assertQueryCount(1); // 단일 쿼리 실행 확인
//    }
//
//    @Test
//    @DisplayName("댓글 조회 - 사용자별 댓글 정렬 조회")
//    void test_comment() {
//        // given
//        User user = persistUser();
//
//        // when
//        commentRepository.findByUserOrderByCreateDateTimeDesc(user);
//
//        // then
//        assertQueryCount(1); // 단일 쿼리 실행 확인
//    }
//}
