package hello.board.global.aspect;

import hello.board.domain.comment.Comment;
import hello.board.domain.comment.CommentRepository;
import hello.board.domain.post.Post;
import hello.board.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Before("@annotation(hello.board.global.annotation.CheckPostOwner)")
    public void checkPostOwner(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        Object[] args = joinPoint.getArgs();
        Long postId = (Long) args[0];

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!post.getUser().getUsername().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 게시글에 대한 권한이 없습니다.");
        }
    }

    @Before("@annotation(hello.board.global.annotation.CheckCommentOwner)")
    public void checkCommentOwner(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        Long commentId = findId(joinPoint.getArgs());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUsername().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 댓글에 대한 권한이 없습니다.");
        }
    }

    private Long findId(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Long) {
                return (Long) arg;
            }
        }
        throw new IllegalStateException("메서드 파라미터에서 ID를 찾을 수 없습니다.");
    }
}
