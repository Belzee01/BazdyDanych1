package database;

import database.services.DatabaseService;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static database.Queries.INSERT_NEW_HASLA;
import static database.Queries.INSERT_NEW_USER;
import static database.Queries.SELECT_NEW_USER_ID;

public class DatabaseController {

    private static Logger logger = Logger.getLogger(DatabaseController.class);

    private DatabaseService databaseService = null;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    private void cleanUpConnections() {
        if (databaseService.getConnection() != null) {
            try {
                System.err.print("Transaction is being rolled back");
                databaseService.getConnection().rollback();
            } catch (SQLException excep) {
                excep.printStackTrace();
            }
        }
    }

    public void insertNewUser(String name, String surname) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_USER);

            logger.info("Executing insertNewUser");

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);

            preparedStatement.executeUpdate();

            logger.info("New user inserted");
        } catch (SQLException e) {
            e.printStackTrace();
            cleanUpConnections();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Integer selectNewUserId(String surname) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String userId = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_NEW_USER_ID);

            preparedStatement.setString(1, surname);

            resultSet = preparedStatement.executeQuery();

            try {
                while (resultSet.next()) {
                    userId = resultSet.getString("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            cleanUpConnections();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (resultSet == null) {
            logger.info("ResultSet null");
            return null;
        }

        assert userId != null;
        return Integer.valueOf(userId);
    }

    public void insertNewCredentials(String login, String password, String selector) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_HASLA);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, selectNewUserId(selector));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            cleanUpConnections();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
