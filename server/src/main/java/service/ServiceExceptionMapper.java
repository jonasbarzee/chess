package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseUnavailableException;
import dataaccess.DuplicateKeyException;

public class ServiceExceptionMapper {
    public static ChessServerException map(DataAccessException e) {
        if (e instanceof DuplicateKeyException) {
            return new AlreadyTakenException("Username is already taken.");
        } else if (e instanceof DatabaseUnavailableException) {
            return new InternalServerException("Internal Server Error: Database connection failed");
        } else {
            return new InternalServerException("Internal Server Error: Database failure");

        }
    }
}

