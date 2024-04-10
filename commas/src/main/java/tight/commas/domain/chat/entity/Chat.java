package tight.commas.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import tight.commas.domain.chat.dto.ChatDto;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @Column(name = "sender_id")
    private String senderId;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendDate;

    public static Chat createChat(ChatRoom chatRoom, ChatDto chatDto){
        Chat chat = new Chat();
        chat.room = chatRoom;
        chat.senderId = chatDto.getWriter();
        chat.message = chatDto.getMessage();
        chat.sendDate = LocalDateTime.now();
        return chat;
    }

}