package service;

import chess.result.ClearDatabaseResult;
import dataaccess.MemAuthDataAccess;
import dataaccess.MemGameDataAccess;
import dataaccess.MemUserDataAccess;

public class ClearService {
    private final MemUserDataAccess memUserDataAccess;
    private final MemGameDataAccess memGameDataAccess;
    private final MemAuthDataAccess memAuthDataAccess;

    public ClearService(MemUserDataAccess memUserDataAccess, MemGameDataAccess memGameDataAccess, MemAuthDataAccess memAuthDataAccess) {
        this.memUserDataAccess = memUserDataAccess;
        this.memGameDataAccess = memGameDataAccess;
        this.memAuthDataAccess = memAuthDataAccess;
    }

    public ClearDatabaseResult clearDatabase() {
        memUserDataAccess.deleteAllUsers();
        memGameDataAccess.deleteAllGameData();
        memAuthDataAccess.deleteAllAuthData();
        return new ClearDatabaseResult();
    }
}
