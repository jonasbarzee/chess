package dataaccess;

import java.sql.SQLException;

public class SQLExceptionMapper {

    public static DataAccessException map(SQLException e) {
        String sqlState = e.getSQLState();

        if (sqlState == null) {
            return new DataAccessException("Unknown SQL error: " + e.getMessage(), e);
        }

        if (sqlState.startsWith("23")) {
            return new DuplicateKeyException("Dublicate or constrain violation: " + e.getMessage(), e);
        }

        if ("08001".equals(sqlState) || e.getMessage().contains("Communications link failure")) {
            return new DatabaseUnavailableException("Database connection lost", e);
        }
        return new DataAccessException("General database error: " + e.getMessage(), e);
    }
}
