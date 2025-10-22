package service.tests;

import chess.request.LoginRequest;
import chess.request.LogoutRequest;
import chess.request.RegisterRequest;
import chess.result.LoginResult;
import chess.result.LogoutResult;
import chess.result.RegisterResult;
import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.NoUserException;
import service.UnauthorizedException;
import service.WrongPasswordException;
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

    @Test
    public void loginFailureWrongUsername() {
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

        LoginRequest loginRequest = new LoginRequest("wrong username", userData.password());

        Assertions.assertThrows(NoUserException.class, () -> {
            userService.login(loginRequest);
        });
    }

    @Test
    public void loginFailureWrongPassword() {
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

        LoginRequest loginRequest = new LoginRequest(userData.username(), "wrong password");

        Assertions.assertThrows(WrongPasswordException.class, () -> {
            userService.login(loginRequest);
        });
    }

    @Test
    public void logoutSuccess() {
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

            LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
            LogoutResult logoutResult = userService.logout(logoutRequest);

            LogoutResult expected = new LogoutResult();
            Assertions.assertEquals(expected, logoutResult);
        });
    }

    @Test
    public void logoutFailureBadToken() {
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


