package dataaccess;

public class AuthDataAccessException extends GameDataAccessException {
    public AuthDataAccessException(String message) {
        super(message);
    }
    public AuthDataAccessException(String message, Throwable ex) {
        super(message, ex);
    }
}
