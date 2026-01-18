package hello.board.controller.user;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import hello.board.ControllerTestSupport;
import hello.board.domain.Role;
import hello.board.domain.message.Message;
import hello.board.domain.user.User;
import hello.board.infrastructure.web.user.request.UserForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class UserControllerTest extends ControllerTestSupport {

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("wss3325")
                .nickname("keke")
                .password("1234")
                .grade(Role.USER)
                .build();
        userRepository.save(user);
    }

    @DisplayName("회원가입이 성공하면 로그인 페이지로 리다이렉트된다")
    @Test
    void signup() throws Exception {
        //given
        UserForm userForm = UserForm.builder()
                .nickname("sup")
                .username("rladntjq3454")
                .password("12345678")
                .build();
        //when & then
        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("nickname", userForm.getNickname())
                        .param("username", userForm.getUsername())
                        .param("password", userForm.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("successMessage"))
                .andDo(print());
    }

    @DisplayName("중복된 아이디로 회원가입 시 실패한다.")
    @Test
    void signupFailDuplicateUsername() throws Exception {
        //given
        String duplicate = "wss3325";

        //when & then
        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("nickname", "newNickname")
                        .param("username", duplicate)
                        .param("password", "12345678"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/signup"))
                .andExpect(model().attributeHasFieldErrors("form", "username"))
                .andDo(print());
    }

    @Test
    void login() throws Exception {
        //given
        //when & then
        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("username", "wss3325")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    void myPage() throws Exception {
        // given
        User receiver = User.builder()
                .username("krs3454")
                .nickname("hihihihi")
                .password("1234")
                .grade(Role.USER)
                .build();
        userRepository.save(receiver);

        User sender = userRepository.findByUsername("wss3325").orElseThrow();

        Message message = Message.builder()
                .content("정처기 1일 전.")
                .receiver(receiver)
                .sender(sender)
                .build();
        messageRepository.save(message);

        // when & then
        mockMvc.perform(get("/mypage")
                        .with(user("wss3325")))
                .andExpect(status().isOk())
                .andExpect(view().name("users/mypage"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("receivedMessages"))
                .andExpect(model().attributeExists("sentMessages"))
                .andDo(print());
    }
}