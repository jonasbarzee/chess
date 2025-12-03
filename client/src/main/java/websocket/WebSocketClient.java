package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient extends Endpoint {

    Session session;
    ServerMessageHandler serverMessageHandler;
    private static final Gson gson = new Gson();

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
                ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                serverMessageHandler.notify(serverMessage);
            }
        });
        System.out.println("Connected message handler");
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
