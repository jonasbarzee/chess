package service.tests;

import chess.request.CreateGameRequest;
import chess.request.JoinGameRequest;
import chess.result.CreateGameResult;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AlreadyTakenException;
import service.GameService;
import service.UnauthorizedException;
import service.UserNotRegisteredException;

public class GameTests {

    private GameService gameService;
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;
    private UserDataAccess userDataAccess;

    @Test
    public void createGameSuccess() {
        gameDataAccess = new GameDataAccess();
        authDataAccess = new AuthDataAccess();
        userDataAccess = new UserDataAccess();
        gameService = new GameService(gameDataAccess, authDataAccess, userDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");

        Assertions.assertDoesNotThrow(() -> {
            userDataAccess.createUser(userData);
            AuthData authData = authDataAccess.create(userData.username());
            CreateGameRequest createGameRequest = new CreateGameRequest("mygame", authData.authToken());
            gameService.createGame(createGameRequest);
        });
    }

    // Should I have to register before I can create a game??
    @Test
    public void createGameFailureNotAuthorized() {
        gameDataAccess = new GameDataAccess();
        authDataAccess = new AuthDataAccess();
        userDataAccess = new UserDataAccess();
        gameService = new GameService(gameDataAccess, authDataAccess, userDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            CreateGameRequest createGameRequest = new CreateGameRequest("mygame", authDataAccess.generateToken());
            gameService.createGame(createGameRequest);
        });
    }

    @Test
    public void joinGameSuccessWhite() {
        gameDataAccess = new GameDataAccess();
        authDataAccess = new AuthDataAccess();
        userDataAccess = new UserDataAccess();
        gameService = new GameService(gameDataAccess, authDataAccess, userDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");

        Assertions.assertDoesNotThrow(() -> {
            userDataAccess.createUser(userData);
            AuthData authData = authDataAccess.create(userData.username());
            CreateGameRequest createGameRequest = new CreateGameRequest("mygame", authData.authToken());
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(), "white", createGameResult.gameID());
            gameService.joinGame(joinGameRequest);
        });
    }

    @Test
    public void joinGameSuccessBlack() {
        gameDataAccess = new GameDataAccess();
        authDataAccess = new AuthDataAccess();
        userDataAccess = new UserDataAccess();
        gameService = new GameService(gameDataAccess, authDataAccess, userDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");

        Assertions.assertDoesNotThrow(() -> {
            userDataAccess.createUser(userData);
            AuthData authData = authDataAccess.create(userData.username());
            CreateGameRequest createGameRequest = new CreateGameRequest("mygame", authData.authToken());
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(), "BLACK", createGameResult.gameID());
            gameService.joinGame(joinGameRequest);
        });
    }

    @Test
    public void joinGameFailureWhiteAlreadyTaken() {
        gameDataAccess = new GameDataAccess();
        authDataAccess = new AuthDataAccess();
        userDataAccess = new UserDataAccess();
        gameService = new GameService(gameDataAccess, authDataAccess, userDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");
        UserData userData1 = new UserData("username1", "password1", "email1@email.com");

        Assertions.assertDoesNotThrow(() -> {
            userDataAccess.createUser(userData);
            AuthData authData = authDataAccess.create(userData.username());
            CreateGameRequest createGameRequest = new CreateGameRequest("mygame", authData.authToken());
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(), "white", createGameResult.gameID());
            gameService.joinGame(joinGameRequest);

            userDataAccess.createUser(userData1);
            AuthData authData1 = authDataAccess.create(userData1.username());

            Assertions.assertThrows(AlreadyTakenException.class, () -> {
                JoinGameRequest joinGameRequest1 = new JoinGameRequest(authData1.authToken(), "white", createGameResult.gameID());
                gameService.joinGame(joinGameRequest1);
            });
        });

    }
}
