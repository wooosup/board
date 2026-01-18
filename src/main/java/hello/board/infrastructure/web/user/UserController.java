package hello.board.infrastructure.web.user;

import hello.board.infrastructure.web.message.response.MessageDto;
import hello.board.infrastructure.web.user.request.UserForm;
import hello.board.infrastructure.web.user.response.UserLikedPostsDto;
import hello.board.infrastructure.web.user.response.UserPostsAndCommentsDto;
import hello.board.infrastructure.web.user.validator.UserSignUpValidator;
import hello.board.service.message.MessageService;
import hello.board.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class UserController {
    public static final String USERS_SIGNUP = "users/signup";
    public static final String REDIRECT_LOGIN = "redirect:/login";
    public static final String USERS_LOGIN_FORM = "users/loginForm";
    public static final String USERS_MYPAGE = "users/mypage";

    private final UserService userService;
    private final MessageService messageService;
    private final UserSignUpValidator signUpValidator;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "로그인 정보가 없습니다. 확인 후 다시 입력해 주십시오.");
        }
        return USERS_LOGIN_FORM;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("form", UserForm.builder().build());
        return USERS_SIGNUP;
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("form") UserForm form, BindingResult result,
                         RedirectAttributes redirect) {
        signUpValidator.validate(form, result);

        if (result.hasErrors()) {
            return USERS_SIGNUP;
        }

        userService.saveUser(form);

        setUpSuccessRedirect(redirect);
        return REDIRECT_LOGIN;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return REDIRECT_LOGIN;
    }

    @GetMapping("/mypage")
    public String myPage(Model model, Principal pri) {
        String username = pri.getName();

        // 사용자 게시글 및 댓글 조회
        UserPostsAndCommentsDto userContent = userService.getUserPostsAndComments(username);
        model.addAttribute("posts", userContent.getPosts());
        model.addAttribute("comments", userContent.getComments());

        // 좋아요 누른 글 조회
        UserLikedPostsDto userLikedPosts = userService.getUserLikedPosts(username);
        model.addAttribute("likedPosts", userLikedPosts.getLikedPosts());

        // 받은 메시지 조회
        List<MessageDto> receivedMessages = messageService.getReceivedMessages(username);
        model.addAttribute("receivedMessages", receivedMessages);

        // 보낸 메시지 조회
        List<MessageDto> sentMessages = messageService.getSentMessages(username);
        model.addAttribute("sentMessages", sentMessages);

        return USERS_MYPAGE;
    }

    @PostMapping("/delete")
    public String deleteMember(Principal principal, HttpServletRequest request) {
        if (principal != null) {
            userService.deleteMember(principal.getName());
            request.getSession().invalidate();

            return "redirect:/";
        }
        return "redirect:/login";
    }

    private void setUpSuccessRedirect(RedirectAttributes redirect) {
        redirect.addFlashAttribute("successMessage", "회원가입이 완료되었습니다!");
    }

}
