package service;

import chess.result.ClearDatabaseResult;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
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

    public ClearDatabaseResult clearDatabase() throws ChessServerException{

        try {
            userDataAccess.deleteAllUsers();
            authDataAccess.deleteAllAuthData();
            gameDataAccess.deleteAllGameData();
        } catch (DataAccessException e) {
            throw ServiceExceptionMapper.map(e);

        }
        return new ClearDatabaseResult();
    }
}
