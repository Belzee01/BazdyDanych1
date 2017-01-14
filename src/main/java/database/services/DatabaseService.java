package database.services;

import database.DatabaseController;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseService {

    private Connection connection = null;

    private Properties properties = null;

    private static Logger logger = Logger.getLogger(DatabaseController.class);

    public DatabaseService() {
        properties = loadProperties();
    }

    /**
     * Method for setting autocommit option, if false then autocommit is disabled, otherwise it's enabled
     * @param commit
     */
    public void setAutoCommit(boolean commit) {
        try {
            this.connection.setAutoCommit(commit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens application.properties file in which there are all information about database connection
     * @return Properties object containing all data needed to connect database
     */
    private Properties loadProperties() {
        Properties properties = new Properties();
        InputStream stream = null;

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            stream = classLoader.getResourceAsStream("application.properties");
            properties.load(stream);
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            if (stream != null) {
                try {
                    stream.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    /**
     * Makes connection to database, unless invoked, it's not possible to run any queries on database
     */
    public void connectToDb() {
        try {
             Class.forName(properties.getProperty("JDBC_DRIVER"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(properties.getProperty("DB_URL"), properties.getProperty("USER"), properties.getProperty("PASS"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        logger.info("Connected to the DB");
    }

    /**
     * Closes connection to database. Should be invoked after we're finished working with database
     */
    public void closeConnection() {
        try{
            if(connection != null)
                connection.close();
            logger.info("Closed connection");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for current connection to database
     * @return Current connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Rollbacks failed transaction
     */
    public void cleanUpConnections() {
        if (getConnection() != null) {
            try {
                System.err.print("Transaction is being rolled back");
                getConnection().rollback();
            } catch (SQLException excep) {
                excep.printStackTrace();
            }
        }
    }
}
