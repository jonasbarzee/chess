package dataaccess.memdao;

import dataaccess.GameDataAccess;
import dataaccess.exceptions.GameDataAccessException;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemGameDataAccess implements GameDataAccess {
    private final Map<Integer, GameData> gameTable = new HashMap<>();

    public Integer createGameData(GameData gameData) {
        gameTable.put(gameData.gameID(), gameData);
        return gameData.gameID();
    }

    public void updateGameData(GameData gameData) {
        createGameData(gameData);
    }

    public GameData getGame(Integer gameID) throws GameDataAccessException {
        if (!hasGame(gameID)) {
            throw new GameDataAccessException("No game with given gameID.");
        }
        return gameTable.get(gameID);
    }

    public Collection<GameData> getGames() {
        return gameTable.values();
    }

    public void deleteAllGameData() {
        gameTable.clear();
    }

    public boolean hasGame(Integer gameID) {
        return gameTable.containsKey(gameID);
    }
}
