package dataaccess.interfaces;

import dataaccess.exceptions.DataAccessException;
import model.UserData;

public interface UserDataAccess {
    public void createUser(UserData userData) throws DataAccessException;
    public boolean userExists(String username) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void deleteAllUsers() throws DataAccessException;

}
