package hello.board.infrastructure.web.message;

import hello.board.infrastructure.web.message.request.MessageForm;
import hello.board.infrastructure.web.message.response.MessageDto;
import hello.board.service.EntityFinder;
import hello.board.service.message.MessageService;
import hello.board.service.user.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final EntityFinder entityFinder;

    @GetMapping("/send")
    public String sendMessageForm(@RequestParam(value = "receiverUsername", required = false) String receiverUsername,
                                  Model model) {
        MessageForm.MessageFormBuilder messageForm = MessageForm.builder();

        if (receiverUsername != null) {
            String receiverNickname = userService.getNicknameByUserName(receiverUsername);

            messageForm.receiverUsername(receiverUsername)
                       .receiverNickname(receiverNickname);
        }

        model.addAttribute("messageForm", messageForm);
        return "messages/sendMessage";
    }

    @PostMapping("/send")
    public String sendMessage(@Validated @ModelAttribute("messageForm") MessageForm form,
                              BindingResult result, Principal pri) {
        if (result.hasErrors()) {
            return "messages/sendMessage";
        }
        messageService.sendMessage(pri.getName(), form);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public MessageDto getMessage(@PathVariable Long id) {
        return messageService.getMessageDtoById(id);
    }

    @PostMapping("/deleteAll")
    public String deleteAllMessages(Principal principal) {
        messageService.deleteAllMessages(principal.getName());
        return "redirect:/mypage";
    }

}
