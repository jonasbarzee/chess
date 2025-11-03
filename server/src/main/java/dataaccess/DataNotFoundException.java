package dataaccess;

public class DataNotFoundException extends DataAccessException {
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public DataNotFoundException(String message) {
        super(message);
    }
}
