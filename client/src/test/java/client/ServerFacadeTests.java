package client;

import chess.request.CreateGameRequest;
import chess.request.LoginRequest;
import chess.request.LogoutRequest;
import chess.request.RegisterRequest;
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

}
