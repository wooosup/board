package hello.board.service;

import hello.board.IntegrationTestSupport;
import hello.board.domain.entity.comment.Comment;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import hello.board.service.user.dto.UserForm;
import hello.board.service.user.dto.UserLikedPostsDto;
import hello.board.service.user.dto.UserPostsAndCommentsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest extends IntegrationTestSupport {


    @DisplayName("아이디 중복 검사")
    @Test
    void duplicateUsername() throws Exception {
        //given
        UserForm userForm1 = UserForm.builder()
                .username("wss3325")
                .password("12345")
                .nickname("sup")
                .build();
        UserForm userForm2 = UserForm.builder()
                .username("wss3325")
                .password("12345")
                .nickname("woo")
                .build();
        userService.saveUser(userForm1);

        // expect
        assertThatThrownBy(() -> userService.saveUser(userForm2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }    
    
    @DisplayName("닉네임 중복 검사")
    @Test
    void duplicateNickname() throws Exception {
        //given
        UserForm userForm1 = UserForm.builder()
                .username("wss3325")
                .password("12345")
                .nickname("sup")
                .build();
        UserForm userForm2 = UserForm.builder()
                .username("krs3454")
                .password("12345")
                .nickname("sup")
                .build();
        userService.saveUser(userForm1);

        // expect
        assertThatThrownBy(() -> userService.saveUser(userForm2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 닉네임입니다.");
    }

    @Test
    void getUserPostsAndComments() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .password("12345")
                .nickname("sup")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        postRepository.save(post);
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .parent(null)
                .content("댓글")
                .build();
        commentRepository.save(comment);

        //when
        UserPostsAndCommentsDto result = userService.getUserPostsAndComments("wss3325");

        //then
        assertThat(result.getPosts().get(0).getTitle()).isEqualTo("제목");
        assertThat(result.getComments().get(0).getContent()).isEqualTo("댓글");
    }

    @DisplayName("좋아요 누른 글 조회")
    @Test
    void getLikedPosts() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .password("12345")
                .nickname("sup")
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
        UserLikedPostsDto likedPosts = userService.getUserLikedPosts(user.getUsername());

        //then
        assertThat(likedPosts.getLikedPosts().get(0).getTitle()).isEqualTo("제목");
    }
}