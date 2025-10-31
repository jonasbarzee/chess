package chess;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessGameAdapter implements JsonSerializer<ChessGame>, JsonDeserializer<ChessGame> {

    @Override
    public JsonElement serialize(ChessGame game, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("turn", game.getTeamTurn().toString());
        json.add("board", context.serialize(game.getBoard()));
        return json;
    }

    @Override
    public ChessGame deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        ChessGame.TeamColor turn = ChessGame.TeamColor.valueOf(object.get("turn").getAsString());
        ChessBoard board = context.deserialize(object.get("board"), ChessBoard.class);

        ChessGame game = new ChessGame();
        game.setTeamTurn(turn);
        game.setBoard(board);
        return game;
    }
}


