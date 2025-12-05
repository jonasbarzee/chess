package websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient extends Endpoint {

    Session session;
    ServerMessageHandler serverMessageHandler;
    private static final Gson GSON = new Gson();

    public WebSocketClient(String url, ServerMessageHandler serverMessageHandler) throws ResponseException {
        try {
            this.serverMessageHandler = serverMessageHandler;

            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();
            container.connectToServer(this, config, socketURI);

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
            }
        }


    public void send(String json) throws ResponseException {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(json);
            }
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("In onOpen");
        this.session = session;

        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
                String type = jsonObject.get("serverMessageType").getAsString();
                ServerMessage serverMessage;

                switch (type) {
                    case "LOAD_GAME" -> serverMessage = GSON.fromJson(message, LoadGameMessage.class);
                    case "ERROR" -> serverMessage = GSON.fromJson(message, ErrorMessage.class);
                    case "NOTIFICATION" -> serverMessage = GSON.fromJson(message, NotificationMessage.class);
                    default -> {
                        System.err.println("Unknown server message type: " + type);
                        return;
                    }
                }
                serverMessageHandler.notify(serverMessage);
            }
        });
        System.out.println("Connected message handler");
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Websocket closed, reason: " + closeReason);
    }

    @Override
    public void onError(Session session, Throwable thr) {
        thr.printStackTrace();
    }
}
