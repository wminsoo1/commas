package tight.commas.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tight.commas.domain.chat.dto.ChatDto;
import tight.commas.domain.chat.entity.Chat;
import tight.commas.domain.chat.repository.ChatRepository;
import tight.commas.domain.chat.repository.ChatRoomRepository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatService {
    private final SimpMessagingTemplate template;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    public void enterChatRoom(ChatDto message) {
        message.writeMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    public void sendMessage(ChatDto message) {
        chatRoomRepository.findByRoomId(message.getRoomId())
                .ifPresent(chatRoom -> {
                    Chat chat = Chat.createChat(chatRoom, message);
                    chatRepository.save(chat);
                });
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    public List<Chat> getChatsByRoomId(String roomId) {
        return chatRepository.findByRoom_RoomIdOrderBySendDateAsc(roomId);
    }

}
