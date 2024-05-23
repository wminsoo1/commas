package tight.commas.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import tight.commas.global.exception.ErrorCode;


@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"유저가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
