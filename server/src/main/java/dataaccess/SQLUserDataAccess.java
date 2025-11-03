package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDataAccess extends SQLDataAccess implements UserDataAccess {
    public SQLUserDataAccess() {
    }

    public void createUser(UserData userData) throws DataAccessException {
        String username = userData.username();
        String hashedPass = userData.password();
        String email = userData.email();

        String statement = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?);";
        executeUpdate(statement, username, hashedPass, email);
    }


    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT * FROM users WHERE username = ?;";
        return queryForObject(statement, rs -> new UserData(rs.getString("username"), rs.getString("password_hash"), rs.getString("email")), username);
    }

    public boolean userExists(String username) throws DataAccessException {
        String statement = "SELECT * FROM users WHERE username = ?;";
        UserData userData = queryForObject(statement, rs -> new UserData(rs.getString("username"), rs.getString("password_hash"), rs.getString("email")), username);
        if (userData == null) {
            return false;
        }
        return true;
    }

    public int update(UserData userData) throws DataAccessException {
        String username = userData.username();
        String hashedPass = userData.password();
        String email = userData.email();

        String statement = "UPDATE users SET password_hash = ?, email = ? WHERE username = ?;";
        return executeUpdate(statement, hashedPass, email, username);
    }

    public int delete(String username) throws DataAccessException {
        String statement = "DELETE FROM users WHERE username = ?;";
        return executeUpdate(statement, username);
    }

    public void deleteAllUsers() throws DataAccessException {
        String statement = "TRUNCATE TABLE users;";
        executeUpdate(statement);
    }
}

