package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDataAccess {
    private final Map<String, AuthData> authTable = new HashMap<>();

    public AuthData create(AuthData authData) throws AuthDataAccessException {
        if (hasToken(authData.authToken())) {
            throw new AuthDataAccessException("AuthData already exists for given AuthToken");
        }
        authTable.put(authData.authToken(), authData);
        return authData;
    }

    public AuthData get(String authToken) throws AuthDataAccessException {
        if (hasToken(authToken)) {
            throw new AuthDataAccessException("No AuthData for given AuthToken.");
        }
        return authTable.get(authToken);
    }

    public void delete(String authToken) {
        authTable.remove(authToken);
    }

    public void deleteAll() {
        authTable.clear();
    }

    public boolean hasToken(String authToken) {
        return authTable.containsKey(authToken);
    }
}
