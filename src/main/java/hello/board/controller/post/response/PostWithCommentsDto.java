package hello.board.controller.post.response;

import hello.board.service.comment.dto.CommentDto;
import hello.board.service.post.dto.PostDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostWithCommentsDto {

    private final PostDetailDto postDetail;
    private final List<CommentDto> comments;

    @Builder
    private PostWithCommentsDto(PostDetailDto postDetail, List<CommentDto> comments) {
        this.postDetail = postDetail;
        this.comments = comments;
    }

    public static PostWithCommentsDto of(PostDetailDto postDetail, List<CommentDto> comments) {
        return PostWithCommentsDto.builder()
                .postDetail(postDetail)
                .comments(comments)
                .build();
    }
}
