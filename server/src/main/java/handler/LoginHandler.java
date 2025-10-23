package handler;

import chess.request.LoginRequest;
import chess.result.LoginResult;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.UserService;

public class LoginHandler implements Handler {

    private final UserService userService;
    private final ErrorHandler errorHandler;

    public LoginHandler(UserService userService, ErrorHandler errorHandler) {
        this.userService = userService;
        this.errorHandler = errorHandler;
    }
    @Override
    public void handle(Context context) throws Exception {


        try {
            LoginRequest loginRequest = context.bodyAsClass(LoginRequest.class);
            LoginResult loginResult = userService.login(loginRequest);
            context.json(loginResult);
            context.status(200);
        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}
