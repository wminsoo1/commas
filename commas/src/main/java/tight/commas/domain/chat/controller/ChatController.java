package tight.commas.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import tight.commas.domain.chat.dto.ChatDto;
import tight.commas.domain.chat.dto.RoomIdDto;
import tight.commas.domain.chat.entity.Chat;
import tight.commas.domain.chat.service.ChatService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @MessageMapping(value = "/enter")
    public void enter(ChatDto message){
        chatService.enterChatRoom(message);
    }

    @MessageMapping(value = "/message")
    public void message(ChatDto message){
        chatService.sendMessage(message);
    }

    @PostMapping("/room")
    public ResponseEntity<List<Chat>> getRoom(@RequestBody RoomIdDto roomId) {
        List<Chat> chatList = chatService.getChatsByRoomId(roomId);
        return ResponseEntity.ok(chatList);
    }
}

