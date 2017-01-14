package database.exceptions;

import java.sql.SQLException;

/**
 * Simplifies exception handling
 */
public class DatabaseException extends SQLException {
    public DatabaseException(String s) {
        super(s);
    }
}
