package websocket;

import dataaccess.exceptions.DataAccessException;
import dataaccess.interfaces.AuthDataAccess;
import dataaccess.interfaces.GameDataAccess;
import dataaccess.model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import service.BadRequestException;
import service.ChessServerException;
import service.InternalServerException;
import service.UnauthorizedException;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
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

    public List<ServerMessage> handleJoin(Session session, String token, int gameId)  throws ChessServerException {
        try {
            if (token == null || !authDataAccess.isAuthorized(token)) {
                throw new UnauthorizedException("Unauthorized");
            }

            GameData gameData = gameDataAccess.getGame(gameId);
            if (gameData == null) {
                throw new BadRequestException("Bad Request, game DNE");
            }

            websocketConnectionManager.add(gameId, session);

            ServerMessage loadGameMessage = new LoadGameMessage(gameData.game());
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            System.out.println(loadGameMessage);
            System.out.println(notificationMessage);
            return List.of(loadGameMessage, notificationMessage);
        } catch (DataAccessException ex) {
            throw new InternalServerException("Internal Server Error");
        }

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
