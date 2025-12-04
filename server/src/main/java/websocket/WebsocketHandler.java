package websocket;

import com.google.gson.Gson;
import dataaccess.interfaces.AuthDataAccess;
import dataaccess.interfaces.GameDataAccess;
import dataaccess.sqldao.SQLAuthDataAccess;
import dataaccess.sqldao.SQLGameDataAccess;
import io.javalin.websocket.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final WebsocketConnectionManager wsConnectionManager;
    private final GameWebSocketService gameWebSocketService;
    private final Gson gson = new Gson();

    public WebsocketHandler() {
        AuthDataAccess authDataAccess = new SQLAuthDataAccess();
        GameDataAccess gameDataAccess = new SQLGameDataAccess();
        this.wsConnectionManager = new WebsocketConnectionManager();
        this.gameWebSocketService = new GameWebSocketService(authDataAccess, gameDataAccess, wsConnectionManager);
    }

    @Override
    public void handleConnect(WsConnectContext context) {
        // temp debugging
        System.out.println("Query parameters " + context.queryParamMap());
        System.out.println("Opening");

        wsConnectionManager.register(context.session);
    }

    @Override
    public void handleMessage(WsMessageContext context) {
        System.out.println(context.message());
        try {
            UserGameCommand userGameCommand = parseCommand(context.message());

            List<ServerMessage> outgoingMessages = switch (userGameCommand.getCommandType()) {
                case CONNECT -> gameWebSocketService.handleJoin(context.session, userGameCommand.getAuthToken(), userGameCommand.getGameID());
                case MAKE_MOVE -> {
                    MakeMoveCommand makeMoveCommand = (MakeMoveCommand) userGameCommand;
                    yield gameWebSocketService.handleMove(makeMoveCommand, context.session, makeMoveCommand.getGameID());
                }
                case RESIGN -> gameWebSocketService.handleResign(userGameCommand, context.session);
                case LEAVE -> gameWebSocketService.handleLeave(userGameCommand, context.session);
            };

            sendMessages(outgoingMessages, userGameCommand, context);

        } catch (Exception ex) {
            System.out.println("WebSocket error: " + ex.getMessage());

            ServerMessage errorMessage = new ErrorMessage(ex.getMessage());
            System.out.println(gson.toJson(errorMessage));
            try {
                context.session.getRemote().sendString(gson.toJson(errorMessage));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    public void handleClose(WsCloseContext context) {
        System.out.println("Closing");
        wsConnectionManager.removeOpenSessions(context.session);
    }

    private UserGameCommand parseCommand(String json) {
        UserGameCommand base = gson.fromJson(json, UserGameCommand.class);

        return switch (base.getCommandType()) {
            case MAKE_MOVE -> gson.fromJson(json, MakeMoveCommand.class);
            case LEAVE -> gson.fromJson(json, UserGameCommand.class);
            case RESIGN -> gson.fromJson(json, UserGameCommand.class);
            case CONNECT -> gson.fromJson(json, UserGameCommand.class);
        };
    }

    private void sendMessages(Collection<ServerMessage> serverMessages, UserGameCommand userGameCommand, WsMessageContext context) {
        try {
            int gameId = userGameCommand.getGameID();
            for (ServerMessage message : serverMessages) {
                ServerMessage.ServerMessageType type = message.getServerMessageType();

                switch (userGameCommand.getCommandType()) {
                    case CONNECT -> {
                        if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
                            context.session.getRemote().sendString(gson.toJson(message));
                        } else {
                            wsConnectionManager.broadcastMessage(gameId, context.session, message);
                        }
                    }
                    case MAKE_MOVE -> {
                        if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
                            wsConnectionManager.broadcastMessageToAll(gameId, message);
                        } else if (type == ServerMessage.ServerMessageType.ERROR) {
                            context.session.getRemote().sendString(gson.toJson(message));
                        } else {
                            wsConnectionManager.broadcastMessage(gameId, context.session, message);
                        }
                    }
                    case LEAVE -> wsConnectionManager.broadcastMessage(gameId, context.session, message);
                    case RESIGN -> wsConnectionManager.broadcastMessageToAll(gameId, message);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
   }
}
