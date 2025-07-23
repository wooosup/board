package hello.board.service.user.dto;

import hello.board.infrastructure.web.comment.response.CommentDto;
import hello.board.infrastructure.web.post.response.MyPagePostDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserPostsAndCommentsDto {

    private final List<MyPagePostDto> posts;
    private final List<CommentDto> comments;

    @Builder
    private UserPostsAndCommentsDto(List<MyPagePostDto> posts, List<CommentDto> comments) {
        this.posts = posts;
        this.comments = comments;
    }

    public static UserPostsAndCommentsDto of(List<MyPagePostDto> posts, List<CommentDto> comments) {
        return UserPostsAndCommentsDto.builder()
                .posts(posts)
                .comments(comments)
                .build();
    }
}
