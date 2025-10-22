package handler;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.AlreadyTakenException;
import service.NoUserException;
import service.UnauthorizedException;
import service.WrongPasswordException;

import java.util.Map;

public class ErrorHandler {
    public void handleError(Context context, Exception e) {
        System.out.println("In handleError!!");
        if (e instanceof UnauthorizedException || e instanceof NoUserException || e instanceof WrongPasswordException) {
            context.status(401).json(Map.of("message", "Error: Unauthorized"));
        } else if (e instanceof AlreadyTakenException) {
            context.status(403).json(Map.of("message", e.getMessage()));
        } else if (e instanceof DataAccessException) {
            context.status(500).json(Map.of("message", "Error: Bad Request."));
        } else {
            context.status(500).json(Map.of("message", "Error!!: " + e.getMessage()));
        }
    }
}
