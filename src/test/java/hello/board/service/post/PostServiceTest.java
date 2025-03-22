package hello.board.service.post;

import hello.board.controller.post.response.PostResponse;
import hello.board.domain.Role;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import hello.board.domain.repository.PostRepository;
import hello.board.domain.repository.UserRepository;
import hello.board.exception.EntityNotFoundException;
import hello.board.service.post.dto.PostDetailDto;
import hello.board.service.post.dto.PostForm;
import hello.board.service.post.dto.UpdatePostForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void savePost() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .nickname("keke")
                .password("1234")
                .grade(Role.USER)
                .build();
        userRepository.save(user);

        PostForm form = PostForm.builder()
                .title("제목")
                .content("내용")
                .build();

        //when
        PostResponse response = postService.savePost(form, null, user.getUsername());

        //then
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getContent()).isEqualTo("내용");
    }

    @Test
    void findPost() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .nickname("keke")
                .password("1234")
                .grade(Role.USER)
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        postRepository.save(post);

        //when
        MockHttpServletRequest request = new MockHttpServletRequest();
        PostDetailDto response = postService.findByPostId(post.getId(), request);

        //then
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getContent()).isEqualTo("내용");
    }

    @Test
    void findPostForm() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .nickname("keke")
                .password("1234")
                .grade(Role.USER)
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        postRepository.save(post);

        //when
        UpdatePostForm result = postService.findPostForm(post.getId());

        //then
        assertThat(result.getTitle()).isEqualTo("제목");
        assertThat(result.getContent()).isEqualTo("내용");
    }

    @Test
    void findPostException() throws Exception {
        //given
        Long postId = 999L;

        // expect
        assertThatThrownBy(() -> postService.findByPostId(postId, new MockHttpServletRequest()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글을 찾을 수 없습니다.");
    }

    @Test
    void updatePost() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .nickname("keke")
                .password("1234")
                .grade(Role.USER)
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        postRepository.save(post);

        UpdatePostForm form = UpdatePostForm.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        //when
        PostResponse response = postService.updatePost(post.getId(), form, null, user.getUsername(), null);

        //then
        assertThat(response.getTitle()).isEqualTo("수정된 제목");
        assertThat(response.getContent()).isEqualTo("수정된 내용");
    }

    @Test
    void delete() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .nickname("keke")
                .password("1234")
                .grade(Role.USER)
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        postRepository.save(post);

        //when
        postService.deletePost(post.getId(), user.getUsername());

        //then
        assertThat(postRepository.findById(post.getId())).isEmpty();
    }
}