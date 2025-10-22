package handler;

import chess.request.CreateGameRequest;
import chess.result.CreateGameResult;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.GameService;

import java.util.Map;

public class CreateGameHandler implements Handler {

    private final GameService gameService;
    private final ErrorHandler errorHandler;

    public CreateGameHandler(GameService gameService, ErrorHandler errorHandler) {
        this.gameService = gameService;
        this.errorHandler = errorHandler;
    }

    @Override
    public void handle(Context context) throws Exception {
        try {
            System.out.println("Raw request body: " + context.body());
            String authToken = context.header("authorization");
            System.out.println(authToken);
            Map<String, String> body = context.bodyAsClass(Map.class);
            System.out.println(body);
            String gameName = body.get("gameName");
            System.out.println(gameName);
            CreateGameRequest createGameRequest = new CreateGameRequest(gameName, authToken);
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            context.status(200).json(createGameResult);
        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}

