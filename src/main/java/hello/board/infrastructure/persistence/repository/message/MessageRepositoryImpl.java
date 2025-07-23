package hello.board.infrastructure.persistence.repository.message;

import hello.board.domain.message.Message;
import hello.board.domain.message.MessageRepository;
import hello.board.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private final MessageJpaRepository messageJpaRepository;

    @Override
    public Message save(Message message) {
        return messageJpaRepository.save(message);
    }

    @Override
    public Optional<Message> findById(Long id) {
        return messageJpaRepository.findById(id);
    }

    @Override
    public List<Message> findActiveSentMessages(User sender) {
        return messageJpaRepository.findActiveSentMessages(sender);
    }

    @Override
    public List<Message> findActiveReceivedMessages(User receiver) {
        return messageJpaRepository.findActiveReceivedMessages(receiver);
    }
}
