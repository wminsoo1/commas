package tight.commas.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import tight.commas.domain.user.dto.UserDto;

public class AuthenticationDto {

    @Builder
    @Getter
    public static class Request {
        private String email;
        private String password;
    }

    @Builder
    @Getter
    public static class Response {

        private UserDto.Response user;

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;
    }
}
