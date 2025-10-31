package dataaccess.sqldao;


import dataaccess.exceptions.AuthDataAccessException;
import dataaccess.exceptions.SQLDataAccessException;
import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;


public class SQLAuthDataAccess extends SQLDataAccess {

    public SQLAuthDataAccess()  {
    }

    public void create(AuthData authData) throws AuthDataAccessException {
        String user = authData.username();
        String token = authData.authToken();

        String statement = "INSERT INTO auth_data (auth_token, username) VALUES (?, ?)";
        try {
            executeUpdate(statement,token ,user);
        } catch (SQLDataAccessException e) {
            throw new AuthDataAccessException(String.format("Unable to insert into auth_data, %s", e.getMessage()));
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

    public int delete(String authToken) throws AuthDataAccessException {
        String statement = "DELETE FROM auth_data WHERE auth_token = ?;";
        try {
            return executeUpdate(statement, authToken);
        } catch (SQLDataAccessException e) {
            throw new AuthDataAccessException(String.format("Unable to delete from auth_data, %s", e.getMessage()));
        }

    }



}
