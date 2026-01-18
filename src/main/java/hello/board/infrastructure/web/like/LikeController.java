package hello.board.infrastructure.web.like;

import hello.board.infrastructure.web.like.response.LikeResponse;
import hello.board.service.poststatistics.LikeService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    public ResponseEntity<LikeResponse> toggleLike(@PathVariable Long postId, Principal pri) {
        String username = pri.getName();

        LikeResponse response = likeService.likePost(postId, username);

        return ResponseEntity.ok(response);
    }
}
