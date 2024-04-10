package tight.commas.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tight.commas.domain.chat.dto.ChatRoomDto;
import tight.commas.domain.chat.entity.ChatRoom;
import tight.commas.domain.chat.repository.ChatRoomRepository;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public void createChatRoom() {
        ChatRoomDto chatRoomDto = ChatRoomDto.createChatRoomDto();
        ChatRoom chatRoom = ChatRoom.createChatRoom(chatRoomDto);
        chatRoomRepository.save(chatRoom);
    }

}
