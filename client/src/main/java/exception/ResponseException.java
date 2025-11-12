package exception;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        ClientError,
        AlreadyTakenError,
        UnauthorizedError,
        WrongPasswordError,
        NoUserError,
        BadRequestError,
    }

    private final Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    }

    public static ResponseException fromJson(String json) {
        System.out.println("In fromJson");
        System.out.println(json);

        try {
            HashMap<String, Object> map = new Gson().fromJson(json, HashMap.class);
            if (map == null || map.isEmpty()) {
                return new ResponseException(Code.ServerError, "Empty error response from server.");
            }

            Object statusObj = map.get("status");
            Object messageObj = map.get("message");

            if (statusObj == null) {
                return new ResponseException(Code.ServerError, "Missing status field in server response.");
            }

            Code status;
            try {
                status = Code.valueOf(map.get("status").toString());
                System.out.println(status);
            } catch (IllegalArgumentException e) {
                System.out.println("Caught illegal argument exception");
                status = Code.ServerError;
            }

            String message = (messageObj != null) ? messageObj.toString() : "Unknown server error";
            System.out.println("returning new response exception");
            System.out.println(status);
            System.out.println(message);
            return new ResponseException(status, message);
        } catch (Exception e) {
            return new ResponseException(Code.ServerError, "Malformed server response.");
        }
    }

    public Code code() {
        return code;
    }

    public static Code fromHttpStatusCode(int httpStatusCode) {
        return switch (httpStatusCode) {
            case 500 -> Code.ServerError;
            case 403 -> Code.AlreadyTakenError;
            case 401 -> Code.UnauthorizedError;
            case 400 -> Code.ClientError;
            default -> throw new IllegalArgumentException("Unknown HTTP status code: " + httpStatusCode);
        };
    }

    public int toHttpStatusCode() {
        return switch (code) {
            case ServerError -> 500;
            case AlreadyTakenError -> 403;
            case UnauthorizedError -> 401;
            case NoUserError -> 401;
            case WrongPasswordError -> 401;
            case BadRequestError -> 400;
            case ClientError -> 400;
        };
    }

}

