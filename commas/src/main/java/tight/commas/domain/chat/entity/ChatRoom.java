package tight.commas.domain.chat.entity;
import jakarta.persistence.*;
import lombok.*;
import tight.commas.domain.chat.dto.ChatRoomDto;


@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoom_id")
    private Long id;

    private String roomId;


    public static ChatRoom createChatRoom(ChatRoomDto chatRoomDto){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = chatRoomDto.getRoomId();
        return chatRoom;
    }
}