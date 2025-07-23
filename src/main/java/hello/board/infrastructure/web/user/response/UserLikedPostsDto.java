package hello.board.service.user.dto;

import hello.board.infrastructure.web.post.response.LikedPostDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserLikedPostsDto {

    private final List<LikedPostDto> likedPosts;

    @Builder
    private UserLikedPostsDto(List<LikedPostDto> posts) {
        this.likedPosts = posts;
    }

    public static UserLikedPostsDto of(List<LikedPostDto> likedPosts) {
        return UserLikedPostsDto.builder()
                .posts(likedPosts)
                .build();
    }
}
