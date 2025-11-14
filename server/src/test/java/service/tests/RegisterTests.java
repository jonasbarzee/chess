package service.tests;

import chess.request.RegisterRequest;
import chess.result.RegisterResult;
import dataaccess.memorydao.MemAuthDataAccess;
import dataaccess.memorydao.MemUserDataAccess;
import dataaccess.model.AuthData;
import dataaccess.model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AlreadyTakenException;
import service.UserService;

public class RegisterTests {

    private UserService userService;
    private MemUserDataAccess memUserDataAccess;
    private MemAuthDataAccess memAuthDataAccess;

    @Test
    public void registerSuccess() {
        memUserDataAccess = new MemUserDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        userService = new UserService(memUserDataAccess, memAuthDataAccess);

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
        memUserDataAccess = new MemUserDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        userService = new UserService(memUserDataAccess, memAuthDataAccess);

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
