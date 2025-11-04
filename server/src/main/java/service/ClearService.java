package service;

import chess.result.ClearDatabaseResult;
import dataaccess.interfaces.AuthDataAccess;
import dataaccess.exceptions.DataAccessException;
import dataaccess.interfaces.GameDataAccess;
import dataaccess.interfaces.UserDataAccess;

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
