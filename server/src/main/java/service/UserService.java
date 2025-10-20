package service;

import chess.request.RegisterRequest;
import chess.result.RegisterResult;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public UserService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest, UserData userToReg, AuthData authDataToReg) throws AlreadyTakenException {
        try {
            UserData userData = userDataAccess.getUser(registerRequest.username());
            if (userData != null) {
                throw new AlreadyTakenException("Username is already taken.");
            }
            userDataAccess.createUser(userToReg);
            AuthData authData = authDataAccess.create(authDataToReg);
            return new RegisterResult(authData.username(), authData.authToken());
        } catch (DataAccessException ex) {
            return null;
        }
    }
    // public LoginResult login(LoginRequest loginRequest) {}
    // public void logout(LogoutRequest logoutRequest) {}
}