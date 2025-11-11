package ui;

import exception.ResponseException;
import org.xml.sax.ErrorHandler;

public class ExceptionHandler {

    private ExceptionHandler() {
    }

    public static String handle(Exception e) {
        if (e instanceof ResponseException re) {
            return switch (re.code()) {
                case ClientError -> "Client error: " + re.getMessage();
                case ServerError -> "Server error. Please try again later.";
            };
        }

        return "Unexpected error: " + e.getMessage();
    }

}
