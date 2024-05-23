package tight.commas.global.exception;

import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;


    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
