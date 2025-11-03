package dataaccess;

public class DuplicateKeyException extends DataAccessException {
    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateKeyException(String message) {
        super(message);
    }
}
