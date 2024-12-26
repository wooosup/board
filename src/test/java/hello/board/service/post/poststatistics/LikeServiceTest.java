package hello.board.service.post.poststatistics;

import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.repository.LikeRepository;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("게시글 좋아요")
    @Test
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
        likeService.likePost(savedPost.getId(), "user");
        int likeCount = likeRepository.countByPostId(savedPost.getId()).intValue();

        //then
        assertThat(likeCount).isEqualTo(1);
    }
}