package hello.board.infrastructure.web.comment;

import hello.board.infrastructure.web.comment.request.CommentForm;
import hello.board.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public String createComment(@PathVariable Long postId, @Validated @ModelAttribute("commentForm") CommentForm commentForm, BindingResult result, Principal pri) {
        if (pri == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            return "redirect:/post/" + postId;
        }
        commentService.saveComment(commentForm, pri.getName());
        return "redirect:/post/" + postId;
    }

    @PostMapping("/edit/{commentId}")
    public String updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                                @RequestParam("content") String content) {
        if (content == null || content.trim().isEmpty()) {
            return "redirect:/post/" + postId;
        }

        commentService.updateComment(content, commentId);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return "redirect:/post/" + postId;
    }

}
