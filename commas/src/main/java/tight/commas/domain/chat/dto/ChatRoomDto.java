package tight.commas.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatRoomDto {

    private String roomId;
    private Long parkId;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoomDto createChatRoomDto(Long parkId){
        ChatRoomDto room = new ChatRoomDto();
        room.parkId = parkId;
        room.roomId = UUID.randomUUID().toString();
        return room;
    }
}
