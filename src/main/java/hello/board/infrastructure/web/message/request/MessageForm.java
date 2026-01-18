package hello.board.infrastructure.web.message.request;

import hello.board.domain.message.Message;
import hello.board.domain.user.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageForm {

    @NotNull(message = "수신자를 입력해주세요.")
    private final String receiverUsername;

    private final String receiverNickname;

    @NotEmpty(message = "메시지 내용을 입력해주세요.")
    private final String content;

    @Builder
    private MessageForm(String receiverUsername, String receiverNickname, String content) {
        this.receiverUsername = receiverUsername;
        this.receiverNickname = receiverNickname;
        this.content = content;
    }

    public Message toEntity(User sender, User receiver) {
        return Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(this.content)
                .build();
    }
}
