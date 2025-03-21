package hello.board.controller.user;

import hello.board.service.message.dto.MessageDto;
import hello.board.service.user.dto.UserForm;
import hello.board.service.user.dto.UserPostsAndCommentsDto;
import hello.board.service.message.MessageService;
import hello.board.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "로그인 정보가 없습니다. 확인 후 다시 입력해 주십시오.");
        }
        return "users/loginForm";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("form", UserForm.builder().build());
        return "users/signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("form") UserForm form, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "users/signup";
        }

        duplicateUser(form, result);
        if (result.hasErrors()) {
            return "users/signup";
        }

        userService.saveUser(form);

        redirect.addFlashAttribute("successMessage", "회원가입이 완료되었습니다!");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public String myPage(Model model, Principal pri) {
        String username = pri.getName();
        UserPostsAndCommentsDto userContent = userService.getUserPostsAndComments(username);
        model.addAttribute("posts", userContent.getPosts());
        model.addAttribute("comments", userContent.getComments());

        List<MessageDto> receivedMessages = messageService.getReceivedMessages(username);
        model.addAttribute("receivedMessages", receivedMessages);

        List<MessageDto> sentMessages = messageService.getSentMessages(username);
        model.addAttribute("sentMessages", sentMessages);

        return "users/mypage";
    }

    private void duplicateUser(UserForm form, BindingResult result) {
        if (userService.isUsernameDuplicate(form.getUsername())) {
            result.rejectValue("username", "duplicate", "이미 존재하는 아이디입니다.");
        }
        if (userService.isNicknameDuplicate(form.getNickname())) {
            result.rejectValue("nickname", "duplicate", "이미 존재하는 닉네임입니다.");
        }
    }
}
