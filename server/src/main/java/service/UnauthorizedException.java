package service;

public class UnauthorizedException extends ServiceException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
