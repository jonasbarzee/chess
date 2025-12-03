package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketConnectionManager {
    private final Set<Session> openSessions = ConcurrentHashMap.newKeySet();
    private final Map<Integer, Set<Session>> gameSessions = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public void register(Session session) {
        openSessions.add(session);
    }

    public void add(int gameId, Session session) {
        gameSessions.computeIfAbsent(gameId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void remove(Session session) {
        openSessions.remove(session);
//        var set = gameSessions.get(gameId);
//        if (set != null) {
//            set.remove(session);
//        }
    }

    public void broadcastMessageToAll(Integer gameId, ServerMessage serverMessage) throws IOException {
        System.out.println("Broadcasting message to All");
        System.out.println("Server Message " + serverMessage.getServerMessageType());
        String msg = gson.toJson(serverMessage);
        System.out.println("parsed to Json");

        System.out.println("Sessions in game " + gameId + ": " + gameSessions.get(gameId));

        for (Session session : gameSessions.getOrDefault(gameId, Set.of())) {
            if (session.isOpen()) {
                try {
                    session.getRemote().sendString(msg);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    gameSessions.get(gameId).remove(session);
                }
            }
        }
        System.out.println("Messages Sent");
    }


    public void broadcastMessage(Integer gameId, Session excludeSession, ServerMessage serverMessage) throws IOException {
        System.out.println("Broadcasting message");
        System.out.println("Server Message " + serverMessage.getServerMessageType());
        String msg = gson.toJson(serverMessage);
        System.out.println("parsed to Json");

        System.out.println("Sessions in game " + gameId + ": " + gameSessions.get(gameId));

        for (Session session : gameSessions.getOrDefault(gameId, Set.of())) {
            if (session.isOpen() && session != excludeSession) {
                try {
                    session.getRemote().sendString(msg);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    gameSessions.get(gameId).remove(session);
                }
            }
        }
        System.out.println("Messages Sent");
    }
}
