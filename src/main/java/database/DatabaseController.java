package database;

import database.services.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import sample.views.AdminListView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static database.Queries.*;
import static database.Queries.AdminUser.*;

public class DatabaseController {

    private static Logger logger = Logger.getLogger(DatabaseController.class);

    private DatabaseService databaseService = null;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
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
            databaseService.cleanUpConnections();
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
            databaseService.cleanUpConnections();
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
            databaseService.cleanUpConnections();
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

    public boolean checkCredentials(String login, String password) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean authen = false;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_GIVEN_CREDENTIALS);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
            authen = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            databaseService.cleanUpConnections();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return authen;
    }

    public ObservableList<AdminListView> selectAllAdmins() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<AdminListView> data = FXCollections.observableArrayList();
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_ALL_ADMINS);

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int userId = resultSet.getInt("uzytkownicy_id");
                int adminId = resultSet.getInt("id");
                preparedStatement = databaseService.getConnection().prepareStatement(SELECT_USER_NAME_AND_SURNAME_BY_ID);
                preparedStatement.setInt(1, userId);
                ResultSet user = preparedStatement.executeQuery();

                preparedStatement = databaseService.getConnection().prepareStatement(SELECT_HASLA_LOGIN_BY_USER_ID);
                preparedStatement.setInt(1, userId);
                ResultSet haslo = preparedStatement.executeQuery();

                if(user.next() && haslo.next())
                    data.add(new AdminListView(adminId, haslo.getString("login"), user.getString("imie"), user.getString("nazwisko")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            databaseService.cleanUpConnections();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    public void deleteFromAdminList(int adminId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(DELETE_FROM_ADMIN_LIST);
            preparedStatement.setInt(1, adminId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            databaseService.cleanUpConnections();
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
