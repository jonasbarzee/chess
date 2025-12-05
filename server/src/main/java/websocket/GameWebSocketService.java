package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
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
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
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

    public List<ServerMessage> handleJoin(Session session, String token, int gameId, UserGameCommand joinCommand)  throws ChessServerException {
        System.out.println();
        try {
            if (token == null || !authDataAccess.isAuthorized(token)) {
                throw new UnauthorizedException("Unauthorized");
            }

            GameData gameData = gameDataAccess.getGame(gameId);
            if (gameData == null) {
                throw new BadRequestException("Bad Request, game DNE");
            }

            String username = authDataAccess.getUsername(token);
            String requestedColor = joinCommand.getPlayerColor();
            if (requestedColor == null) {
                requestedColor = "observer";
            }
            websocketConnectionManager.add(gameId, session);

            ServerMessage loadGameMessage = new LoadGameMessage(gameData.game());
            ServerMessage notificationMessage = new NotificationMessage("Player " + username + " joined as " + requestedColor);
            System.out.println(loadGameMessage);
            System.out.println(notificationMessage);
            return List.of(loadGameMessage, notificationMessage);
        } catch (DataAccessException ex) {
            throw new InternalServerException("Internal Server Error");
        }

    }

    public List<ServerMessage> handleMove(MakeMoveCommand makeMoveCommand, Session session, int gameId) throws ChessServerException {
        try {
            System.out.println("Move: " + makeMoveCommand.getMove());
            ChessMove move = makeMoveCommand.getMove();
            GameData gameData = gameDataAccess.getGame(gameId);
            String username = authDataAccess.getUsername(makeMoveCommand.getAuthToken());
            System.out.println(gameData.whiteUsername() + " " + username);
            ChessGame.TeamColor color = gameData.whiteUsername().equalsIgnoreCase(username.toLowerCase())? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            ChessGame game = gameData.game();
            ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
            Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());



            System.out.println(piece.getTeamColor() + " " + color);
            if (piece.getTeamColor() != color) {
                 return List.of( new ErrorMessage("Error: cannot move piece of not your color."));
            }

            if (!validMoves.contains(move)) {
                System.out.println("Can't make move, not a valid move");
                return List.of(new ErrorMessage("Error: Not a valid move."));
            }
            game.makeMove(move);

            boolean checkmate = game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK);
            boolean stalemate = game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK);
            boolean check = game.isInCheck(ChessGame.TeamColor.WHITE) || game.isInCheck(ChessGame.TeamColor.BLACK);
            boolean resigned = game.isWhiteResigned() || game.isBlackResigned();

            if (resigned) {
                System.out.println("IN RESIGNED");
                game.setGameOver(false);
                gameDataAccess.updateGameData(gameData);
                return List.of(new ErrorMessage("Cannot move after resign, game is considered over."));
            }

            if (game.getIsGameOver()) {
                System.out.println("Can't make move, game is over");
                LoadGameMessage loadGameMessage = new LoadGameMessage(game);
                ErrorMessage errorMessage = new ErrorMessage("No moves can be made, game is over.");
                return List.of(loadGameMessage, errorMessage);
            }

            if (checkmate || stalemate ) {
                game.setGameOver(true);
                gameDataAccess.updateGameData(gameData);
            }
            gameDataAccess.updateGameData(gameData);

            List<ServerMessage> messages = new ArrayList<>();
            messages.add(new LoadGameMessage(game));

            if (checkmate) {
                System.out.println("In checkmate if");
                messages.add(new NotificationMessage("Checkmate. Game Over."));
            } else if (stalemate) {
                System.out.println("In stalemate if");
                messages.add(new NotificationMessage("Stalemate. Game Over."));
            } else if (check) {
                System.out.println("In check if");
                messages.add(new NotificationMessage("Check."));
            } else {
                messages.add(new NotificationMessage("Player " + username + " made move " + makeMoveCommand.getMove().toString()));
            }
            System.out.println("Messages: " + messages);
            return messages;

        } catch (Exception ex) {
            throw new InternalServerException("Internal Server Error");
        }
    }

    public List<ServerMessage> handleResign(UserGameCommand userGameCommand, Session session) throws ChessServerException {
        try {
            int gameId = userGameCommand.getGameID();
            String authToken = userGameCommand.getAuthToken();
            String username = authDataAccess.getUsername(authToken);
            GameData gameData = gameDataAccess.getGame(gameId);
            ChessGame game = gameData.game();

            if (!username.equalsIgnoreCase(gameData.whiteUsername()) && !username.equalsIgnoreCase(gameData.blackUsername()))
                return List.of(new ErrorMessage("Error: Observer Cannot Resign"));

            if (game.isWhiteResigned() || game.isBlackResigned()) {
                return List.of(new ErrorMessage("Error Cannot resign more than once"));
            }

            if (username.equalsIgnoreCase(gameData.whiteUsername())) {
                game.setWhiteResigned(true);
            } else {
                game.setBlackResigned(true);
            }

            game.setGameOver(true);
            gameDataAccess.updateGameData(gameData);

            NotificationMessage notificationMessage = new NotificationMessage("Player " + username + " has resigned. Game is now over.");
            return List.of(notificationMessage);
        } catch (DataAccessException e) {
            throw new InternalServerException("Internal Server Error");
        }
    }
    public List<ServerMessage> handleLeave(UserGameCommand userGameCommand, Session session) throws ChessServerException {
        try {
            System.out.println("HERE");
            int gameId = userGameCommand.getGameID();
            String authToken = userGameCommand.getAuthToken();
            String username = authDataAccess.getUsername(authToken);
            GameData gameData = gameDataAccess.getGame(gameId);
            String color = userGameCommand.getPlayerColor();
            String observer = "";

            if (color == null) {
                observer = "(observer)";

            }

            if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
                gameDataAccess.updateGameData(new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game()));
            } else {
                gameDataAccess.updateGameData(new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game()));
            }


            NotificationMessage notificationMessage = new NotificationMessage("Player " + username + " left the game." + observer);
            try {
                websocketConnectionManager.broadcastMessage(gameId, session, notificationMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            websocketConnectionManager.removeOpenSessions(session);
            websocketConnectionManager.removeGameSession(session, gameId);

            return List.of();
        } catch (DataAccessException ex) {
            throw new InternalServerException("Internal Server Error");
        }
   }
}
