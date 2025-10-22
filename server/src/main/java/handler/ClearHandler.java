package handler;

import chess.request.ClearDatabaseRequest;
import chess.result.ClearDatabaseResult;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearService;

import java.util.Map;

public class ClearHandler implements Handler {

    private final ClearService clearService;
    private final ErrorHandler errorHandler;

    public ClearHandler(ClearService clearService, ErrorHandler errorHandler) {
        this.clearService = clearService;
        this.errorHandler = errorHandler;
    }

    @Override
    public void handle(Context context) throws Exception {
        try {
            System.out.println("Raw request body: " + context.body());
            clearService.clearDatabase();
            context.json(Map.of());
            context.status(200);
        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}
