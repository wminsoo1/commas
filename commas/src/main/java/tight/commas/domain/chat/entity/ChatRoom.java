package tight.commas.domain.chat.entity;
import jakarta.persistence.*;
import lombok.*;
import tight.commas.domain.chat.dto.ChatRoomDto;
import tight.commas.domain.park.entity.Park;

import java.util.UUID;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoom_id")
    private Long id;

    private String roomId;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    private Park park;

    public static ChatRoom createChatRoom(Park park) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = park.getParkName();
        chatRoom.park = park;
        return chatRoom;
    }

}