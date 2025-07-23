package hello.board.infrastructure.web.user.response;

import hello.board.domain.Role;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserResponse {

    private final Long id;
    private final String username;
    private final String nickname;
    private final String password;
    private final Role grade;
    private final List<Post> posts;

    @Builder
    private UserResponse(Long id, String username, String nickname, String password, Role grade, List<Post> posts) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.grade = grade;
        this.posts = posts;
    }

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .grade(user.getGrade())
                .posts(user.getPosts())
                .build();
    }
}
