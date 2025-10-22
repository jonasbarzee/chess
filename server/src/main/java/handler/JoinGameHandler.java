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
            System.out.println("Raw request body: " + context.body());
            String authToken = context.header("authorization");
            System.out.println(authToken);

            JoinGameRequestBody joinGameRequestBody = context.bodyAsClass(JoinGameRequestBody.class);
            System.out.println(joinGameRequestBody.gameID());
            System.out.println(joinGameRequestBody.playerColor());


            JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, joinGameRequestBody.playerColor(), joinGameRequestBody.gameID());
            JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
            context.json(joinGameResult);
            context.status(200);

        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}

