package tight.commas.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

public class AuthenticationDto {

    @Builder
    @Getter
    public static class Request {
        private String email;
        private String password;
    }

    @Builder
    public static class Response {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;
    }
}
