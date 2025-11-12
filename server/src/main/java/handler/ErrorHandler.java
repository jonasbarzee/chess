package handler;

import io.javalin.http.Context;
import service.*;

import java.util.Map;

public class ErrorHandler {
    public void handleError(Context context, Exception e) {
        int status;
        String error;
        String message = "Error: " + e.getMessage();
        if (e instanceof BadRequestException) {
            status = 400;
            error = "BadRequestError";
        } else if (e instanceof NoUserException) {
            status = 401;
            error = "NoUserError";
        } else if (e instanceof WrongPasswordException) {
            status = 401;
            error = "WrongPasswordError";
        } else if (e instanceof UnauthorizedException) {
            status = 401;
            error = "UnauthorizedError";
        } else if (e instanceof AlreadyTakenException) {
            status = 403;
            error = "AlreadyTakenError";
        } else {
            status = 500;
            error = "ServerError";
        }
        context.status(status).json(Map.of(
                "message", message,
                "status", error
        ));
    }
}
