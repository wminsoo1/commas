package tight.commas.global.exception;

public class RunTimeConnectException extends DbException{
    private static final String MESSAGE = "SQL예외 입니다";
    public RunTimeConnectException(String message) {
        super(message);
    }
}
