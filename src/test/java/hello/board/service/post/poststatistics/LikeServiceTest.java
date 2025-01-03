package hello.board.service.post.poststatistics;

import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("게시글 좋아요")
    @Test
    @Transactional
    void like() throws Exception {
        //given
        User user = User.builder()
                .username("user")
                .nickname("user")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);

        //when
        likeService.likePost(savedPost.getId(), user.getUsername());
        Integer likeCount = post.getLikeCount();

        //then
        assertThat(likeCount).isEqualTo(1);
    }

    @Test
    void multi() throws Exception {
        //given
        int available = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(available * 2);
        CountDownLatch latch = new CountDownLatch(4);
        User user = User.builder()
                .username("user")
                .nickname("user")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        Post savedPost = postRepository.save(post);
        postRepository.flush();

        User user1 = User.builder().username("user1").password("1234").build();
        User user2 = User.builder().username("user2").password("1234").build();
        User user3 = User.builder().username("user3").password("1234").build();
        User user4 = User.builder().username("user4").password("1234").build();
        userRepository.saveAll(List.of(user1, user2, user3, user4));
        userRepository.flush();


        //when
        executorService.execute(()->{
            likeService.likePost(savedPost.getId(), user1.getUsername());
            latch.countDown();
        });

        executorService.execute(()->{
            likeService.likePost(savedPost.getId(), user2.getUsername());
            latch.countDown();
        });

        executorService.execute(()->{
            likeService.likePost(savedPost.getId(), user3.getUsername());
            latch.countDown();
        });

        executorService.execute(()->{
            likeService.likePost(savedPost.getId(), user4.getUsername());
            latch.countDown();
        });

        latch.await();
        executorService.shutdown();

        //then
        Post updatedPost = postRepository.findById(post.getId()).get();
        int likeCount = updatedPost.getLikeCount();
        assertThat(likeCount).isEqualTo(4);
    }
}