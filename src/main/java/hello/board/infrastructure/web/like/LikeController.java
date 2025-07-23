package hello.board.infrastructure.web.post;

import hello.board.service.poststatistics.dto.LikeResponse;
import hello.board.service.poststatistics.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
