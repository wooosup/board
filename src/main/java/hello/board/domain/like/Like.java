package hello.board.domain.like;

import static jakarta.persistence.FetchType.LAZY;

import hello.board.domain.BaseEntity;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})
        }
)
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    private Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public static Like create(User user, Post post) {
        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();
        post.incrementLikeCount();

        return like;
    }

    public void disconnect() {
        this.post.decrementLikeCount();
    }
}
