package dataaccess;

import model.AuthData;

import java.util.*;

public class AuthDataAccess {
    private final Collection<AuthData> authList = new ArrayList<>();
    private final Map<String, Collection<AuthData>> authTable = new HashMap<>();

    public AuthData create(String username) throws AuthDataAccessException {
        if (hasUsername(username)) {
            throw new AuthDataAccessException("Username already exists.");
        }
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, username);
        authList.add(authData);
        authTable.put(authData.username(), authList);
        return authData;
    }

    public AuthData update(String username) {
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, username);
        authTable.put(authData.username(), authList);
        return authData;
    }

    public Collection<AuthData> get(String username) throws AuthDataAccessException {
        if (!hasUsername(username)) {
            throw new AuthDataAccessException("No AuthData for given username.");
        }
        return authTable.get(username);
    }

    public String getUsername(String authToken) throws AuthDataAccessException {
        for (Collection<AuthData> authList : authTable.values()) {
            for (AuthData authData : authList) {
                if (authData.authToken().equals(authToken)) {
                    return authData.username();
                }
            }
        }
        throw new AuthDataAccessException("Unauthorized.");
    }

    public boolean isAuthorized(String authToken) {
        for (Collection<AuthData> authList : authTable.values()) {
            for (AuthData authData : authList) {
                if (authData.authToken().equals(authToken)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void delete(String authToken) {
        for (Collection<AuthData> authList : authTable.values()) {
            for (AuthData authData : authList) {
                if (authData.authToken().equals(authToken)) {
                    authList.remove(authToken);
                }
            }
        }
    }


    public void deleteAllAuthData() {
        authTable.clear();
    }

    public boolean hasUsername(String username) {
        return authTable.containsKey(username);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
