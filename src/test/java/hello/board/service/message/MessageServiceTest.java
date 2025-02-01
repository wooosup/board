package hello.board.service.message;

import hello.board.domain.Role;
import hello.board.service.message.dto.MessageDto;
import hello.board.service.message.dto.MessageForm;
import hello.board.domain.entity.user.User;
import hello.board.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void sendMessage() throws Exception {
        //given
        User sender = User.builder()
                .username("wss3325")
                .nickname("user123")
                .password("1234")
                .grade(Role.USER)
                .build();

        User receiver = User.builder()
                .username("krs3454")
                .nickname("user321")
                .password("1234")
                .grade(Role.USER)
                .build();
        userRepository.saveAll(List.of(sender, receiver));

        MessageForm form = MessageForm.builder()
                .receiverUsername(receiver.getUsername())
                .receiverNickname(receiver.getNickname())
                .content("내용 무")
                .build();

        //when
        MessageDto message = messageService.sendMessage(sender.getUsername(), form);

        //then
        assertThat(message.getContent()).isEqualTo(form.getContent());
    }

    @Test
    void getReceiverMessage() throws Exception {
        //given
        User sender = User.builder()
                .username("wss3325")
                .nickname("user123")
                .password("1234")
                .grade(Role.USER)
                .build();

        User receiver = User.builder()
                .username("krs3454")
                .nickname("user321")
                .password("1234")
                .grade(Role.USER)
                .build();
        userRepository.saveAll(List.of(sender, receiver));

        MessageForm form = MessageForm.builder()
                .receiverUsername(receiver.getUsername())
                .receiverNickname(receiver.getNickname())
                .content("내용 무")
                .build();

        //when
        messageService.sendMessage(sender.getUsername(), form);
        List<MessageDto> messages = messageService.getReceivedMessages(receiver.getUsername());

        //then
        assertThat(messages).hasSize(1)
                .extracting("content")
                .containsExactlyInAnyOrder("내용 무");
    }
}