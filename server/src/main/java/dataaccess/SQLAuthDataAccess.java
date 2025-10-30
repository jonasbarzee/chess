package dataaccess;


import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;


public class SQLAuthDataAccess extends SQLDataAccess {

    public SQLAuthDataAccess() throws AuthDataAccessException {
        try {
            configureDatabase();
        } catch (SQLDataAccessException ex) {
            throw new AuthDataAccessException(String.format("Couldn't configure the database, %s", ex.getMessage()));
        }
    }

    public AuthData create(String username) throws AuthDataAccessException {
        String authToken = generateToken();

        String statement = "INSERT INTO auth_data (auth_token, username) VALUES (?, ?)";
        try {
            executeUpdate(statement, authToken, username);
        } catch (SQLDataAccessException e) {
            throw new AuthDataAccessException(String.format("Unable to insert into auth_data, %s", e.getMessage()));
        }
        return new AuthData(authToken, username);
    }

    public AuthData get(String authToken) throws AuthDataAccessException {
        String statement = ("SELECT * FROM auth_data WHERE auth_token = ?;");
        try {
            return queryForObject(statement, rs -> new AuthData(rs.getString("username"), rs.getString("auth_token")), authToken);
        } catch (SQLException e) {
            throw new AuthDataAccessException("Unable to read from auth_data");
        }
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
