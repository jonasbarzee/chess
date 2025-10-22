package server;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import handler.*;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        System.out.println("In the server method of the server class!!");
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.before(ctx -> {
            System.out.println("Incoming " + ctx.method() + " " + ctx.path());
        });

        javalin.after(ctx -> {
            if (ctx.status().getCode() == 404) {
                System.out.println("No handler matched " + ctx.method() + " " + ctx.path());
            }
        });

        javalin.after(ctx -> {
            if (ctx.status().getCode() == 200) {
                System.out.println("handler matched " + ctx.method() + " " + ctx.path());
            }
        });

        // Register your endpoints and exception handlers here.
        UserDataAccess userDataAccess = new UserDataAccess();
        AuthDataAccess authDataAccess = new AuthDataAccess();
        GameDataAccess gameDataAccess = new GameDataAccess();
        UserService userService = new UserService(userDataAccess, authDataAccess);
        GameService gameService = new GameService(gameDataAccess, authDataAccess, userDataAccess);
        ClearService clearService = new ClearService(userDataAccess, gameDataAccess, authDataAccess);
        ErrorHandler errorHandler = new ErrorHandler();

        javalin.post("/user", new RegisterHandler(userService, errorHandler));

        javalin.post("/session", new LoginHandler(userService, errorHandler));
        javalin.delete("/session", new LogoutHandler(userService, errorHandler));

        javalin.delete("/db", new ClearHandler(clearService, errorHandler));

        javalin.get("/game", new ListGamesHandler(gameService, errorHandler));
        javalin.post("/game", new CreateGameHandler(gameService, errorHandler));
        javalin.put("/game", new JoinGameHandler(gameService, errorHandler));



        javalin.exception(Exception.class, (e, ctx) -> {
            System.out.println("In server exception handler");
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
