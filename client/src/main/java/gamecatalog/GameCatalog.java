package gamecatalog;

import chess.result.ListGamesResult;
import chess.result.ListGamesResultBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameCatalog {
    private Map<Integer, ListGamesResultBuilder> clientIdToGame = new HashMap<>();
    private Map<Integer, Integer> clientIdToServerId = new HashMap<>();
    private Map<Integer, Integer> serverIdToClientId = new HashMap<>();

    public void updateGameCatalog(ListGamesResult newGames) {
        clientIdToGame.clear();
        clientIdToServerId.clear();
        serverIdToClientId.clear();
        int clientId = 1; // client ID
        for (ListGamesResultBuilder game : newGames.games()) {
            clientIdToGame.put(clientId, game);
            clientIdToServerId.put(clientId, game.gameID());
            serverIdToClientId.put(game.gameID(), clientId);
            clientId++;
        }
    }

    public Integer getServerIdFromClientId(Integer clientId) {
        return clientIdToServerId.get(clientId);
    }

    public Integer getClientIdFromServerId(Integer serverId) {
        return serverIdToClientId.get(serverId);
    }

    public ListGamesResultBuilder getGame(Integer clientId) {
        return clientIdToGame.get(clientId);
    }

    public Collection<ListGamesResultBuilder> getGames() {
        return clientIdToGame.values();
    }
}
