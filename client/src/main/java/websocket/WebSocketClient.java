package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient extends Endpoint {

    Session session;            ;
    ServerMessageHandler serverMessageHandler;
    private static final Gson gson = new Gson();

    public WebSocketClient(String url, ServerMessageHandler serverMessageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    serverMessageHandler.notify(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public void send(String json) throws ResponseException {
        try {
            session.getBasicRemote().sendText(json);
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

// do I need these overridden methods?
//    @Override
//    public void onClose(Session session, CloseReason closeReason) {
//        serverMessageHandler.onClose(closeReason);
//    }
//
//    @Override
//    public void onError(Session session, Throwable thr) {
//        serverMessageHandler.onError(thr);
//    }

    // still need methods for each action of the user
    // this should be done in the ChessClient class i think?
    // maybe write it in a different class
}
