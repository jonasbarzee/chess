package handler;

import chess.request.JoinGameRequestBody;
import chess.request.JoinGameRequest;
import chess.result.JoinGameResult;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.GameService;

public class JoinGameHandler implements Handler {

    private final GameService gameService;
    private final ErrorHandler errorHandler;

    public JoinGameHandler(GameService gameService, ErrorHandler errorHandler) {
        this.gameService = gameService;
        this.errorHandler = errorHandler;
    }

    @Override
    public void handle(Context context) throws Exception{

        try {
            String authToken = context.header("authorization");
            JoinGameRequestBody joinGameRequestBody = context.bodyAsClass(JoinGameRequestBody.class);
            JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, joinGameRequestBody.playerColor(), joinGameRequestBody.gameID());
            JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
            context.json(joinGameResult);
            context.status(200);

        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}

