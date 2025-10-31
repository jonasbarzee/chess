package dataaccess;

import dataaccess.exceptions.AuthDataAccessException;
import model.AuthData;

public interface AuthDataAccess {
    public void deleteAllAuthData();
    public void delete(String authToken);
    public boolean isAuthorized(String authToken);
    public String getUsername(String authToken) throws AuthDataAccessException;
    public AuthData create(String username) throws AuthDataAccessException;
    public AuthData update(String username) throws AuthDataAccessException;


}
