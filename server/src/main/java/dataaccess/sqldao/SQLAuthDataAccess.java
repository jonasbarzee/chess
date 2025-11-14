package dataaccess.sqldao;


import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DataNotFoundException;
import dataaccess.interfaces.AuthDataAccess;
import dataaccess.model.AuthData;

import java.util.UUID;


public class SQLAuthDataAccess extends SQLDataAccess implements AuthDataAccess {

    public SQLAuthDataAccess() {
    }

    public AuthData create(String username) throws DataAccessException {
        String authToken = generateToken();

        String statement = "INSERT INTO auth_data (auth_token, username) VALUES (?, ?)";
        executeUpdate(statement, authToken, username);
        return new AuthData(authToken, username);
    }

    public AuthData update(String username) throws DataAccessException {
        String authToken = generateToken();

        String statement = "INSERT INTO auth_data (auth_token, username) VALUES (?, ?)";
        executeUpdate(statement, authToken, username);
        return new AuthData(authToken, username);

    }

    public AuthData get(String authToken) throws DataAccessException {
        String statement = "SELECT * FROM auth_data WHERE auth_token = ?;";
        System.out.println(statement);
        return queryForObject(statement, rs -> new AuthData(rs.getString("username"), rs.getString("auth_token")), authToken);
    }

    public void delete(String authToken) throws DataAccessException {
        String statement = "DELETE FROM auth_data WHERE auth_token = ?;";
        executeUpdate(statement, authToken);
    }

    public void deleteAllAuthData() throws DataAccessException {
        String statement = "TRUNCATE TABLE auth_data;";
        executeUpdate(statement);
    }

    public boolean isAuthorized(String authToken) throws DataAccessException {
        String statement = "SELECT * FROM auth_data WHERE auth_token = ?;";
        AuthData authData = queryForObject(statement, rs -> new AuthData(rs.getString("auth_token"), rs.getString("username")), authToken);
        if (authData == null) {
            return false;
        }
        return true;
    }

    public String getUsername(String authToken) throws DataAccessException {
        String statement = "SELECT * FROM auth_data WHERE auth_token = ?;";
        AuthData authData = queryForObject(statement, rs -> new AuthData(rs.getString("auth_token"), rs.getString("username")), authToken);
        if (authData == null) {
            throw new DataNotFoundException("Unable to read from auth_data.");
        }
        return authData.username();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
