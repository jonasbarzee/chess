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
        System.out.println("RegisterHandler invoked!!");
    }
    @Override
    public void handle(Context context) throws Exception {

        System.out.println("Raw request body: " + context.body());

        RegisterRequest registerRequest = context.bodyAsClass(RegisterRequest.class);
        RegisterResult registerResult;
        System.out.println("RegisterHandler handle invoked!");
        try {
            registerResult = userService.register(registerRequest);

            System.out.println("Register result" + registerResult);

            context.json(registerResult);
            context.status(200);

        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}

