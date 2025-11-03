package dataaccess;

import chess.ChessGame;
import chess.ChessGameAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDataAccess extends SQLDataAccess implements GameDataAccess {
    Gson gson;

    public SQLGameDataAccess() {
        gson = new GsonBuilder().registerTypeAdapter(ChessGame.class, new ChessGameAdapter()).create();
    }

    public Integer createGameData(GameData gameData) {
        // don't user gameData's gameID when inserting in to the database because the schema auto-increments
        String whiteUser = gameData.whiteUsername();
        String blackUser = gameData.blackUsername();
        String gameName = gameData.gameName();
        ChessGame game = gameData.game();

        System.out.println(whiteUser);
        System.out.println(blackUser);
        System.out.println(gameName);
        System.out.println(game);

        String gameJson = gson.toJson(game);
        String statement = "INSERT INTO games (white_username, black_username, game_name, chess_game) VALUES (?, ?, ?, ?);";
        try {
            return executeUpdate(statement, whiteUser, blackUser, gameName, gameJson);
        } catch (SQLDataAccessException e) {
            throw new RuntimeException("Unable to insert into games table" + e.getMessage());
        }
    }

    public void deleteAllGameData() {
        String statement = "TRUNCATE TABLE games;";
        try {
            executeUpdate(statement);
        } catch (SQLDataAccessException e) {
            throw new RuntimeException("Unable to delete data in games table");
        }
    }

    public GameData getGame(Integer gameID) throws GameDataAccessException {
        System.out.println(gameID);
        String statement = "SELECT * FROM games WHERE game_id = ?;";
        try {
            return queryForObject(statement, rs -> {
                ChessGame game = gson.fromJson(rs.getString("chess_game"), ChessGame.class);
                return new GameData(rs.getInt("game_id"), rs.getString("white_username"),
                        rs.getString("black_username"), rs.getString("game_name"), game);
            }, gameID);
        } catch (SQLException e) {
            throw new GameDataAccessException(String.format("Unable to get game from games table, %s", e.getMessage()));
        }
    }

    public void updateGameData(GameData gameData) {
        String statement = """
                
                UPDATE games
                SET white_username = ?, black_username = ?, game_name = ?, chess_game = ?
                WHERE game_id = ?;
                """;
        String gameJson = gson.toJson(gameData.game());
        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();
        String gameName = gameData.gameName();
        Integer gameID = gameData.gameID();

        System.out.println(white);
        System.out.println(black);
        System.out.println(gameName);
        System.out.println(gameID);

        try {
            int rowsAffected = executeUpdate(statement, white, black, gameName, gameJson, gameID);

            if (rowsAffected == 0) {
                System.out.println("Game not found or updated");
            }
        } catch (SQLDataAccessException e) {
            throw new RuntimeException("Unable to update data in games table");
        }
    }

    public Collection<GameData> getGames() {
        String statement = "SELECT * FROM games;";
        Collection<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                ChessGame game = gson.fromJson(rs.getString("chess_game"), ChessGame.class);
                GameData gameData = new GameData(
                        rs.getInt("game_id"),
                        rs.getString("white_username"),
                        rs.getString("black_username"),
                        rs.getString("game_name"),
                        game
                );
                games.add(gameData);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown");
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
        }
        return games;
    }

}



