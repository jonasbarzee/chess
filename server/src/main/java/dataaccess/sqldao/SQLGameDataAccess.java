package dataaccess.sqldao;

import chess.ChessGame;
import chess.ChessGameAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.GameData;

public class SQLGameDataAccess {
    Gson gson;

    public SQLGameDataAccess() {
       gson = new GsonBuilder().registerTypeAdapter(ChessGame.class, new ChessGameAdapter()).create();
    }

    public void create(GameData gameData) {
        // don't user gameData's gameID when inserting in to the database because the schema auto-increments
        String whiteUser = gameData.whiteUsername();
        String blackUser = gameData.blackUsername();
        String gameName = gameData.gameName();
        ChessGame game = gameData.game();

        String json = gson.toJson(game);




//        String statement = "INSERT INTO games ("


//        ChessGame gameFromJson = gson.fromJson(json, ChessGame.class);
//        return new GameData( )
    }

//    public GameData get(Integer gameID) {

//        return new GameData()
//    }


}
