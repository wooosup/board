package hello.board.controller.comment.response;

import hello.board.domain.entity.comment.Comment;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentResponse {
    private final Long id;
    private final String content;
    private final Post post;
    private final User user;
    private final Comment parent;
    private final List<Comment> children;
    private final boolean deleted;

    @Builder
    private CommentResponse(Long id, String content, Post post, User user, Comment parent, List<Comment> children, boolean deleted) {
        this.id = id;
        this.content = content;
        this.post = post;
        this.user = user;
        this.parent = parent;
        this.children = children;
        this.deleted = deleted;
    }

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .post(comment.getPost())
                .user(comment.getUser())
                .parent(comment.getParent())
                .children(comment.getChildren())
                .deleted(comment.isDeleted())
                .build();
    }
}
