package service.tests;

import chess.request.CreateGameRequest;
import chess.request.JoinGameRequest;
import chess.request.ListGamesRequest;
import chess.result.CreateGameResult;
import chess.result.ListGamesResult;
import dataaccess.memdao.MemAuthDataAccess;
import dataaccess.memdao.MemGameDataAccess;
import dataaccess.memdao.MemUserDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AlreadyTakenException;
import service.GameService;
import service.UnauthorizedException;

public class GameTests {

    private GameService gameService;
    private MemGameDataAccess memGameDataAccess;
    private MemAuthDataAccess memAuthDataAccess;
    private MemUserDataAccess memUserDataAccess;

    @Test
    public void createGameSuccess() {
        memGameDataAccess = new MemGameDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        memUserDataAccess = new MemUserDataAccess();
        gameService = new GameService(memGameDataAccess, memAuthDataAccess, memUserDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");

        Assertions.assertDoesNotThrow(() -> {
            memUserDataAccess.createUser(userData);
            AuthData authData = memAuthDataAccess.create(userData.username());
            CreateGameRequest createGameRequest = new CreateGameRequest("mygame", authData.authToken());
            gameService.createGame(createGameRequest);
        });
    }

    // Should I have to register before I can create a game??
    @Test
    public void createGameFailureNotAuthorized() {
        memGameDataAccess = new MemGameDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        memUserDataAccess = new MemUserDataAccess();
        gameService = new GameService(memGameDataAccess, memAuthDataAccess, memUserDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            CreateGameRequest createGameRequest = new CreateGameRequest("mygame", memAuthDataAccess.generateToken());
            gameService.createGame(createGameRequest);
        });
    }

    @Test
    public void joinGameSuccessWhite() {
        memGameDataAccess = new MemGameDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        memUserDataAccess = new MemUserDataAccess();
        gameService = new GameService(memGameDataAccess, memAuthDataAccess, memUserDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");

        Assertions.assertDoesNotThrow(() -> {
            memUserDataAccess.createUser(userData);
            AuthData authData = memAuthDataAccess.create(userData.username());
            CreateGameRequest createGameRequest = new CreateGameRequest("mygame", authData.authToken());
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(), "white", createGameResult.gameID());
            gameService.joinGame(joinGameRequest);
        });
    }

    @Test
    public void joinGameSuccessBlack() {
        memGameDataAccess = new MemGameDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        memUserDataAccess = new MemUserDataAccess();
        gameService = new GameService(memGameDataAccess, memAuthDataAccess, memUserDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");

        Assertions.assertDoesNotThrow(() -> {
            memUserDataAccess.createUser(userData);
            AuthData authData = memAuthDataAccess.create(userData.username());
            CreateGameRequest createGameRequest = new CreateGameRequest("myGame", authData.authToken());
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(), "BLACK", createGameResult.gameID());
            gameService.joinGame(joinGameRequest);
        });
    }

    @Test
    public void joinGameFailureWhiteAlreadyTaken() {
        memGameDataAccess = new MemGameDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        memUserDataAccess = new MemUserDataAccess();
        gameService = new GameService(memGameDataAccess, memAuthDataAccess, memUserDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");
        UserData userData1 = new UserData("username1", "password1", "email1@email.com");

        Assertions.assertDoesNotThrow(() -> {
            memUserDataAccess.createUser(userData);
            AuthData authData = memAuthDataAccess.create(userData.username());
            CreateGameRequest createGameRequest = new CreateGameRequest("myGame", authData.authToken());
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(), "white", createGameResult.gameID());
            gameService.joinGame(joinGameRequest);

            memUserDataAccess.createUser(userData1);
            AuthData authData1 = memAuthDataAccess.create(userData1.username());

            Assertions.assertThrows(AlreadyTakenException.class, () -> {
                JoinGameRequest joinGameRequest1 = new JoinGameRequest(authData1.authToken(), "white", createGameResult.gameID());
                gameService.joinGame(joinGameRequest1);
            });
        });
    }

    @Test
    public void getGamesSuccess() {
        memGameDataAccess = new MemGameDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        memUserDataAccess = new MemUserDataAccess();
        gameService = new GameService(memGameDataAccess, memAuthDataAccess, memUserDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");
        UserData userData1 = new UserData("username1", "password1", "email1@email.com");

        Assertions.assertDoesNotThrow(() -> {
            memUserDataAccess.createUser(userData);
            AuthData authData = memAuthDataAccess.create(userData.username());
            CreateGameRequest createGameRequest = new CreateGameRequest("myGame", authData.authToken());
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(), "white", createGameResult.gameID());
            gameService.joinGame(joinGameRequest);

            memUserDataAccess.createUser(userData1);
            AuthData authData1 = memAuthDataAccess.create(userData1.username());
            JoinGameRequest joinGameRequest1 = new JoinGameRequest(authData1.authToken(), "black", createGameResult.gameID());
            gameService.joinGame(joinGameRequest1);

            ListGamesRequest listGamesRequest = new ListGamesRequest(authData.authToken());
            ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);

            Assertions.assertNotNull(listGamesResult);
            Assertions.assertNotNull(listGamesResult.games());
        });
    }

    @Test
    public void getGamesFailureUnauthorized() {
        memGameDataAccess = new MemGameDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        memUserDataAccess = new MemUserDataAccess();
        gameService = new GameService(memGameDataAccess, memAuthDataAccess, memUserDataAccess);


        UserData userData = new UserData("username", "password", "email@email.com");
        UserData userData1 = new UserData("username1", "password1", "email1@email.com");

        Assertions.assertDoesNotThrow(() -> {
            memUserDataAccess.createUser(userData);
            AuthData authData = memAuthDataAccess.create(userData.username());
            CreateGameRequest createGameRequest = new CreateGameRequest("myGame", authData.authToken());
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);

            Assertions.assertThrows(UnauthorizedException.class, () -> {

                ListGamesRequest listGamesRequest = new ListGamesRequest(memAuthDataAccess.generateToken());
                gameService.listGames(listGamesRequest);
            });
        });
    }
}
