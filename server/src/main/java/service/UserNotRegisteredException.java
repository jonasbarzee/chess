package service;

public class UserNotRegisteredException extends ServiceException {
    public UserNotRegisteredException(String message) {
        super(message);
    }
}
