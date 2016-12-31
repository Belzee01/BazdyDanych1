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

    public void closeConnection() {
        try{
            if(connection != null)
                connection.close();
            logger.info("Closed connection");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

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
