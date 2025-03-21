package hello.board.service.post.poststatistics;

import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import hello.board.domain.repository.PostRepository;
import hello.board.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
                .username("wss3325")
                .nickname("sup")
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
        Integer result = post.getLikeCount();

        //then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void discountLike() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .nickname("sup")
                .password("1234")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        postRepository.save(post);
        likeService.likePost(post.getId(), user.getUsername());

        //when
        likeService.likePost(post.getId(), user.getUsername());

        //then
        Integer result = post.getLikeCount();
        assertThat(result).isZero();
    }
}