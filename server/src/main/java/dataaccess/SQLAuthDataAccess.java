package dataaccess;


import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;


public class SQLAuthDataAccess extends SQLDataAccess implements AuthDataAccess {

    public SQLAuthDataAccess() {
    }

    public AuthData create(String username) throws AuthDataAccessException {
        String authToken = generateToken();

        String statement = "INSERT INTO auth_data (auth_token, username) VALUES (?, ?)";
        try {
            executeUpdate(statement, authToken, username);
            return new AuthData(authToken, username);
        } catch (SQLDataAccessException e) {
            throw new AuthDataAccessException(String.format("Unable to insert into auth_data, %s", e.getMessage()));
        }
    }

    public AuthData update(String username) {
        String authToken = generateToken();

        String statement = "INSERT INTO auth_data (auth_token, username) VALUES (?, ?)";
        try {
            executeUpdate(statement, authToken, username);
            return new AuthData(authToken, username);
        } catch (SQLDataAccessException e) {
            System.out.println("SWALLOWED ERROR");
            return null;
        }
    }

    public AuthData get(String authToken) throws AuthDataAccessException {
        String statement = "SELECT * FROM auth_data WHERE auth_token = ?;";
        try {
            return queryForObject(statement, rs -> new AuthData(rs.getString("username"), rs.getString("auth_token")), authToken);
        } catch (SQLException e) {
            throw new AuthDataAccessException("Unable to read from auth_data");
        }
    }

    public void delete(String authToken) {
        String statement = "DELETE FROM auth_data WHERE auth_token = ?;";
        try {
            executeUpdate(statement, authToken);
        } catch (SQLDataAccessException e) {
            System.out.println("SWALLOWED ERROR");
        }
    }

    public void deleteAllAuthData() {
        String statement = "TRUNCATE TABLE auth_data;";
        try {
            executeUpdate(statement);
        } catch (SQLDataAccessException e) {
            System.out.println("SWALLOWED ERROR");
        }
    }

    public boolean isAuthorized(String authToken) {
        String statement = "SELECT * FROM auth_data WHERE auth_token = ?;";
        try {
            AuthData authData = queryForObject(statement, rs -> new AuthData(rs.getString("username"), rs.getString("auth_token")), authToken);
            if (authData == null) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.out.println("SWALLOWED ERROR");
            return false;
        }
    }

    public String getUsername(String authToken) throws AuthDataAccessException {
        String statement = "SELECT * FROM auth_data WHERE auth_token = ?;";
        try {
            AuthData authData = queryForObject(statement, rs -> new AuthData(rs.getString("auth_token"), rs.getString("username")), authToken);
            if (authData == null) {
                throw new AuthDataAccessException("Unable to read from auth_data.");
            }
            System.out.println(authData.username());
            return authData.username();
        } catch (SQLException e) {
            throw new AuthDataAccessException("Unable to read from auth_data");
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }


}
