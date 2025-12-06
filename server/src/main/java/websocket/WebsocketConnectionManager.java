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

    public void removeOpenSessions(Session session) {
        openSessions.remove(session);
    }

    public void removeGameSession(Session session, int gameId) {
        Set<Session> set = gameSessions.get(gameId);
        if (set != null) {
            set.remove(session);
        }
    }

    public void broadcastMessageToAll(Integer gameId, ServerMessage serverMessage) throws IOException {
        broadcastInternal(gameId, null, serverMessage, true);
    }

    public void broadcastMessage(Integer gameId, Session excludeSession, ServerMessage serverMessage) throws IOException {
        broadcastInternal(gameId, excludeSession, serverMessage, false);
    }

    private void broadcastInternal(Integer gameId, Session exclude, ServerMessage msgObj, boolean toAll) throws IOException {
        System.out.println("Broadcasting message" + (toAll ? " to All" : ""));
        System.out.println("Server Message " + msgObj.getServerMessageType());

        String msg = gson.toJson(msgObj);
        System.out.println("parsed to Json");

        Set<Session> sessions = gameSessions.getOrDefault(gameId, Set.of());
        System.out.println("Sessions in game " + gameId + ": " + sessions);

        for (Session session : sessions) {
            if (!session.isOpen()) continue;
            if (!toAll && session == exclude) continue;

            try {
                session.getRemote().sendString(msg);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                gameSessions.get(gameId).remove(session);
            }
        }
        System.out.println("Messages Sent");
    }
}
