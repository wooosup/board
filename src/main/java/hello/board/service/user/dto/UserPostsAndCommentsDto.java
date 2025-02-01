package hello.board.service.user.dto;

import hello.board.service.comment.dto.CommentDto;
import hello.board.service.post.dto.MyPagePostDto;
import lombok.Getter;

import java.util.List;

@Getter
public class UserPostsAndCommentsDto {

    private final List<MyPagePostDto> posts;
    private final List<CommentDto> comments;

    public UserPostsAndCommentsDto(List<MyPagePostDto> posts, List<CommentDto> comments) {
        this.posts = posts;
        this.comments = comments;
    }
}
