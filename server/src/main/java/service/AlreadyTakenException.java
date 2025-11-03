package service;

public class AlreadyTakenException extends ChessServerException {
    public AlreadyTakenException(String message) {
        super(message, 401);
    }
}
