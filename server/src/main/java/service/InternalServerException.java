package service;

public class InternalServerException extends ChessServerException {
    public InternalServerException(String message) {
        super(message, 500);
    }
}
