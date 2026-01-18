package hello.board.infrastructure.web.user.response;

import hello.board.domain.user.User;
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

    public static UserPostsAndCommentsDto of(User user) {
        return UserPostsAndCommentsDto.builder()
                .posts(user.getPosts().stream().map(MyPagePostDto::of).toList())
                .comments(user.getComments().stream().map(CommentDto::of).toList())
                .build();
    }
}
