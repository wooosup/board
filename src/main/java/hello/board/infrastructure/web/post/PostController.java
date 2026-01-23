package hello.board.infrastructure.web.post;

import hello.board.infrastructure.web.comment.request.CommentForm;
import hello.board.infrastructure.web.post.request.PostForm;
import hello.board.infrastructure.web.post.request.UpdatePostForm;
import hello.board.infrastructure.web.post.response.MainPostDto;
import hello.board.infrastructure.web.post.response.Pagination;
import hello.board.infrastructure.web.post.response.PostResponse;
import hello.board.infrastructure.web.post.response.PostSearch;
import hello.board.infrastructure.web.post.response.PostWithCommentsDto;
import hello.board.service.post.PostService;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String postList(@ModelAttribute PostSearch search,
                            @PageableDefault Pageable pageable,
                            Model model) {
        Page<MainPostDto> posts = postService.searchPosts(search, pageable);

        Pagination pagination = new Pagination(posts);

        model.addAttribute("posts", posts);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        return "posts/postList";
    }

    @GetMapping("/post/create")
    public String createPostForm(Model model) {
        model.addAttribute("form", PostForm.builder().build());
        return "posts/createPostForm";
    }

    @PostMapping("/post/create")
    public String createPost(@Validated @ModelAttribute("form") PostForm form, BindingResult result,
                             @RequestParam(value = "images", required = false) List<MultipartFile> images,
                             Principal pri) {
        if (result.hasErrors()) {
            return "posts/createPostForm";
        }
        PostResponse response = postService.savePost(form, images, pri.getName());

        return "redirect:/post/" + response.getId();
    }

    @GetMapping("/post/{postId}")
    public String viewPost(@PathVariable Long postId, Model model, HttpServletRequest request) {
        String clientIp = getIp(request);

        PostWithCommentsDto postWithComments = postService.readPost(postId, clientIp);

        model.addAttribute("post", postWithComments.getPostDetail());
        model.addAttribute("comments", postWithComments.getComments());
        model.addAttribute("commentForm", CommentForm.builder().build());

        return "posts/viewPost";
    }

    @GetMapping("/post/edit/{postId}")
    public String updatePostForm(@PathVariable Long postId, Model model) {
        UpdatePostForm form = postService.findPostForm(postId);
        model.addAttribute("form", form);
        model.addAttribute("postId", postId);
        return "posts/updatePostForm";
    }

    @PostMapping("/post/edit/{postId}")
    public String updatePost(@PathVariable Long postId, @Validated @ModelAttribute("form") UpdatePostForm form,
                             @RequestParam(value = "images", required = false) List<MultipartFile> images,
                             @RequestParam(value = "imageIdsToDelete", required = false) List<Long> imageIdsToDelete) {
        postService.updatePost(postId, form, images, imageIdsToDelete);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/";
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String session = request.getSession().getId();
        return ip + ":" + session;
    }

}