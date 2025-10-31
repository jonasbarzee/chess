package dataaccess.memdao;

import dataaccess.exceptions.AuthDataAccessException;
import model.AuthData;

import java.util.*;

public class MemAuthDataAccess {
    private final Map<String, AuthData> authTable = new HashMap<>();

    public AuthData create(String username) throws AuthDataAccessException {
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, username);
        authTable.put(authData.authToken(), authData);
        return authData;
    }

    public AuthData update(String username) {
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, username);
        authTable.put(authData.authToken(), authData);
        return authData;
    }

    public AuthData get(String authToken) throws AuthDataAccessException {
        if (!isAuthorized(authToken)) {
            throw new AuthDataAccessException("Unauthorized");
        }
        return authTable.get(authToken);
    }

    public String getUsername(String authToken) throws AuthDataAccessException {
        for (AuthData authData : authTable.values()) {
            if (authData.authToken().equals(authToken)) {
                return authData.username();
            }
        }
        throw new AuthDataAccessException("Unauthorized");
    }

    public void delete(String authToken) {
        String usernameToDelete = null;

        for (Map.Entry<String, AuthData> entry : authTable.entrySet()) {
            if (entry.getValue().authToken().equals(authToken)) {
                usernameToDelete = entry.getKey();
                break;
            }
        }
        if (usernameToDelete != null) {
            authTable.remove(usernameToDelete);
        }
    }


    public void deleteAllAuthData() {
        authTable.clear();
    }

    public boolean isAuthorized(String authToken) {
        return authTable.containsKey(authToken);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
