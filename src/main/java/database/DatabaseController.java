package database;

import database.exceptions.DatabaseException;
import database.services.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import sample.views.*;

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

    public void checkCredentials(String login, String password) throws DatabaseException {
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
        if (!authen)
            throw new DatabaseException("ERROR - Niepoprawny uzytkownik");
    }

    public boolean authenticate(String login, String password) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer userId = null;
        boolean authen = false;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_GIVEN_CREDENTIALS);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                userId = resultSet.getInt("uzytkownicy_id");
            }
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_ADMIN);
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
                authen = true;
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
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_ALL_ADMINS_FROM_VIEW);

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(new AdminListView(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("imie"), resultSet.getString("nazwisko")));
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
            preparedStatement = databaseService.getConnection().prepareStatement(DELETE_FROM_ADMIN_LIST_BY_USER_ID);
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

    public ObservableList<ExamineListView> selectAllExamines() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<ExamineListView> data = FXCollections.observableArrayList();
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_ALL_EXAMINES);

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(
                        new ExamineListView(
                                resultSet.getInt("id"),
                                resultSet.getString("nazwa"),
                                resultSet.getInt("cena"),
                                resultSet.getInt("czas")
                        )
                );
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

    public void deleteFromExamineList(int examineId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(DELETE_FROM_EXAMINE_LIST);
            preparedStatement.setInt(1, examineId);
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

    public ObservableList<DoctorsListView> selectAllDoctors() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<DoctorsListView> data = FXCollections.observableArrayList();
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_ALL_DOCTORS);

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(
                        new DoctorsListView(
                                resultSet.getInt("id"),
                                resultSet.getString("imie"),
                                resultSet.getString("nazwisko")
                        )
                );
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

    public void deleteFromDoctorsList(int examineId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(DELETE_FROM_DOCTORS_LIST);
            preparedStatement.setInt(1, examineId);
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

    public ObservableList<CompanyListView> selectAllCompanies() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<CompanyListView> data = FXCollections.observableArrayList();
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_ALL_COMAPNIES);

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(
                        new CompanyListView(
                                resultSet.getInt("id"),
                                resultSet.getString("nazwa"),
                                resultSet.getString("nip"),
                                resultSet.getString("adres")
                        )
                );
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

    public void deleteFromCompanyList(int companyId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(DELETE_FROM_COMPANY_LIST);
            preparedStatement.setInt(1, companyId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.info("Nie można usunąć firmy jeżeli są dla niej zarejestrowni pacjenci");
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

    public ObservableList<PatientListView> selectAllPatients() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<PatientListView> data = FXCollections.observableArrayList();
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_ALL_PACIENT_VIEW);

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(
                        new PatientListView(
                                resultSet.getInt("id"),
                                resultSet.getString("imie"),
                                resultSet.getString("nazwisko"),
                                resultSet.getString("nazwa")
                        )
                );
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

    public void deleteFromPatientList(int patientId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(DELETE_FROM_PATIENT_LIST);
            preparedStatement.setInt(1, patientId);
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

    public ObservableList<UserListView> selectAllUsers() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<UserListView> data = FXCollections.observableArrayList();
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_ALL_USERS_VIEW);

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(
                        new UserListView(
                                resultSet.getInt("id"),
                                resultSet.getString("imie"),
                                resultSet.getString("nazwisko"),
                                resultSet.getString("login")
                        )
                );
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

    public void deleteFromUserList(int userId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(DELETE_FROM_USER_LIST);
            preparedStatement.setInt(1, userId);
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
