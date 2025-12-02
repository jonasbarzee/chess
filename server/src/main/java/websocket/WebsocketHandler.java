package websocket;

import com.google.gson.Gson;
import dataaccess.interfaces.AuthDataAccess;
import dataaccess.interfaces.GameDataAccess;
import dataaccess.sqldao.SQLAuthDataAccess;
import dataaccess.sqldao.SQLGameDataAccess;
import io.javalin.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

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
        String token = context.queryParam("authToken");
        int gameId = Integer.parseInt(context.queryParam("gameId"));

        List<ServerMessage> messages = gameWebSocketService.handleConnect(context.session, token, gameId);

        for (ServerMessage message : messages) {
            context.send(gson.toJson(message));
        }
    }

    @Override
    public void handleMessage(WsMessageContext context) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(context.message(), UserGameCommand.class);

            List<ServerMessage> outgoingMessages = switch (userGameCommand.getCommandType()) {
                case CONNECT -> gameWebSocketService.handleConnect(context.session, userGameCommand.getAuthToken(), userGameCommand.getGameID());
                case MAKE_MOVE -> gameWebSocketService.handleMove(userGameCommand, context.session);
                case RESIGN -> gameWebSocketService.handleResign(userGameCommand, context.session);
                case LEAVE -> gameWebSocketService.handleLeave(userGameCommand, context.session);
            };

            for (ServerMessage message : outgoingMessages) {
                wsConnectionManager.broadcastMessage(userGameCommand.getGameID().toString(), context.session, message);
            }
        } catch (Exception ex) {
            context.send(gson.toJson(ServerMessage.ServerMessageType.ERROR));
        }
    }

    @Override
    public void handleClose(WsCloseContext context) {
        String gameId = context.queryParam("gameId");
        wsConnectionManager.remove(gameId, context.session);
    }

    // implement the methods here like make_move, leave, resign, connect
    // use the connections variable here to do things
}
