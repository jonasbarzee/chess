package dataaccess;

public class GameDataAccessException extends DataAccessException {
    public GameDataAccessException(String message) {
        super(message);
    }
    public GameDataAccessException(String message, Throwable ex) {
        super(message, ex);
    }
}
