package service.tests;

import chess.request.LoginRequest;
import chess.request.LogoutRequest;
import chess.request.RegisterRequest;
import chess.result.LoginResult;
import chess.result.LogoutResult;
import chess.result.RegisterResult;
import dataaccess.memorydao.MemAuthDataAccess;
import dataaccess.memorydao.MemUserDataAccess;
import dataaccess.model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.NoUserException;
import service.UnauthorizedException;
import service.WrongPasswordException;
import service.UserService;

public class LoginTests {

    private UserService userService;
    private final UserData userData = new UserData("username", "password", "email@email.com");
    private final RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
    private final LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());

    @BeforeEach
    public void setup() {
        MemUserDataAccess memUserDataAccess = new MemUserDataAccess();
        MemAuthDataAccess memAuthDataAccess = new MemAuthDataAccess();
        userService = new UserService(memUserDataAccess, memAuthDataAccess);

        Assertions.assertDoesNotThrow(() -> {
            RegisterResult registerResult = userService.register(registerRequest);
            Assertions.assertNotNull(registerResult);
            Assertions.assertNotNull(registerResult.username());
            Assertions.assertNotNull(registerResult.authToken());
        });
    }

    @Test
    public void loginSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            LoginResult loginResult = userService.login(loginRequest);
            Assertions.assertNotNull(loginResult);
            Assertions.assertNotNull(loginResult.authToken());
            Assertions.assertNotNull(loginResult.username());
        });
    }

    @Test
    public void loginFailureWrongUsername() {
        LoginRequest loginRequest = new LoginRequest("wrong username", userData.password());

        Assertions.assertThrows(NoUserException.class, () -> {
            userService.login(loginRequest);
        });
    }

    @Test
    public void loginFailureWrongPassword() {
        LoginRequest loginRequest = new LoginRequest(userData.username(), "wrong password");

        Assertions.assertThrows(WrongPasswordException.class, () -> {
            userService.login(loginRequest);
        });
    }

    @Test
    public void logoutSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            LoginResult loginResult = userService.login(loginRequest);
            Assertions.assertNotNull(loginResult);
            Assertions.assertNotNull(loginResult.authToken());
            Assertions.assertNotNull(loginResult.username());

            LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
            LogoutResult logoutResult = userService.logout(logoutRequest);

            LogoutResult expected = new LogoutResult();
            Assertions.assertEquals(expected, logoutResult);
        });
    }

    @Test
    public void logoutFailureBadToken() {
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            LoginResult loginResult = userService.login(loginRequest);
            Assertions.assertNotNull(loginResult);
            Assertions.assertNotNull(loginResult.authToken());
            Assertions.assertNotNull(loginResult.username());

            LogoutRequest logoutRequest = new LogoutRequest("");
            LogoutResult logoutResult = userService.logout(logoutRequest);

            LogoutResult expected = new LogoutResult();
            Assertions.assertEquals(expected, logoutResult);
        });
    }
}


