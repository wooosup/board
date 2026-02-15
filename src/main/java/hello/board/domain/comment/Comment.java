package hello.board.domain.comment;

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
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    private boolean deleted = false;

    @Builder
    private Comment(String content, Post post, User user, Comment parent) {
        this.content = content;
        this.post = post;
        this.user = user;
        this.parent = parent;
    }

    public static Comment create(String content, Post post, User user, Comment parent) {
        Comment comment = Comment.builder()
                .content(content)
                .post(post)
                .user(user)
                .parent(parent)
                .build();

        if (parent != null) {
            parent.addReply(comment);
        }
        return comment;
    }

    private void addReply(Comment reply) {
        this.children.add(reply);
    }

    public Comment detachFromParent() {
        if (this.parent == null) {
            return null;
        }

        Comment parent = this.parent;
        parent.removeChild(this);
        return parent;
    }

    public void updateComment(String newContent) {
        this.content = newContent;
    }

    public void delete() {
        this.deleted = true;
    }

    public void removeChild(Comment current) {
        this.children.remove(current);
    }

    public boolean isPhysicallyDeletable() {
        return this.deleted && hasNoActiveChildren();
    }

    private boolean hasNoActiveChildren() {
        return children.isEmpty() || children.stream().allMatch(Comment::isDeleted);
    }
}
