package dataaccess;

import javax.xml.crypto.Data;

public class DatabaseUnavailableException extends DataAccessException {
    public DatabaseUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
