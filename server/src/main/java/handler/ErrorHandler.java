package handler;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.AlreadyTakenException;
import service.UnauthorizedException;

import java.util.Map;

public class ErrorHandler {
    public void handleError(Context context, Exception e) {
        if (e instanceof UnauthorizedException) {
            context.status(401).json(Map.of("message", "Error: Unauthorized"));
        } else if (e instanceof AlreadyTakenException) {
            context.status(403).json(Map.of("message", e.getMessage()));
        } else if (e instanceof DataAccessException) {
            context.status(500).json(Map.of("message", "Error: Bad Request."));
        } else {
            context.status(500).json(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}
