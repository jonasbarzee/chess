package handler;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.*;

import java.util.Map;

public class ErrorHandler {
    public void handleError(Context context, Exception e) {
        System.out.println("In handleError!!");
        if (e instanceof BadRequestException) {
            context.status(400).json(Map.of("message", "Error: Unauthorized"));
        } else if (e instanceof NoUserException) {
            context.status(401).json(Map.of("message", "Error: Unauthorized"));
        } else if (e instanceof WrongPasswordException) {
            context.status(401).json(Map.of("message", "Error: Unauthorized"));
        } else if (e instanceof UnauthorizedException) {
            context.status(401).json(Map.of("message", "Error: Unauthorized"));
        } else if (e instanceof AlreadyTakenException) {
            context.status(403).json(Map.of("message", "Error" + e.getMessage()));
        } else {
            context.status(500).json(Map.of("message", "Error!!: " + e.getMessage()));
        }
    }
}
