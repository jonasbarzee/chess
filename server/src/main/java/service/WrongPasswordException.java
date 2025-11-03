package service;

public class WrongPasswordException extends ChessServerException {
    public WrongPasswordException(String message) {
        super(message, 401);
    }
}
