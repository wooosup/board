package hello.board.infrastructure.web.comment.request;

import hello.board.domain.comment.Comment;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentForm {

    @NotEmpty(message = "댓글을 작성해주세요.")
    private final String content;
    private final Long postId;
    private final Long parentId;

    @Builder
    private CommentForm(String content, Long postId, Long parentId) {
        this.content = content;
        this.postId = postId;
        this.parentId = parentId;
    }

    public Comment toEntity(User user, Post post, Comment parent) {
        return Comment.builder()
                .content(this.content)
                .user(user)
                .post(post)
                .parent(parent)
                .build();
    }
}
