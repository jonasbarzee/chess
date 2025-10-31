package service;

import chess.request.LoginRequest;
import chess.request.LogoutRequest;
import chess.request.RegisterRequest;
import chess.result.LoginResult;
import chess.result.LogoutResult;
import chess.result.RegisterResult;
import dataaccess.exceptions.DataAccessException;
import dataaccess.memdao.MemAuthDataAccess;
import dataaccess.memdao.MemUserDataAccess;
import model.AuthData;
import model.UserData;

public class UserService {
    private final MemUserDataAccess memUserDataAccess;
    private final MemAuthDataAccess memAuthDataAccess;

    public UserService(MemUserDataAccess memUserDataAccess, MemAuthDataAccess memAuthDataAccess) {
        this.memUserDataAccess = memUserDataAccess;
        this.memAuthDataAccess = memAuthDataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException, BadRequestException {

        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();

        if (username == null || password == null || email == null) {
            throw new BadRequestException("Bad request");
        }

        UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());

        try {
            memUserDataAccess.createUser(userData);
            AuthData authData = memAuthDataAccess.create(userData.username());
            return new RegisterResult(authData.username(), authData.authToken());

        } catch (DataAccessException ex) {
            throw new AlreadyTakenException("Username is already taken.");
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws NoUserException, WrongPasswordException, BadRequestException {
        String username = loginRequest.username();

        if (username == null) {
            throw new BadRequestException("username is null");
        } else if (!memUserDataAccess.userExists(username)) {
            throw new NoUserException("Given username is not registered.");
        }
        UserData userData = memUserDataAccess.getUser(username);

        if (loginRequest.password() == null) {
            throw new BadRequestException("password is null");
        } else if (!loginRequest.password().equals(userData.password())) {
            throw new WrongPasswordException("Given password was incorrect.");
        }

        AuthData authData = memAuthDataAccess.update(username);
        return new LoginResult(authData.authToken(), authData.username());

    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws UnauthorizedException {
        String authToken = logoutRequest.authToken();

        if (authToken == null) {
            throw new UnauthorizedException("authToken is null");
        } else if (!memAuthDataAccess.isAuthorized(authToken)) {
            throw new UnauthorizedException("authToken is bad");
        }
        if (memAuthDataAccess.isAuthorized(authToken)) {
            memAuthDataAccess.delete(authToken);
        }
        return new LogoutResult();
    }
}