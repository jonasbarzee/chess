package service;

public class ChessServerException extends Exception {
    private final int statusCode;

    public ChessServerException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
