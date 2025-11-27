package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketConnectionManager {
    private final Map<String, Set<Session>> gameSessions = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public void add(String gameId, Session session) {
        gameSessions.computeIfAbsent(gameId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void remove(String gameId, Session session) {
        var set = gameSessions.get(gameId);
        if (set != null) {
            set.remove(session);
        }
    }

    public void broadcastMessage(String gameId, Session excludeSession, ServerMessage serverMessage) throws IOException {
        String msg = gson.toJson(serverMessage);

        for (Session session : gameSessions.getOrDefault(gameId, Set.of())) {
            if (session.isOpen() && session != excludeSession) {
                try {
                    session.getRemote().sendString(msg);
                } catch (Exception e) {
                    gameSessions.get(gameId).remove(session);
                }
            }
        }
    }
}
