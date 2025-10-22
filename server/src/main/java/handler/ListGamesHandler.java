package handler;

import chess.request.ListGamesRequest;
import chess.result.ListGamesResult;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.GameService;

public class ListGamesHandler implements Handler {

    private final GameService gameService;
    private final ErrorHandler errorHandler;

    public ListGamesHandler(GameService gameService, ErrorHandler errorHandler) {
        this.gameService = gameService;
        this.errorHandler = errorHandler;
    }

    @Override
    public void handle(Context context) throws Exception {
        try {
            String authToken = context.header("authorization");
            System.out.println("Raw request body: " + context.body());
            System.out.println(authToken);
            ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
            ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
            context.json(listGamesResult);
            context.status(200);
        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}

