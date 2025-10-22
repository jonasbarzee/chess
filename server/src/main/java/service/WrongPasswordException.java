package service;

public class WrongPasswordException extends ServiceException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
