package database.exceptions;

import java.sql.SQLException;

/**
 * Created by DW on 2016-12-02.
 */
public class DatabaseException extends SQLException {
    public DatabaseException(String s) {
        super(s);
    }
}
