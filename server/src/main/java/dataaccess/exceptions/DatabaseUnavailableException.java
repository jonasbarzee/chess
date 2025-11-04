package dataaccess.exceptions;

public class DatabaseUnavailableException extends DataAccessException {
    public DatabaseUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
