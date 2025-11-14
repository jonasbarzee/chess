package client;

import chess.request.*;
import chess.result.CreateGameResult;
import chess.result.LoginResult;
import chess.result.RegisterResult;
import dataaccess.sqldao.DatabaseManager;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println(port);
        serverFacade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    @AfterEach
    public void reset() throws Exception {
        DatabaseManager.clearDatabase();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test@test.com");
        Assertions.assertDoesNotThrow(() -> {
            RegisterResult registerResult = serverFacade.register(registerRequest);
            Assertions.assertNotNull(registerResult.authToken());
            Assertions.assertNotNull(registerResult.username());
        });
    }

    @Test
    public void registerFailure() {
        RegisterRequest registerRequest = new RegisterRequest(null, null, null);
        Assertions.assertThrows(ResponseException.class, () -> {
            serverFacade.register(registerRequest);
        });
    }

    @Test
    public void loginSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test@test.com");
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.register(registerRequest);
            LoginResult loginResult = serverFacade.login(loginRequest);
            Assertions.assertNotNull(loginResult.authToken());
            Assertions.assertNotNull(loginResult.username());
        });
    }

    @Test
    public void loginFailure() {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test@test.com");
        LoginRequest loginRequest = new LoginRequest("badUsername", "badPassword");
        Assertions.assertThrows(ResponseException.class, () -> {
            serverFacade.register(registerRequest);
            serverFacade.login(loginRequest);
        });
    }

    @Test
    public void createGameSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test@test.com");
        Assertions.assertDoesNotThrow(() -> {
            RegisterResult registerResult = serverFacade.register(registerRequest);
            CreateGameRequest createGameRequest = new CreateGameRequest("game1", registerResult.authToken());
            CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
            Assertions.assertNotNull(createGameResult.gameID());
        });
    }

    @Test
    public void createGameFailure() {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test@test.com");
        Assertions.assertThrows(ResponseException.class, () -> {
            serverFacade.register(registerRequest);
            CreateGameRequest createGameRequest = new CreateGameRequest("game1", "badToken");
            serverFacade.createGame(createGameRequest);
        });
    }

    @Test
    public void logoutSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test@test.com");
        Assertions.assertDoesNotThrow(() -> {
            RegisterResult registerResult = serverFacade.register(registerRequest);
            serverFacade.logout(new LogoutRequest(registerResult.authToken()));
        });
    }

    @Test
    public void logoutFailure() {
        Assertions.assertThrows(ResponseException.class, () -> {
            serverFacade.logout(new LogoutRequest("badToken"));
        });
    }

    @Test
    public void listGamesSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test@test.com");
        Assertions.assertDoesNotThrow(() -> {
            RegisterResult registerResult = serverFacade.register(registerRequest);
            CreateGameRequest createGameRequest1 = new CreateGameRequest("game1", registerResult.authToken());
            CreateGameRequest createGameRequest2 = new CreateGameRequest("game2", registerResult.authToken());
            CreateGameRequest createGameRequest3 = new CreateGameRequest("game3", registerResult.authToken());
            CreateGameResult createGameResult1 = serverFacade.createGame(createGameRequest1);
            CreateGameResult createGameResult2 = serverFacade.createGame(createGameRequest2);
            CreateGameResult createGameResult3 = serverFacade.createGame(createGameRequest3);
            Assertions.assertNotNull(createGameResult1);
            Assertions.assertNotNull(createGameResult2);
            Assertions.assertNotNull(createGameResult3);
        });
    }

    @Test
    public void listGamesFailure() {
        Assertions.assertThrows(ResponseException.class, () -> {
            CreateGameRequest createGameRequest1 = new CreateGameRequest("game1", "badToken");
            CreateGameResult createGameResult1 = serverFacade.createGame(createGameRequest1);
            Assertions.assertNull(createGameResult1);
        });
    }

    @Test
    public void joinGameSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            RegisterRequest registerRequest = new RegisterRequest("test", "test", "test@test.com");
            RegisterResult registerResult = serverFacade.register(registerRequest);
            CreateGameRequest createGameRequest = new CreateGameRequest("myGame", registerResult.authToken());
            CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest(registerResult.authToken(), "white", createGameResult.gameID());
            serverFacade.joinGame(joinGameRequest);
        });
    }

    @Test
    public void joinGameFailure() {
        Assertions.assertThrows(ResponseException.class, () -> {
            JoinGameRequest joinGameRequest = new JoinGameRequest("badToken", "white", 1);
            serverFacade.joinGame(joinGameRequest);
        });
    }
}
