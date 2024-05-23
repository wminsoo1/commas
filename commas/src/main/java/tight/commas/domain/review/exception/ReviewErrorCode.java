package tight.commas.domain.review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import tight.commas.global.exception.ErrorCode;


@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND,"리뷰가 존재하지 않습니다."),
    INVALID_REVIEW_DATA(HttpStatus.BAD_REQUEST, "유효하지 않은 리뷰 데이터입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
