package tight.commas.global.exception;

public class RuntimeSQLException extends DbException{
    private static final String MESSAGE = "SQL예외 입니다";
    public RuntimeSQLException() {
        super(MESSAGE);
    }
}
