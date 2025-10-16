package dataaccess;

public class UserDataAccessException extends DataAccessException {
    public UserDataAccessException(String message) {
        super(message);
    }
    public UserDataAccessException(String message, Throwable ex) {
        super(message, ex);
    }
}
