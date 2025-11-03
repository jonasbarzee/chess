package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDataAccess extends SQLDataAccess implements UserDataAccess {
    public SQLUserDataAccess() {
    }

    public void createUser(UserData userData) throws UserDataAccessException {
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

    public UserData getUser(String username) {
        String statement = "SELECT * FROM users WHERE username = ?;";
        try {
            return queryForObject(statement, rs -> new UserData(rs.getString("username"), rs.getString("password_hash"), rs.getString("email")), username);
        } catch (SQLException e) {
            System.out.println("SWALLOWED ERROR");
            return null;
        }
    }

    public boolean userExists(String username) {
        String statement = "SELECT * FROM users WHERE username = ?;";
        try {
            UserData userData = queryForObject(statement, rs -> new UserData(rs.getString("username"), rs.getString("password_hash"), rs.getString("email")), username);
            if (userData == null) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            return false;
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

    public void deleteAllUsers() throws SQLDataAccessException {
        String statement = "TRUNCATE TABLE users;";
        try {
            executeUpdate(statement);
        } catch (SQLDataAccessException e) {
            throw new SQLDataAccessException("Error");
        }
    }
}

