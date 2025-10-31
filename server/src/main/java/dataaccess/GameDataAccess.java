package dataaccess;

import dataaccess.exceptions.GameDataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    public void deleteAllGameData();
    public void createGameData(GameData gameData);
    public GameData getGame(Integer gameID) throws GameDataAccessException;
    public void updateGameData(GameData gameData);
    public Collection<GameData> getGames();
}
