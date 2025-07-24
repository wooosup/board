package hello.board.service.comment;

import hello.board.IntegrationTestSupport;
import hello.board.infrastructure.web.comment.response.CommentResponse;
import hello.board.domain.Role;
import hello.board.domain.comment.Comment;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.infrastructure.web.comment.response.CommentDto;
import hello.board.infrastructure.web.comment.request.CommentForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CommentServiceTest extends IntegrationTestSupport {

    @Test
    void save() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .password("1234")
                .grade(Role.USER)
                .nickname("seop")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        CommentForm commentForm = CommentForm.builder()
                .postId(post.getId())
                .parentId(null)
                .content("댓글")
                .build();

        //when
        CommentResponse result = commentService.saveComment(commentForm, user.getUsername());

        //then
        assertThat(result.getUser().getNickname()).isEqualTo("seop");
        assertThat(result.getPost().getContent()).isEqualTo("내용");
        assertThat(result.getContent()).isEqualTo("댓글");
    }

    @Test
    void findAll() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .password("1234")
                .grade(Role.USER)
                .nickname("seop")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build();
        Post savedPost = postRepository.save(post);

        Comment comment1 = Comment.builder()
                .user(user)
                .post(savedPost)
                .parent(null)
                .content("댓글1")
                .build();
        Comment comment2 = Comment.builder()
                .user(user)
                .post(savedPost)
                .parent(comment1)
                .content("댓글2")
                .build();
        Comment comment3 = Comment.builder()
                .user(user)
                .post(savedPost)
                .parent(comment1)
                .content("댓글3")
                .build();
        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        commentRepository.flush();
        postRepository.flush();

        em.clear();
        //when
        List<CommentDto> result = commentService.findAll(post.getId());

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getContent()).isEqualTo("댓글1");
        assertThat(result.get(0).getChildren().get(0).getContent()).isEqualTo("댓글2");
    }

    @DisplayName("자식댓글이 있는 부모댓글을 삭제하면 부모댓글은 삭제되지 않고 삭제된 것으로 표시된다.")
    @Test
    @WithMockUser(username = "wss3325")
    void delete() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .password("1234")
                .grade(Role.USER)
                .nickname("seop")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        Comment comment1 = Comment.builder()
                .user(user)
                .post(post)
                .parent(null)
                .content("댓글1")
                .build();
        Comment comment2 = Comment.builder()
                .user(user)
                .post(post)
                .parent(comment1)
                .content("댓글1")
                .build();
        commentRepository.saveAll(List.of(comment1, comment2));
        commentRepository.flush();

        em.clear();
        //when
        commentService.deleteComment(comment1.getId());

        //then
        Optional<Comment> result = commentRepository.findById(comment1.getId());
        assertThat(result).isPresent();
        assertThat(result.get().isDeleted()).isTrue();
    }

    @Test
    @WithMockUser(username = "wss3325")
    void update() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .password("1234")
                .grade(Role.USER)
                .nickname("seop")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .parent(null)
                .content("댓글1")
                .build();
        commentRepository.save(comment);

        //when
        commentService.updateComment("수정된 댓글", comment.getId());

        //then
        assertThat(comment.getContent()).isEqualTo("수정된 댓글");
    }
}