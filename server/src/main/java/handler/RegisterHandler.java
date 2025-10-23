package handler;

import chess.request.RegisterRequest;
import chess.result.RegisterResult;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.UserService;

public class RegisterHandler implements Handler {

    private final UserService userService;
    private final ErrorHandler errorHandler;

    public RegisterHandler(UserService userService, ErrorHandler errorHandler) {
        this.userService = userService;
        this.errorHandler = errorHandler;
    }
    @Override
    public void handle(Context context) throws Exception {
        try {
            RegisterRequest registerRequest = context.bodyAsClass(RegisterRequest.class);
            RegisterResult registerResult;
            registerResult = userService.register(registerRequest);
            context.json(registerResult);
            context.status(200);

        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}

