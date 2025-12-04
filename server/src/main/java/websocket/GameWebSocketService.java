package websocket;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.exceptions.DataAccessException;
import dataaccess.interfaces.AuthDataAccess;
import dataaccess.interfaces.GameDataAccess;
import dataaccess.model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import service.BadRequestException;
import service.ChessServerException;
import service.InternalServerException;
import service.UnauthorizedException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Collection;
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

            String username = authDataAccess.getUsername(token);
            String color = gameData.whiteUsername().isEmpty() ? "white" : "black";
            websocketConnectionManager.add(gameId, session);

            ServerMessage loadGameMessage = new LoadGameMessage(gameData.game());
            ServerMessage notificationMessage = new NotificationMessage("Player " + username + " joined as " + color);
            System.out.println(loadGameMessage);
            System.out.println(notificationMessage);
            return List.of(loadGameMessage, notificationMessage);
        } catch (DataAccessException ex) {
            throw new InternalServerException("Internal Server Error");
        }

    }

    public List<ServerMessage> handleMove(MakeMoveCommand makeMoveCommand, Session session, int gameId) throws ChessServerException {
        try {
            System.out.println(makeMoveCommand.getMove());
            ChessMove move = makeMoveCommand.getMove();
            GameData gameData = gameDataAccess.getGame(gameId);
            ChessGame game = gameData.game();
            Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());

            if (validMoves.contains(move)) {
               game.makeMove(move);
               gameDataAccess.updateGameData(gameData);
            }

            // check write here

            ServerMessage loadGameMessage = new LoadGameMessage(gameData.game());
            ServerMessage notificationMessage = new NotificationMessage("Player made move " + move);
            return List.of(loadGameMessage, notificationMessage);
        } catch (Exception ex) {
            throw new InternalServerException("Internal Server Error");
        }

    }

    public List<ServerMessage> handleResign(UserGameCommand userGameCommand, Session session) {
        return List.of();
    }
    public List<ServerMessage> handleLeave(UserGameCommand userGameCommand, Session session) {
        return List.of();
    }
}
