package service;

import chess.request.LoginRequest;
import chess.request.RegisterRequest;
import chess.result.LoginResult;
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

    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException {
        UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());

        try {
            userDataAccess.createUser(userData);
            AuthData authData = authDataAccess.create(userData.username());
            return new RegisterResult(authData.username(), authData.authToken());

        } catch (DataAccessException ex) {
            throw new AlreadyTakenException("Username is already taken.");
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws UserNotRegisteredException, AlreadyTakenException {
        String username = loginRequest.username();
        UserData userData = userDataAccess.getUser(username);

        if (userData == null) {
            throw new UserNotRegisteredException("Given username is not registered.");
        } else if (!loginRequest.password().equals(userData.password())) {
            throw new UserNotRegisteredException("Given password was incorrect.");
        }

        AuthData authData = authDataAccess.update(username);
        return new LoginResult(authData.authToken(), authData.username());

    }
    // public void logout(LogoutRequest logoutRequest) {}
}