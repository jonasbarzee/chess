package dataaccess;

import model.UserData;

public interface UserDataAccess {
    public void createUser(UserData userData) throws UserDataAccessException;
    public boolean userExists(String username);
    public UserData getUser(String username);
    public void deleteAllUsers() throws SQLDataAccessException;

}
