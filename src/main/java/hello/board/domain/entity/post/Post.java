package hello.board.domain.entity.post;

import hello.board.domain.BaseEntity;
import hello.board.domain.entity.comment.Comment;
import hello.board.domain.entity.like.Like;
import hello.board.domain.entity.post.image.Image;
import hello.board.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Image> images = new ArrayList<>();

    @Version
    private Integer version;

    private Integer likeCount = 0;

    @Transient
    private Integer viewCount = 0;

    @Builder
    private Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public int getHotScore() {
        return this.viewCount + (this.likeCount * 10);
    }
}
