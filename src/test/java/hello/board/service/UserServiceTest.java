package hello.board.service;

import hello.board.service.user.dto.UserForm;
import hello.board.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @DisplayName("아이디 중복 검사")
    @Test
    void duplicateUsername() throws Exception {
        //given
        UserForm userForm1 = new UserForm();
        userForm1.setUsername("testUser");
        userForm1.setNickname("nickname");
        userForm1.setPassword("password");

        UserForm userForm2 = new UserForm();
        userForm2.setUsername("testUser");
        userForm2.setNickname("nickname2");
        userForm2.setPassword("password");

        //when
        userService.saveUser(userForm1);

        //then
        assertThatThrownBy(() -> userService.saveUser(userForm2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }    
    
    @DisplayName("닉네임 중복 검사")
    @Test
    void duplicateNickname() throws Exception {
        //given
        UserForm userForm1 = new UserForm();
        userForm1.setUsername("testUser1");
        userForm1.setNickname("nickname");
        userForm1.setPassword("password");

        UserForm userForm2 = new UserForm();
        userForm2.setUsername("testUser2");
        userForm2.setNickname("nickname");
        userForm2.setPassword("password");

        //when
        userService.saveUser(userForm1);

        //then
        assertThatThrownBy(() -> userService.saveUser(userForm2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 닉네임입니다.");
    }
}