package tight.commas.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tight.commas.domain.chat.entity.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByRoom_RoomIdOrderBySendDateAsc(String roomId);
}
