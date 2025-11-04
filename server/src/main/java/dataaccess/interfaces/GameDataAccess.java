package dataaccess.interfaces;

import dataaccess.exceptions.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    public void deleteAllGameData() throws DataAccessException;
    public Integer createGameData(GameData gameData) throws DataAccessException;
    public GameData getGame(Integer gameID) throws DataAccessException;
    public void updateGameData(GameData gameData) throws DataAccessException;
    public Collection<GameData> getGames();
}
