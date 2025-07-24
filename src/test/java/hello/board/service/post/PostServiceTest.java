package hello.board.service.post;

import hello.board.IntegrationTestSupport;
import hello.board.domain.Role;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.global.exception.EntityNotFoundException;
import hello.board.infrastructure.web.post.request.UpdatePostForm;
import hello.board.infrastructure.web.post.response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostServiceTest extends IntegrationTestSupport {

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
    @WithMockUser(username = "wss3325")
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
        PostResponse response = postService.updatePost(post.getId(), form, null, null);

        //then
        assertThat(response.getTitle()).isEqualTo("수정된 제목");
        assertThat(response.getContent()).isEqualTo("수정된 내용");
    }

    @DisplayName("다른 사람이 작성한 게시글을 수정하면 예외가 발생한다.")
    @Test
    @WithMockUser(username = "otherUser")
    void updatePostException() throws Exception {
        //given
        User owner = User.builder()
                .username("wss3325")
                .nickname("keke")
                .password("1234")
                .grade(Role.USER)
                .build();
        User otherUser = User.builder()
                .username("otherUser")
                .nickname("hihi")
                .password("3454")
                .grade(Role.USER)
                .build();
        userRepository.saveAll(List.of(owner, otherUser));

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(owner)
                .build();
        postRepository.save(post);

        UpdatePostForm form = UpdatePostForm.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // expect
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> postService.updatePost(post.getId(), form, null, null));

        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(e.getReason()).isEqualTo("이 게시글에 대한 권한이 없습니다.");
    }

    @Test
    @WithMockUser(username = "wss3325")
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
        postService.deletePost(post.getId());

        //then
        assertThat(postRepository.findById(post.getId())).isEmpty();
    }
}