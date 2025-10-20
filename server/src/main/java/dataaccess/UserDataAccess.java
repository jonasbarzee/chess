package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDataAccess {
    private final Map<String, UserData> userTable = new HashMap<>();

    public void createUser(UserData userData) throws UserDataAccessException {
        if (userTable.containsKey(userData.username())) {
            throw new UserDataAccessException("User already exists");
        }
        userTable.put(userData.username(), userData);
    }

    public UserData getUser(String username) throws UserDataAccessException {
        if (!userTable.containsKey(username)) {
            throw new UserDataAccessException("User does not exist");
        }
        return userTable.get(username);
    }

    public void updateUser(UserData userData) throws UserDataAccessException{
        if (!userTable.containsKey(userData.username())) {
            throw new UserDataAccessException("User does not exist");
        }
        userTable.put(userData.username(), userData);
    }

    public void deleteUser(String username) {
        userTable.remove(username);
    }

    public void deleteAllUsers() {
        userTable.clear();
    }
}
