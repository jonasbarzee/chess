package server;

import dataaccess.SQLAuthDataAccess;
import dataaccess.SQLGameDataAccess;
import dataaccess.SQLUserDataAccess;
import handler.*;
import io.javalin.*;
import io.javalin.json.JavalinGson;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(new JavalinGson());
        });

        // Register your endpoints and exception handlers here.
        SQLUserDataAccess userDataAccess = new SQLUserDataAccess();
        SQLAuthDataAccess authDataAccess = new SQLAuthDataAccess();
        SQLGameDataAccess gameDataAccess = new SQLGameDataAccess();
        UserService userService = new UserService(userDataAccess, authDataAccess);
        GameService gameService = new GameService(gameDataAccess, authDataAccess);
        ClearService clearService = new ClearService(userDataAccess, gameDataAccess, authDataAccess);
        ErrorHandler errorHandler = new ErrorHandler();

        javalin.post("/user", new RegisterHandler(userService, errorHandler));

        javalin.post("/session", new LoginHandler(userService, errorHandler));
        javalin.delete("/session", new LogoutHandler(userService, errorHandler));

        javalin.delete("/db", new ClearHandler(clearService, errorHandler));

        javalin.get("/game", new ListGamesHandler(gameService, errorHandler));
        javalin.post("/game", new CreateGameHandler(gameService, errorHandler));
        javalin.put("/game", new JoinGameHandler(gameService, errorHandler));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
