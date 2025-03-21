package hello.board.service.user.dto;

import hello.board.domain.Role;
import hello.board.domain.entity.user.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserForm {

    @NotEmpty(message = "닉네임은 필수입니다.")
    private final String nickname;

    @NotEmpty(message = "아이디는 필수입니다.")
    @Size(min = 5, message = "아이디는 5자리 이상이어야 합니다.")
    private final String username;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자리 이상이어야 합니다.")
    private final String password;

    @Builder
    private UserForm(String nickname, String username, String password) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .password(encodedPassword)
                .grade(Role.USER)
                .build();
    }
}
