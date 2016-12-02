package database;

import database.services.DatabaseService;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static database.Queries.INSERT_NEW_HASLA;
import static database.Queries.INSERT_NEW_USER;

public class DatabaseController {

    private PreparedStatement preparedStatement = null;

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
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_USER);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
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

    public void insertNewHasla(String login, String password, String selector) {
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_HASLA);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, selector);
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
