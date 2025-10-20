package service.tests;

import chess.request.LoginRequest;
import chess.request.RegisterRequest;
import chess.result.LoginResult;
import chess.result.RegisterResult;
import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

public class LoginTests {

    private UserService userService;
    private UserDataAccess userDataAccess;
    private AuthDataAccess authDataAccess;

    @Test
    public void loginSuccess() {
        userDataAccess = new UserDataAccess();
        authDataAccess = new AuthDataAccess();
        userService = new UserService(userDataAccess, authDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");

        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());

        Assertions.assertDoesNotThrow(() -> {
            RegisterResult registerResult = userService.register(registerRequest);
            Assertions.assertNotNull(registerResult);
            Assertions.assertNotNull(registerResult.username());
            Assertions.assertNotNull(registerResult.authToken());
        });

        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());

        Assertions.assertDoesNotThrow(() -> {
            LoginResult loginResult = userService.login(loginRequest);
            Assertions.assertNotNull(loginResult);
            Assertions.assertNotNull(loginResult.authToken());
            Assertions.assertNotNull(loginResult.username());
        });
    }
}


