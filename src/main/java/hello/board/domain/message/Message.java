package hello.board.domain.message;

import static jakarta.persistence.FetchType.LAZY;

import hello.board.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    @Column(name = "is_read")
    private boolean isRead = false;
    private boolean isDeletedBySender = false;
    private boolean isDeletedByReceiver = false;

    @Builder
    private Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sentTime = LocalDateTime.now();
    }

    public static Message create(User sender, User receiver, String content) {
        return Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .build();
    }

    public void deleteBy(User user) {
        if (this.sender.equals(user)) {
            this.isDeletedBySender = true;
        }
        if (this.receiver.equals(user)) {
            this.isDeletedByReceiver = true;
        }
    }

}
