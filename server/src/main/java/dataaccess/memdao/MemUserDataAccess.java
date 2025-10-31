package dataaccess.memdao;

import dataaccess.UserDataAccess;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UserDataAccessException;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemUserDataAccess implements UserDataAccess {
    private final Map<String, UserData> userTable = new HashMap<>();

    public void createUser(UserData userData) throws DataAccessException {
        if (getUser(userData.username()) != null) {
            throw new UserDataAccessException("User already exists");
        }
        userTable.put(userData.username(), userData);
    }

    public UserData getUser(String username) {
        if (userTable.containsKey(username)) {
            return userTable.get(username);
        }
        return null;
    }

    public boolean userExists(String username) {
        return userTable.containsKey(username);
    }

    public void updateUser(UserData userData) throws UserDataAccessException {
        if (getUser(userData.username()) == null) {
            throw new UserDataAccessException("User does not exist");
        }
        userTable.put(userData.username(), userData);
    }

    public void deleteAllUsers() {
        userTable.clear();
    }
}
