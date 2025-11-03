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

    public Integer createGameData(GameData gameData) throws DataAccessException {
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
        return executeUpdate(statement, whiteUser, blackUser, gameName, gameJson);
    }

    public void deleteAllGameData() throws DataAccessException {
        String statement = "TRUNCATE TABLE games;";
        executeUpdate(statement);
    }

    public GameData getGame(Integer gameID) throws DataAccessException {
        String statement = "SELECT * FROM games WHERE game_id = ?;";
        return queryForObject(statement, rs -> {
            ChessGame game = gson.fromJson(rs.getString("chess_game"), ChessGame.class);
            return new GameData(rs.getInt("game_id"), rs.getString("white_username"),
                    rs.getString("black_username"), rs.getString("game_name"), game);
        }, gameID);
    }

    public void updateGameData(GameData gameData) throws DataAccessException {
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

        executeUpdate(statement, white, black, gameName, gameJson, gameID);
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



