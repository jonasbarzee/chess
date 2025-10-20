package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GamaDataAccess {
    private final Map<Integer, GameData> gameTable = new HashMap<>();

    public void createGameData(GameData gameData) {
        gameTable.put(gameData.gameID(), gameData);
    }

    public void updateGameData(GameData gameData) {
        createGameData(gameData);
    }

    public GameData getGame(int gameID) throws GameDataAccessException {
        if (hasGame(gameID)) {
            throw new GameDataAccessException("No game with given gameID.");
        }
        return gameTable.get(gameID);
    }

    public Collection<GameData> getGames() {
        return gameTable.values();
    }

    public void deleteGameData(int gameID) {
        gameTable.remove(gameID);
    }

    public void deleteAllGameData() {
        gameTable.clear();
    }

    public boolean hasGame(int gameID) {
        return gameTable.containsKey(gameID);
    }
}
