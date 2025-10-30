package service.tests;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.ClearService;

public class ClearTests {

    private ClearService clearService;
    private MemUserDataAccess memUserDataAccess;
    private MemGameDataAccess memGameDataAccess;
    private MemAuthDataAccess memAuthDataAccess;

    @Test
    public void clearDatabaseEmpty() {
        memUserDataAccess = new MemUserDataAccess();
        memGameDataAccess = new MemGameDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        clearService = new ClearService(memUserDataAccess, memGameDataAccess, memAuthDataAccess);

        clearService.clearDatabase();

        Assertions.assertThrows(UserDataAccessException.class, () -> {
            UserData userData = new UserData("username", "password", "email@email.com");
            memUserDataAccess.updateUser(userData);
        });

        Assertions.assertThrows(GameDataAccessException.class, () -> {
            memGameDataAccess.getGame(0);
            memGameDataAccess.getGame(1);
            memGameDataAccess.getGame(10);
        });

        Assertions.assertThrows(AuthDataAccessException.class, () -> {
            memAuthDataAccess.get("username");
            memAuthDataAccess.get("default");
            memAuthDataAccess.get("");
        });
    }

    @Test
    public void clearDatabaseFilled() {
        memUserDataAccess = new MemUserDataAccess();
        memGameDataAccess = new MemGameDataAccess();
        memAuthDataAccess = new MemAuthDataAccess();
        clearService = new ClearService(memUserDataAccess, memGameDataAccess, memAuthDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");
        UserData userData1 = new UserData("default", "password", "email@email.com");
        UserData userData2 = new UserData("", "password", "email@email.com");

        GameData gameData = new GameData(0,"", "", "", new ChessGame());
        GameData gameData1 = new GameData(1, "", "", "", new ChessGame());
        GameData gameData2 = new GameData(10, "", "", "", new ChessGame());

        Assertions.assertDoesNotThrow(() -> {
            memUserDataAccess.createUser(userData);
            memUserDataAccess.createUser(userData1);
            memUserDataAccess.createUser(userData2);

            memGameDataAccess.createGameData(gameData);
            memGameDataAccess.createGameData(gameData1);
            memGameDataAccess.createGameData(gameData2);

            memAuthDataAccess.create(userData.username());
            memAuthDataAccess.create(userData1.username());
            memAuthDataAccess.create(userData2.username());
        });


        clearService.clearDatabase();

        Assertions.assertThrows(UserDataAccessException.class, () -> {
            memUserDataAccess.updateUser(userData);
            memUserDataAccess.updateUser(userData1);
            memUserDataAccess.updateUser(userData2);
        });

        Assertions.assertThrows(GameDataAccessException.class, () -> {
            memGameDataAccess.getGame(gameData.gameID());
            memGameDataAccess.getGame(gameData1.gameID());
            memGameDataAccess.getGame(gameData2.gameID());
        });

        Assertions.assertThrows(AuthDataAccessException.class, () -> {
            memAuthDataAccess.get(userData.username());
            memAuthDataAccess.get(userData1.username());
            memAuthDataAccess.get(userData2.username());
        });
    }
}
