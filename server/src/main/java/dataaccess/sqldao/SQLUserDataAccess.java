package dataaccess.sqldao;

import dataaccess.exceptions.SQLDataAccessException;
import dataaccess.exceptions.UserDataAccessException;
import model.UserData;

import java.sql.SQLException;

public class SQLUserDataAccess extends SQLDataAccess {
    public SQLUserDataAccess() {
    }

    public void create(UserData userData) throws UserDataAccessException {
        String username = userData.username();
        String hashedPass = userData.password();
        String email = userData.email();

        String statement = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?);";
        try {
            executeUpdate(statement, username, hashedPass, email);
        } catch (SQLDataAccessException e) {
           throw new UserDataAccessException(String.format("Unable to insert into users, %s, %s", statement, e.getMessage()));
        }
    }

    public UserData get(String username) throws UserDataAccessException {
       String statement = "SELECT * FROM users WHERE username = ?;";
       try {
           return queryForObject(statement, rs -> new UserData(rs.getString("username"), rs.getString("password_hash"), rs.getString("email")), username);
       } catch (SQLException e) {
           throw new UserDataAccessException("Unable to read from users");
       }
    }

    public int update(UserData userData) throws UserDataAccessException {
        String username = userData.username();
        String hashedPass = userData.password();
        String email = userData.email();

        String statement = "UPDATE users SET password_hash = ?, email = ? WHERE username = ?;";
        try {
            return executeUpdate(statement, hashedPass, email, username);
        } catch (SQLDataAccessException e) {
            throw new UserDataAccessException("Unable to update users:" + e.getMessage());
        }
    }

    public int delete(String username) throws UserDataAccessException {
        String statement = "DELETE FROM users WHERE username = ?;";
        try {
            return executeUpdate(statement, username);
        } catch (SQLDataAccessException e) {
            throw new UserDataAccessException("Unable to delete from users");
        }
    }
}

