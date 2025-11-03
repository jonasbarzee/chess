package service;

public class NoUserException extends ChessServerException {
    public NoUserException(String message) {
        super(message, 401);
    }
}
