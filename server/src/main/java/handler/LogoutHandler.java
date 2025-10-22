package handler;

import chess.request.LogoutRequest;
import chess.request.RegisterRequest;
import chess.result.LogoutResult;
import chess.result.RegisterResult;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.UserService;

public class LogoutHandler implements Handler {


    private final UserService userService;
    private final ErrorHandler errorHandler;

    public LogoutHandler(UserService userService, ErrorHandler errorHandler) {
        this.userService = userService;
        this.errorHandler = errorHandler;
    }

    @Override
    public void handle(Context context) throws Exception {
        LogoutRequest logoutRequest = context.bodyAsClass(LogoutRequest.class);
        LogoutResult logoutResult;
        try {
            logoutResult = userService.logout(logoutRequest);
            context.json(logoutResult);
            context.status(200);

        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}
