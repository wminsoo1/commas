package tight.commas.global.auth.dto;

import lombok.Builder;
import lombok.Getter;

public class RegisterDto {

    @Builder
    @Getter
    public static class Request {
        private String email;
        private String nickname;
        private String password;
    }
}
