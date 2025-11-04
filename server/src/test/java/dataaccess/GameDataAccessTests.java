package dataaccess;

import chess.ChessGame;
import dataaccess.sqldao.DatabaseManager;
import dataaccess.sqldao.SQLDataAccess;
import dataaccess.sqldao.SQLGameDataAccess;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

public class GameDataAccessTests {

    private static SQLGameDataAccess sqlGameDataAccess;

    @BeforeAll
    public static void init() throws Exception {
        SQLDataAccess.configureDatabase();
    }

    @BeforeEach
    public void setup() {
        sqlGameDataAccess = new SQLGameDataAccess();
    }

    @AfterEach
    public void reset() throws Exception {
        DatabaseManager.clearDatabase();
    }

    @Test
    public void createGameSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            ChessGame chessGame = new ChessGame();
            GameData gameData1 = new GameData(1, null, null, "gameName", chessGame);
            sqlGameDataAccess.createGameData(gameData1);
        });
    }

    @Test
    public void createGameFailure() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            sqlGameDataAccess.createGameData(null);
        });
    }

    @Test
    public void deleteAllGameDataSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            ChessGame chessGame = new ChessGame();
            GameData gameData1 = new GameData(1, null, null, "gameName", chessGame);
            GameData gameData2 = new GameData(2, null, null, "gameName", chessGame);
            GameData gameData3 = new GameData(3, null, null, "gameName", chessGame);
            sqlGameDataAccess.createGameData(gameData1);
            sqlGameDataAccess.createGameData(gameData2);
            sqlGameDataAccess.createGameData(gameData3);

            sqlGameDataAccess.deleteAllGameData();
        });
    }

    @Test
    public void deleteAllGameDataNegative() {
        Assertions.assertDoesNotThrow(() -> {
            sqlGameDataAccess.deleteAllGameData();
        });
    }

    @Test
    public void getGameSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            ChessGame chessGame = new ChessGame();
            GameData gameData1 = new GameData(1, null, null, "gameName", chessGame);
            sqlGameDataAccess.createGameData(gameData1);
            sqlGameDataAccess.getGame(1);
        });
    }

    @Test
    public void getGameNegative() {
        Assertions.assertDoesNotThrow(() -> {
            sqlGameDataAccess.getGame(1);
        });
    }

    @Test
    public void updateGameDataSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            ChessGame chessGame = new ChessGame();
            GameData gameData1 = new GameData(1, null, null, "gameName", chessGame);
            GameData gameData2 = new GameData(1, "me", null, "gameName", chessGame);
            sqlGameDataAccess.createGameData(gameData1);
            sqlGameDataAccess.updateGameData(gameData2);
        });
    }

    @Test
    public void updateGameDataFailure() {
        Assertions.assertThrows(NullPointerException.class, () -> {
          sqlGameDataAccess.updateGameData(null);
        });
    }

    @Test
    public void getGamesSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            ChessGame chessGame = new ChessGame();
            GameData gameData1 = new GameData(1, null, null, "gameName", chessGame);
            GameData gameData2 = new GameData(2, null, null, "gameName", chessGame);
            GameData gameData3 = new GameData(3, null, null, "gameName", chessGame);
            sqlGameDataAccess.createGameData(gameData1);
            sqlGameDataAccess.createGameData(gameData2);
            sqlGameDataAccess.createGameData(gameData3);

            Collection<GameData> games = sqlGameDataAccess.getGames();
            Assertions.assertNotNull(games);
        });
    }

    @Test
    public void getGamesNegative() {
        Assertions.assertDoesNotThrow(() -> {
            Collection<GameData> games = sqlGameDataAccess.getGames();
            Assertions.assertEquals(games, List.of());
        });
    }
}
