package tight.commas.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tight.commas.domain.chat.dto.ChatDto;
import tight.commas.domain.chat.dto.RoomIdDto;
import tight.commas.domain.chat.entity.Chat;
import tight.commas.domain.chat.repository.ChatRepository;
import tight.commas.domain.chat.repository.ChatRoomRepository;
import tight.commas.domain.user.entity.User;
import tight.commas.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatService {
    private final SimpMessagingTemplate template;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public void enterChatRoom(ChatDto message) {
        message.writeMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    public void sendMessage(ChatDto message) {
        chatRoomRepository.findByRoomId(message.getRoomId())
                .ifPresent(chatRoom -> {
                    Optional<User> userOptional = userRepository.findById(message.getWriter());
                    userOptional.ifPresent(user -> {
                        Chat chat = Chat.createChat(chatRoom, user, message);
                        chatRepository.save(chat);
                    });
                });
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    public List<Chat> getChatsByRoomId(RoomIdDto roomId) {
        return chatRepository.findByRoomIdOrderBySendDateAsc(roomId.getRoomId());
    }

}
