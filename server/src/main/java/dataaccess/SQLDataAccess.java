package dataaccess;

import chess.ChessGame;

import java.sql.*;

import static java.sql.Types.NULL;

public abstract class SQLDataAccess {


    private static final String[] createStatements = {
            "USE chess;",

            """
            CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(50) PRIMARY KEY,
                email VARCHAR(255) UNIQUE NOT NULL,
                password_hash VARCHAR(255) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS auth_data (
                auth_token VARCHAR(255) PRIMARY KEY,
                username VARCHAR(50) NOT NULL,
                issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            """,
            """ 
           CREATE TABLE IF NOT EXISTS games (
                game_id INT NOT NULL AUTO_INCREMENT,
                chess_game LONGTEXT NOT NULL,
                game_name VARCHAR(50) NOT NULL,
                white_username VARCHAR(50),
                black_username VARCHAR(50),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (game_id)
            );"""
    };

    static public void configureDatabase() throws SQLDataAccessException {
        try {
            DatabaseManager.createDatabase();
            Connection connection = DatabaseManager.getConnection();
            for (String statement : createStatements) {

                try (PreparedStatement ps = connection.prepareStatement(statement)) {
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new AuthDataAccessException("Unable to configure the database.");
                }

            }
        } catch (DataAccessException ex) {
            throw new SQLDataAccessException(ex.getMessage());
        }
    }

    protected int executeUpdate(String statement, Object... params) throws SQLDataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    } else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    } else if (param instanceof ChessGame p) {
                        ps.setString(i + 1, p.toString());
                    } else if (param == null) {
                        ps.setNull(i + 1, NULL);
                    }
                }
                int affectedRows = ps.executeUpdate();

                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                // returns affected rows if now auto incrementing id
                return affectedRows;
            }
        } catch (Exception ex) {
            throw new SQLDataAccessException(String.format("couldn't update database: %s, %s", statement, ex.getMessage()));
        }
    }

    protected <T> T queryForObject(String sqlString, ResultSetMapper<T> mapper, Object... params) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlString)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapper.map(rs);
                }
                return null;
            }
        } catch (DataAccessException e) {
            throw new SQLException(String.format("Unable to execute query %s %s", sqlString, e.getMessage()));
        }
    }


}




