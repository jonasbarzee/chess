package handler;

import chess.request.ClearDatabaseRequest;
import chess.result.ClearDatabaseResult;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearService;
import service.GameService;
import service.UserService;

public class ClearHandler implements Handler {

    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void handle(Context context) {
        ClearDatabaseRequest clearDatabaseRequest = context.bodyAsClass(ClearDatabaseRequest.class);
        ClearDatabaseResult clearDatabaseResult = clearService.clearDatabase(clearDatabaseRequest);

        context.json(clearDatabaseResult);
        context.status(200);
    }
}
