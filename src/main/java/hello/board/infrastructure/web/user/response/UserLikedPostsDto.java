package hello.board.infrastructure.web.user.response;

import hello.board.domain.like.Like;
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

    public static UserLikedPostsDto of(List<Like> likedPosts) {
        return UserLikedPostsDto.builder()
                .posts(likedPosts.stream()
                        .map(LikedPostDto::of)
                        .toList()
                )
                .build();
    }
}
