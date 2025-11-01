package service;

import chess.result.ClearDatabaseResult;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.exceptions.SQLDataAccessException;

public class ClearService {
    private final UserDataAccess userDataAccess;
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public ClearService(UserDataAccess userDataAccess, GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public ClearDatabaseResult clearDatabase() {
        try {

            userDataAccess.deleteAllUsers();
        } catch (SQLDataAccessException e) {
            throw new RuntimeException(e);
        }
        gameDataAccess.deleteAllGameData();
        authDataAccess.deleteAllAuthData();
        return new ClearDatabaseResult();
    }
}
