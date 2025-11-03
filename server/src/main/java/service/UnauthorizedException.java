package service;

public class UnauthorizedException extends ChessServerException {
    public UnauthorizedException(String message) {
        super(message, 403);
    }
}
