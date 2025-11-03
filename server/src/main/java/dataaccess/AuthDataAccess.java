package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
    public void deleteAllAuthData() throws DataAccessException;
    public void delete(String authToken) throws DataAccessException;
    public boolean isAuthorized(String authToken) throws DataAccessException;
    public String getUsername(String authToken) throws DataAccessException;
    public AuthData create(String username) throws DataAccessException;
    public AuthData update(String username) throws DataAccessException;


}
