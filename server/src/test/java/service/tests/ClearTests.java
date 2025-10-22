package service.tests;

import chess.ChessGame;
import chess.request.ClearDatabaseRequest;
import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.ClearService;

public class ClearTests {

    private ClearService clearService;
    private UserDataAccess userDataAccess;
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;

    @Test
    public void clearDatabaseEmpty() {
        userDataAccess = new UserDataAccess();
        gameDataAccess = new GameDataAccess();
        authDataAccess = new AuthDataAccess();
        clearService = new ClearService(userDataAccess, gameDataAccess, authDataAccess);

        clearService.clearDatabase();

        Assertions.assertThrows(UserDataAccessException.class, () -> {
            UserData userData = new UserData("username", "password", "email@email.com");
            userDataAccess.updateUser(userData);
        });

        Assertions.assertThrows(GameDataAccessException.class, () -> {
            gameDataAccess.getGame(0);
            gameDataAccess.getGame(1);
            gameDataAccess.getGame(10);
        });

        Assertions.assertThrows(AuthDataAccessException.class, () -> {
            authDataAccess.get("username");
            authDataAccess.get("default");
            authDataAccess.get("");
        });
    }

    @Test
    public void clearDatabaseFilled() {
        userDataAccess = new UserDataAccess();
        gameDataAccess = new GameDataAccess();
        authDataAccess = new AuthDataAccess();
        clearService = new ClearService(userDataAccess, gameDataAccess, authDataAccess);

        UserData userData = new UserData("username", "password", "email@email.com");
        UserData userData1 = new UserData("default", "password", "email@email.com");
        UserData userData2 = new UserData("", "password", "email@email.com");

        GameData gameData = new GameData(0,"", "", "", new ChessGame());
        GameData gameData1 = new GameData(1, "", "", "", new ChessGame());
        GameData gameData2 = new GameData(10, "", "", "", new ChessGame());

        Assertions.assertDoesNotThrow(() -> {
            userDataAccess.createUser(userData);
            userDataAccess.createUser(userData1);
            userDataAccess.createUser(userData2);

            gameDataAccess.createGameData(gameData);
            gameDataAccess.createGameData(gameData1);
            gameDataAccess.createGameData(gameData2);

            authDataAccess.create(userData.username());
            authDataAccess.create(userData1.username());
            authDataAccess.create(userData2.username());
        });


        clearService.clearDatabase();

        Assertions.assertThrows(UserDataAccessException.class, () -> {
            userDataAccess.updateUser(userData);
            userDataAccess.updateUser(userData1);
            userDataAccess.updateUser(userData2);
        });

        Assertions.assertThrows(GameDataAccessException.class, () -> {
            gameDataAccess.getGame(gameData.gameID());
            gameDataAccess.getGame(gameData1.gameID());
            gameDataAccess.getGame(gameData2.gameID());
        });

        Assertions.assertThrows(AuthDataAccessException.class, () -> {
            authDataAccess.get(userData.username());
            authDataAccess.get(userData1.username());
            authDataAccess.get(userData2.username());
        });
    }
}
