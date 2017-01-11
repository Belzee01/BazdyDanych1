package database;

import database.exceptions.DatabaseException;
import database.services.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import sample.views.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static database.Queries.AdminUser.*;
import static database.Queries.*;

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

    public void insertNewUser(String name, String surname, String login, String password) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_USER_NEXT_SEQ);

            logger.info("Executing insertNewUser");

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);

            preparedStatement.executeUpdate();

            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_USER_ID);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);

            resultSet = preparedStatement.executeQuery();

            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_HASLA_NEXT_SEQ);

            while (resultSet.next()) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                preparedStatement.setInt(3, resultSet.getInt("id"));
                preparedStatement.executeUpdate();
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
            while (resultSet.next()) {
                userId = resultSet.getInt("uzytkownicy_id");
            }
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_ADMIN);
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
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
            while (resultSet.next()) {
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
            while (resultSet.next()) {
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
            while (resultSet.next()) {
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
            while (resultSet.next()) {
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
            while (resultSet.next()) {
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
            while (resultSet.next()) {
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

    public void insertNewAdmin(String name, String surname, String login) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_USER_ID);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            resultSet = preparedStatement.executeQuery();
            Integer userId = null;

            while (resultSet.next()) {
                userId = resultSet.getInt("id");
            }

            if (userId == null)
                throw new DatabaseException("Nie znaleziono uzytkownika!");

            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_ADMIN);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (DatabaseException d) {
            logger.info(d.getMessage());
        } catch (SQLException e) {
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

    public void updateAdmin(String name, String surname, String login) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_USER_ID);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            resultSet = preparedStatement.executeQuery();
            Integer userId = null;

            while (resultSet.next()) {
                userId = resultSet.getInt("id");
            }

            if (userId == null)
                throw new DatabaseException("Nie znaleziono uzytkownika!");

            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_ADMIN);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (DatabaseException d) {
            logger.info(d.getMessage());
        } catch (SQLException e) {
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

    public void insertPatient(String name, String surname, String company, DoctorsListView doctor, List<ExamineListView> examines) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Integer companyId = null;
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_COMAPNY_ID);
            preparedStatement.setString(1, company);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                companyId = resultSet.getInt("id");
            }

            logger.info("Wybrano id firmy");

            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_PATIENT);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setInt(3, companyId);
            preparedStatement.executeUpdate();

            logger.info("Wprowadzono pacjenta");

            Integer patientId = null;
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_PATIENT);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, company);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                patientId = resultSet.getInt("id");
            }

            logger.info("Wybrano id pacjenta");

            Integer doctorId = null;
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_DOCTOR_ID);
            preparedStatement.setString(1, doctor.getName());
            preparedStatement.setString(2, doctor.getSurname());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                doctorId = resultSet.getInt("id");

            }
            logger.info("Wybrano id doktora");

            Integer finalPatientId = patientId;
            Integer finalDoctorId = doctorId;
            examines.forEach(e -> {
                PreparedStatement p = null;
                ResultSet r = null;
                try {
                    if(finalPatientId == null || finalDoctorId == null) {
                        throw new DatabaseException("Crashed!");
                    }
                    p = databaseService.getConnection().prepareStatement(INSERT_NEW_BADANIE);
                    p.setInt(1, finalPatientId);
                    p.setInt(2, e.getId());
                    p.setInt(3, finalDoctorId);
                    p.setDate(4, Date.valueOf(LocalDate.now()));
                    p.executeUpdate();
                    logger.info("Wprowadzono nowe badanie");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (DatabaseException d) {
            logger.info(d.getMessage());
        } catch (SQLException e) {
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
