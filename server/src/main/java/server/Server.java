package server;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import handler.ClearHandler;
import handler.ErrorHandler;
import handler.RegisterHandler;
import io.javalin.*;
import service.ClearService;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        UserDataAccess userDataAccess = new UserDataAccess();
        AuthDataAccess authDataAccess = new AuthDataAccess();
        GameDataAccess gameDataAccess = new GameDataAccess();
        UserService userService = new UserService(userDataAccess, authDataAccess);
        ClearService clearService = new ClearService(userDataAccess, gameDataAccess, authDataAccess);
        ErrorHandler errorHandler = new ErrorHandler();

        javalin.post("/user", new RegisterHandler(userService, errorHandler));
        javalin.delete("/db", new ClearHandler(clearService));

        javalin.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500).json(Map.of("message", "Error: " + e.getMessage()));
        });


    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
