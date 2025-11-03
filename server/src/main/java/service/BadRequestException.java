package service;

public class BadRequestException extends ChessServerException {
    public BadRequestException(String message) {
        super(message, 400);
    }
}
