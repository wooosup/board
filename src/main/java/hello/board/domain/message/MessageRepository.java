package hello.board.domain.message;

import hello.board.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {

    Message save(Message message);

    Optional<Message> findById(Long id);

    List<Message> findActiveSentMessages(User sender);

    List<Message> findActiveReceivedMessages(User receiver);

    List<Message> findActiveMessages(User user);
}
