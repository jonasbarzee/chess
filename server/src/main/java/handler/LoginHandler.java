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
        System.out.println("Login handler invoked!!");
    }
    @Override
    public void handle(Context context) throws Exception {

        System.out.println("Raw request body: " + context.body());

        try {
            LoginRequest loginRequest = context.bodyAsClass(LoginRequest.class);
            System.out.println("Parsed login request" + loginRequest);
            LoginResult loginResult;
            loginResult = userService.login(loginRequest);

            System.out.println("login result" + loginResult);
            System.out.println("auth token" + loginResult.authToken());
            context.json(loginResult);
            context.status(200);
        } catch (Exception ex) {
            errorHandler.handleError(context, ex);
        }
    }
}
