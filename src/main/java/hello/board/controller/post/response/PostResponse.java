package hello.board.controller.post.response;

import hello.board.domain.entity.comment.Comment;
import hello.board.domain.entity.like.Like;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.post.image.Image;
import hello.board.domain.entity.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final User user;
    private final List<Like> likes;
    private final List<Comment> comments;
    private final List<Image> images;
    private final Integer likeCount;
    private final Integer viewCount;

    @Builder
    private PostResponse(Long id, String title, String content, User user, List<Like> likes, List<Comment> comments, List<Image> images, Integer likeCount, Integer viewCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.likes = likes;
        this.comments = comments;
        this.images = images;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
    }


    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .user(post.getUser())
                .likes(post.getLikes())
                .comments(post.getComments())
                .images(post.getImages())
                .likeCount(post.getLikes().size())
                .viewCount(post.getViewCount())
                .build();
    }
}
