package service;

import chess.request.ClearDatabaseRequest;
import chess.result.ClearDatabaseResult;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;

public class ClearService {
    private final UserDataAccess userDataAccess;
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public ClearService(UserDataAccess userDataAccess, GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public ClearDatabaseResult clearDatabase(ClearDatabaseRequest clearDatabaseRequest) {
        userDataAccess.deleteAllUsers();
        gameDataAccess.deleteAllGameData();
        authDataAccess.deleteAllAuthData();
        return new ClearDatabaseResult();
    }
}
