package tight.commas.domain.chat.dto;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ChatDto {
    private String roomId;
    private String writer;
    private String message;
    private LocalDateTime sendDate;

    public void writeMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatDto{" +
                "roomId='" + roomId + '\'' +
                ", writer='" + writer + '\'' +
                ", message='" + message + '\'' +
                ", sendDate=" + sendDate +
                '}';
    }
}

