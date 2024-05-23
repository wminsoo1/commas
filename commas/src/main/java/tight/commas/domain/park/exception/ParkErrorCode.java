package tight.commas.domain.park.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import tight.commas.global.exception.ErrorCode;


@Getter
@RequiredArgsConstructor
public enum ParkErrorCode implements ErrorCode {
    PARK_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 공원을 찾을 수 없습니다."),
    USERPARK_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 좋아요를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
