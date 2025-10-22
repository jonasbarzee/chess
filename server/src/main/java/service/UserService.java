package service;

import chess.request.LoginRequest;
import chess.request.LogoutRequest;
import chess.request.RegisterRequest;
import chess.result.LoginResult;
import chess.result.LogoutResult;
import chess.result.RegisterResult;
import dataaccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public UserService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
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
            userDataAccess.createUser(userData);
            AuthData authData = authDataAccess.create(userData.username());
            return new RegisterResult(authData.username(), authData.authToken());

        } catch (DataAccessException ex) {
            throw new AlreadyTakenException("Username is already taken.");
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws NoUserException, WrongPasswordException, BadRequestException {
        System.out.println("in the login method!!");
        String username = loginRequest.username();

        if (username == null) {
            System.out.println("username is null");
            throw new BadRequestException("username is null");
        } else if (!userDataAccess.userExists(username)) {
            System.out.println("username doesn't exist");
            throw new NoUserException("Given username is not registered.");
        }
        UserData userData = userDataAccess.getUser(username);

        if (loginRequest.password() == null) {
            throw new BadRequestException("password is null");
        } else if (!loginRequest.password().equals(userData.password())) {
            System.out.println("password is bad");
            throw new WrongPasswordException("Given password was incorrect.");
        }

        AuthData authData = authDataAccess.update(username);
        return new LoginResult(authData.authToken(), authData.username());

    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws UnauthorizedException {
        String authToken = logoutRequest.authToken();
        System.out.println("authToken:" + authToken);

        if (authToken == null) {
            System.out.println("authToken is null");
            throw new UnauthorizedException("authToken is null");
        } else if (!authDataAccess.isAuthorized(authToken)) {
            System.out.println("authToken is bad");
            throw new UnauthorizedException("authToken is bad");
        }

        if (authDataAccess.isAuthorized(authToken)) {
            System.out.println("authToken is good");
            authDataAccess.delete(authToken);
        }
        return new LogoutResult();
    }
}