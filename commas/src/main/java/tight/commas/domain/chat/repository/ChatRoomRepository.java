package tight.commas.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tight.commas.domain.chat.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(String roomId);

}
