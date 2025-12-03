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
        // temp debugging
        System.out.println("context object " + context);
        System.out.println("Query parameters " + context.queryParamMap());
        System.out.println("URL " + context.getUpgradeCtx$javalin().fullUrl());
        System.out.println("Session " + context.session);

        System.out.println("Opening");

        wsConnectionManager.register(context.session);

//
//        String token = context.queryParam("authToken");
//        int gameId = Integer.parseInt(context.queryParam("gameId"));
//
//        List<ServerMessage> messages = gameWebSocketService.handleJoin(context.session, token, gameId);
//
//        for (ServerMessage message : messages) {
//            context.send(gson.toJson(message));
//        }
    }

    @Override
    public void handleMessage(WsMessageContext context) {
        System.out.println("Handling message");
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(context.message(), UserGameCommand.class);
            System.out.println(userGameCommand.getCommandType());
            System.out.println(userGameCommand.getGameID());
            System.out.println(userGameCommand.getAuthToken());

            List<ServerMessage> outgoingMessages = switch (userGameCommand.getCommandType()) {
                case CONNECT -> gameWebSocketService.handleJoin(context.session, userGameCommand.getAuthToken(), userGameCommand.getGameID());
                case MAKE_MOVE -> gameWebSocketService.handleMove(userGameCommand, context.session);
                case RESIGN -> gameWebSocketService.handleResign(userGameCommand, context.session);
                case LEAVE -> gameWebSocketService.handleLeave(userGameCommand, context.session);
            };

            for (ServerMessage message : outgoingMessages) {
                if (message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                    wsConnectionManager.broadcastMessageToAll(userGameCommand.getGameID(), message);
                }
                else {
                    wsConnectionManager.broadcastMessage(userGameCommand.getGameID(), context.session, message);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error!!");
            System.out.println(ex.getMessage());
            context.send(gson.toJson(ServerMessage.ServerMessageType.ERROR));
        }
    }

    @Override
    public void handleClose(WsCloseContext context) {
        System.out.println("Closing");
//        String gameId = context.queryParam("gameId");
//        int gameIdInteger = Integer.parseInt(gameId);
        wsConnectionManager.remove(context.session);
    }

    // implement the methods here like make_move, leave, resign, connect
    // use the connections variable here to do things
}
