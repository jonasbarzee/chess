package handler;

import chess.request.CreateGameRequest;
import chess.result.CreateGameResult;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.GameService;

public class CreateGameHandler implements Handler {

    private final GameService gameService;
    private final ErrorHandler errorHandler;

    public CreateGameHandler(GameService gameService, ErrorHandler errorHandler) {
        this.gameService = gameService;
        this.errorHandler = errorHandler;
    }

    @Override
    public void handle(Context context) throws Exception {
        CreateGameRequest createGameRequest = context.bodyAsClass(CreateGameRequest.class);
        CreateGameResult createGameResult;
        try {
            createGameResult = gameService.createGame(createGameRequest);
            context.status(200).json(createGameResult);
        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}

