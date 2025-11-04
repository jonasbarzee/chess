package service;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DataNotFoundException;
import dataaccess.exceptions.DatabaseUnavailableException;
import dataaccess.exceptions.DuplicateKeyException;

public class ServiceExceptionMapper {
    public static ChessServerException map(DataAccessException e) {
        if (e instanceof DuplicateKeyException) {
            return new AlreadyTakenException("Username is already taken.");
        } else if (e instanceof DatabaseUnavailableException) {
            return new InternalServerException("Internal Server Error: Database connection failed");
        } else if (e instanceof DataNotFoundException) {
            return new UnauthorizedException("Unauthorized");

        } else {
            return new InternalServerException("Internal Server Error: Database failure");

        }
    }
}

