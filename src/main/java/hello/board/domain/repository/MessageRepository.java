package hello.board.domain.repository;

import hello.board.domain.entity.message.Message;
import hello.board.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.sender = :sender AND m.isDeletedBySender = false ORDER BY m.sentTime DESC")
    List<Message> findActiveSentMessages(@Param("sender") User sender);

    @Query("SELECT m FROM Message m WHERE m.receiver = :receiver AND m.isDeletedByReceiver = false ORDER BY m.sentTime DESC")
    List<Message> findActiveReceivedMessages(@Param("receiver") User receiver);
}
