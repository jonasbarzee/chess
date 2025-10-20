package service.tests;

import chess.request.RegisterRequest;
import chess.result.RegisterResult;
import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AlreadyTakenException;
import service.UserService;

public class RegisterTests {

    private UserService userService;
    private UserDataAccess userDataAccess;
    private AuthDataAccess authDataAccess;

    @Test
    public void registerSuccess() {
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
    }

    @Test
    public void registerFailureUserExists() {
        userDataAccess = new UserDataAccess();
        authDataAccess = new AuthDataAccess();
        userService = new UserService(userDataAccess, authDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");
        AuthData authData = new AuthData("authToken", userData.username());

        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), authData.authToken());

        Assertions.assertDoesNotThrow(() -> {
            RegisterResult registerResult = userService.register(registerRequest);
            Assertions.assertNotNull(registerResult);
            Assertions.assertNotNull(registerResult.username());
            Assertions.assertNotNull(registerResult.authToken());
        });

        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            userService.register(registerRequest);
        });
    }
}
