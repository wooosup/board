package hello.board.controller.post;

import hello.board.controller.post.response.PostResponse;
import hello.board.controller.post.response.PostWithCommentsDto;
import hello.board.service.comment.dto.CommentForm;
import hello.board.service.post.PostService;
import hello.board.service.post.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String postList(@RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "searchField", required = false) String searchField,
                           @RequestParam(defaultValue = "0") int page, Model model) {
        PostSearch search = PostSearch.builder()
                .keyword(keyword)
                .searchField(searchField)
                .build();

        Pageable pageable = PageRequest.of(page, 10);
        Page<MainPostDto> posts = postService.searchPosts(search, pageable);

        int pageBlockSize = 5;
        int currentPage = posts.getNumber();
        int totalPages = posts.getTotalPages();

        int startPage = (currentPage / pageBlockSize) * pageBlockSize;
        int endPage = Math.min(startPage + pageBlockSize - 1, totalPages - 1);

        model.addAttribute("posts", posts);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "posts/postList";
    }

    @GetMapping("/post/create")
    public String createPostForm(Model model) {
        model.addAttribute("form", PostForm.builder().build());
        return "posts/createPostForm";
    }

    @PostMapping("/post/create")
    public String createPost(@Validated @ModelAttribute("form") PostForm form, BindingResult result, Principal pri,
                             @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        if (result.hasErrors()) {
            return "posts/createPostForm";
        }
        PostResponse response = postService.savePost(form, images, pri.getName());
        return "redirect:/post/" + response.getId();
    }

    @GetMapping("/post/{postId}")
    public String viewPost(@PathVariable Long postId, Model model, HttpServletRequest request) {
        PostDetailDto postDetail = postService.findByPostId(postId, request);
        PostWithCommentsDto postWithComments = postService.findPostWithComments(postId, request);

        model.addAttribute("post", postDetail);
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
                             @RequestParam(value = "imageIdsToDelete", required = false) List<Long> imageIdsToDelete,
                             Principal pri) {
        postService.updatePost(postId, form, images, pri.getName(), imageIdsToDelete);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId, Principal pri) {
        postService.deletePost(postId, pri.getName());
        return "redirect:/";
    }
}