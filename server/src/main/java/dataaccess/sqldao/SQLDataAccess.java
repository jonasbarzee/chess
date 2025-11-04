package dataaccess.sqldao;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import dataaccess.interfaces.ResultSetMapper;

import java.sql.*;

import static java.sql.Types.NULL;

public abstract class SQLDataAccess {


    private static final String[] CreateStatements = {
//            "USE chess;",

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

    static public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        Connection connection = DatabaseManager.getConnection();
        for (String statement : CreateStatements) {
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.executeUpdate();
            } catch (SQLException e) {
                throw SQLExceptionMapper.map(e);
            }
        }
    }

    protected int executeUpdate(String statement, Object... params) throws DataAccessException {
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
        } catch (SQLException e) {
            throw SQLExceptionMapper.map(e);
        }
    }

    protected <T> T queryForObject(String sqlString, ResultSetMapper<T> mapper, Object... params) throws
            DataAccessException {
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
        } catch (SQLException e) {
            throw SQLExceptionMapper.map(e);
        }
    }


}




