package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.UserData;

public interface UserDataAccess {
    public void createUser(UserData userData) throws DataAccessException;
    public boolean userExists(String username);
    public UserData getUser(String username);
    public void deleteAllUsers();

}
