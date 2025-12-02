package websocket;

import dataaccess.interfaces.AuthDataAccess;
import dataaccess.interfaces.GameDataAccess;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.List;

public class GameWebSocketService {

    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;
    private final WebsocketConnectionManager websocketConnectionManager;

    public GameWebSocketService(AuthDataAccess authDataAccess, GameDataAccess gameDataAccess, WebsocketConnectionManager websocketConnectionManager) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.websocketConnectionManager = websocketConnectionManager;
    }

    public List<ServerMessage> handleConnect(Session session, String token, int gameId) {
        return List.of();
    }

    public List<ServerMessage> handleMove(UserGameCommand userGameCommand, Session session) {
        return List.of();
    }

    public List<ServerMessage> handleResign(UserGameCommand userGameCommand, Session session) {
        return List.of();
    }
    public List<ServerMessage> handleLeave(UserGameCommand userGameCommand, Session session) {
        return List.of();
    }
}
