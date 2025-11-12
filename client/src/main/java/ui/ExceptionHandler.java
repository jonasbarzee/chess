package ui;

import exception.ResponseException;

public class ExceptionHandler {

    private ExceptionHandler() {
    }

    public static String handle(Exception e) {
        if (e instanceof ResponseException re) {
            return switch (re.code()) {
                case ClientError -> "Client error: " + re.getMessage();
                case ServerError -> "Server error. Please try again later.";
                case UnauthorizedError -> "Unauthorized.";
                case NoUserError -> "Unauthorized.";
                case WrongPasswordError -> "Unauthorized.";
                case AlreadyTakenError -> "Username already taken.";
                case BadRequestError -> "Bad Request.";

            };
        }

        return "Unexpected error: " + e.getMessage();
    }

}
