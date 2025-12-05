package chess;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessGameAdapter implements JsonSerializer<ChessGame>, JsonDeserializer<ChessGame> {

    @Override
    public JsonElement serialize(ChessGame game, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("turn", game.getTeamTurn().toString());
        json.add("board", context.serialize(game.getBoard()));
        json.add("isGameOver", context.serialize(game.getIsGameOver()));
        json.add("whiteResigned", context.serialize(game.isWhiteResigned()));
        json.add("blackResigned", context.serialize(game.isBlackResigned()));
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

        if (object.has("isGameOver")) {
            game.setGameOver(object.get("isGameOver").getAsBoolean());
        }
        if (object.has("whiteResigned")) {
            game.setWhiteResigned(object.get("whiteResigned").getAsBoolean());
        }
        if (object.has("blackResigned")) {
            game.setBlackResigned(object.get("blackResigned").getAsBoolean());
        }
        return game;
    }
}


