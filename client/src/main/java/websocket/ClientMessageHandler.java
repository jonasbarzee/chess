package websocket;

import ui.ChessClient;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class ClientMessageHandler implements ServerMessageHandler {
    private final ChessClient chessClient;

    public ClientMessageHandler(ChessClient chessClient) {
        this.chessClient = chessClient;
    }

    public void notify(ServerMessage serverMessage) {
        ServerMessage.ServerMessageType type = serverMessage.getServerMessageType();
        switch (type) {
            case LOAD_GAME -> handleLoadGame((LoadGameMessage) serverMessage);
            case ERROR -> handleError((ErrorMessage) serverMessage);
            case NOTIFICATION ->  handleNotification((NotificationMessage) serverMessage);
        }
    }

    public void handleLoadGame(LoadGameMessage loadGameMessage) {
        chessClient.setCurrentGame(loadGameMessage.getGame());
        chessClient.drawBoard(loadGameMessage.getGame().getBoard());

    }

    public void handleError(ErrorMessage errorMessage) {
        System.out.println("Server Error" + errorMessage.getErrorMessage());

    }

    public void handleNotification(NotificationMessage notificationMessage) {
        System.out.println("Notification: " + notificationMessage.getMessage());

    }

    public void addObserver(ChessClient chessClient) {

    }
}
