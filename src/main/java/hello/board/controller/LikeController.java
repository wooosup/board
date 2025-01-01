package hello.board.controller;

import hello.board.domain.like.LikeResponse;
import hello.board.service.post.poststatistics.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        if (pri == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = pri.getName();

        int updatedLikeCount = likeService.likePost(postId, username);
        LikeResponse response = new LikeResponse(updatedLikeCount);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{postId}/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        Long likeCount = likeService.getLikeCount(postId);
        return ResponseEntity.ok(likeCount);
    }
}
