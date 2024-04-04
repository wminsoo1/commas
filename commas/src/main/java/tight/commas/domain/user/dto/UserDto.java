package tight.commas.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import tight.commas.domain.user.entity.User;

public class UserDto {
    @Builder
    @Getter
    public static class Response {
        private Long userId;
        private String nickname;
        private String email;

        public static Response fromUser(User user) {
            return Response.builder()
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .build();
        }
    }
}
