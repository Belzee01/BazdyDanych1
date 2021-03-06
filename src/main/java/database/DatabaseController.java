package database;

import database.exceptions.DatabaseException;
import database.services.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import sample.controllers.forms.ErrorForm;
import sample.views.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static database.Queries.*;
import static database.Queries.AdminUser.*;

public class DatabaseController {

    private static Logger logger = Logger.getLogger(DatabaseController.class);

    /**
     * Used to create, open, close connection to database
     */
    private DatabaseService databaseService = null;

    /**
     * Dependency injection. Sets DatabaseService instance used to create connection to database
     *
     * @param databaseService Instance of DatabaseService
     */
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Inserts new user to database
     *
     * @param name    Name of user that we want to save in DB
     * @param surname Surname of user
     * @param type    Type of user. Allows Standard or Company
     * @throws DatabaseException Invalid data causes exception to be thrown
     */
    public void insertNewUser(String name, String surname, String type) throws DatabaseException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_USER);

            logger.info("Executing insertNewUser");

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, type);

            preparedStatement.executeUpdate();

            logger.info("New user inserted");
        } catch (SQLException e) {
            e.printStackTrace();
            databaseService.cleanUpConnections();
            throw new DatabaseException(e.getMessage());
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

    /**
     * Retrieves all patients related to generated report based on reportId and comapnyId
     *
     * @param companyId Identifies company that patients will be selected for
     * @param reportId  Identifies report that all examines are related to
     * @return Gets list of patients with examine name and prise
     */
    public ObservableList<ReportCompanyListView> selectPatientsForReport(Integer companyId, Integer reportId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<ReportCompanyListView> data = FXCollections.observableArrayList();
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_PATIENTS_BY_RAPORT_ID_AND_COMPANY_ID);
            preparedStatement.setInt(1, companyId);
            preparedStatement.setInt(2, reportId);

            resultSet = preparedStatement.executeQuery();

            try {
                while (resultSet.next()) {
                    data.add(
                            new ReportCompanyListView(
                                    resultSet.getString("imie"),
                                    resultSet.getString("nazwisko"),
                                    resultSet.getString("nazwa"),
                                    resultSet.getInt("cena")
                            )
                    );
                }
            } catch (SQLException e) {
                throw new DatabaseException("Nie utworzono uzytkownika, blad krytyczny!");
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
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

    /**
     * Inserts new user with Company type
     *
     * @param name    Company name
     * @param surname Company nip
     * @param type    Company type
     * @throws DatabaseException Trying to insert duplicate data causes exception
     */
    public void insertNewUserAsCompany(String name, String surname, String type) throws DatabaseException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            databaseService.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_COMAPNY_ID);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                throw new DatabaseException("Firma o podanych danych nie istnieje!");

            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_USER);

            logger.info("Executing insertNewUser");

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, type);

            preparedStatement.executeUpdate();

            logger.info("New user inserted");
        } catch (SQLException e) {
            logger.info(e.getMessage());

            ErrorForm.showError("Error", e.getMessage());

            databaseService.cleanUpConnections();
            throw new DatabaseException(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                databaseService.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retries newly created user identifier
     *
     * @param surname Surname of searched user
     * @return Returns user identifier
     * @throws DatabaseException If user based on surname not found exception thrown
     */
    public Integer selectNewUserId(String surname) throws DatabaseException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer userId = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_NEW_USER_ID);

            preparedStatement.setString(1, surname);

            resultSet = preparedStatement.executeQuery();

            try {
                while (resultSet.next()) {
                    userId = resultSet.getInt("id");
                }
            } catch (SQLException e) {
                throw new DatabaseException("Nie utworzono uzytkownika, blad krytyczny!");
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
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
        return userId;
    }

    /**
     * Inserts new record to hasla table
     *
     * @param login    Login for newly created user
     * @param password Password for newly created user
     * @param selector newly created user identifier
     */
    public void insertNewCredentials(String login, String password, String selector) {
        PreparedStatement preparedStatement = null;
        Integer userId = null;
        databaseService.setAutoCommit(false);
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_HASLA);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            userId = selectNewUserId(selector);

            if (userId == null)
                throw new DatabaseException("Nie znalezniono uzytkownika");

            preparedStatement.setInt(3, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());

            ErrorForm.showError("Constraint Error", e.getMessage());

            databaseService.cleanUpConnections();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            databaseService.setAutoCommit(true);
        }
    }

    /**
     * Checks if given user exists
     *
     * @param login    User login
     * @param password user password
     * @throws DatabaseException If user with given credentials does not exists then exception is thrown
     */
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

    /**
     * Checks if user exists based on login
     *
     * @param login User login
     * @throws DatabaseException If user already exists then exception is thrown
     */
    public void checkCredentials(String login) throws DatabaseException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean authen = false;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_GIVEN_CREDENTIALS_BY_LOGIN);

            preparedStatement.setString(1, login);

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
        if (authen)
            throw new DatabaseException("ERROR - Uzytkownik juz istnieje");
    }

    /**
     * Checks if user is admin
     *
     * @param login    User login
     * @param password user password
     * @return If user is on admin list then it will return true, otherwise user is non admin user and false is retrieved
     */
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

    /**
     * Checks account type based on login and password
     *
     * @param login    User login
     * @param password User password
     * @return Gets account type as String
     */
    public String checkType(String login, String password) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer userId = null;
        boolean authen = false;
        String accountType = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_GIVEN_CREDENTIALS);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userId = resultSet.getInt("uzytkownicy_id");
            }
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_USER_TYPE);
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                accountType = resultSet.getString("typ");
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

        return accountType;
    }

    /**
     * Retrieves all admins
     *
     * @return Admin list filled with AdminListView instances
     */
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

    /**
     * Removes given admin from admini table
     *
     * @param adminId Admin identifier - record is removed based on that parameter
     */
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

    /**
     * Retrieves all examines from badania table
     *
     * @return Examine list filled with ExamineListView instances
     */
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

    /**
     * Removes examine from badania table identified by examineId
     *
     * @param examineId Examine identifier that we want to remove
     */
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

    /**
     * Retrieves list of all doctors from lekarze table
     *
     * @return List of DoctorsListView instances
     */
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

    /**
     * Removes from lekarze table based on passed doctorId
     *
     * @param doctorId Selected doctor identifier
     */
    public void deleteFromDoctorsList(int doctorId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(DELETE_FROM_DOCTORS_LIST);
            preparedStatement.setInt(1, doctorId);
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

    /**
     * Retrieves all companies
     *
     * @return List of CompanyListView instances
     */
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

    /**
     * Removes from firmy table based on passed company identifier
     *
     * @param companyId Selected company
     * @throws DatabaseException Exception uis thrown if there is foreign key violation. Company cannot be removed if there are still registered patients
     */
    public void deleteFromCompanyList(int companyId) throws DatabaseException {
        PreparedStatement preparedStatement = null;
        databaseService.setAutoCommit(false);
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(DELETE_FROM_COMPANY_LIST);
            preparedStatement.setInt(1, companyId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.info("Nie można usunąć firmy jeżeli są dla niej zarejestrowni pacjenci");
            databaseService.cleanUpConnections();
            throw new DatabaseException("Nie można usunąć firmy jeżeli są dla niej zarejestrowni pacjenci");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            databaseService.setAutoCommit(true);
        }
    }

    /**
     * Retrieves all patients
     *
     * @return List of PatientLisView instances
     */
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

    /**
     * Retrieves all reports for specified company
     * @param companyId Company identifier
     * @return List of ReportListViewInstances based on companyId
     */
    public ObservableList<ReportListView> selectAllReports(Integer companyId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<ReportListView> data = FXCollections.observableArrayList();
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_ALL_REPORTS_FOR_COMPANY);
            preparedStatement.setInt(1, companyId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                data.add(
                        new ReportListView(
                                resultSet.getInt("id"),
                                resultSet.getTimestamp("data"),
                                resultSet.getInt("firmy_id")
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

    /**
     * Calculates sum of all examines within given report for given comapny
     * @param companyId Company identifier. Reports are selected based on this selector
     * @param reportId Report identifier. Sum is calculated for specific report
     * @return Integer value as sum of all examines within report
     */
    public Integer selectSumForCompany(Integer companyId, Integer reportId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer sum = 0;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_SUM_FOR_PATIENTS_COMPANY);
            preparedStatement.setInt(1, companyId);
            preparedStatement.setInt(2, reportId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sum = resultSet.getInt("sum");
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
        return sum;
    }

    /**
     * Adds new report into raporty table. Report is added for specific company
     * @param companyId Company identifier. Report is added for specified company
     */
    public void saveNewReportInDB(Integer companyId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(AdminUser.INSERT_NEW_REPORT);
            preparedStatement.setInt(1, companyId);
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

    /**
     * Removes patient from pacjent table based on passed patient id
     * @param patientId Patient identifier
     */
    public void deleteFromPatientList(int patientId) throws DatabaseException {
        PreparedStatement preparedStatement = null;
        databaseService.setAutoCommit(false);
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(DELETE_FROM_PATIENT_LIST);
            preparedStatement.setInt(1, patientId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            databaseService.cleanUpConnections();
            throw new DatabaseException("Nie można usunąć archiwalnego badania. Tylko jeszcze nie zaraportowani pacjenci mogą zostać usunięci!");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            databaseService.setAutoCommit(true);
        }
    }

    /**
     * Retrieves all users from uzytkownicy table
     * @return Returns list of UserListView instances
     */
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
                                resultSet.getString("login"),
                                resultSet.getString("typ")
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

    /**
     * Removes user from uzytkownicy table based on passed user identifier
     * @param userId User identifier
     */
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

    /**
     * Adds new admin to admini table
     * @param name User name
     * @param surname User surname
     * @param login User login
     * @throws DatabaseException If passed user data is invalid then exception will be thrown
     */
    public void insertNewAdmin(String name, String surname, String login) throws DatabaseException {
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
            throw new DatabaseException(e.getMessage());
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

    /**
     * Adds new patient and examines related to him based on company identifier
     * @param name Patient name
     * @param surname Patient surname
     * @param company Company name
     * @param doctor Slected doctor
     * @param examines Selected list of examines for ptient
     * @throws DatabaseException If company name is invalid then exception will be thrown
     */
    public void insertPatient(String name, String surname, String company, DoctorsListView doctor, List<ExamineListView> examines) throws DatabaseException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        databaseService.setAutoCommit(false);
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
            if (companyId == null)
                throw new DatabaseException("Nie poprawna nazwa firmy!");
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
                    if (finalPatientId == null || finalDoctorId == null) {
                        throw new DatabaseException("Crashed!");
                    }
                    p = databaseService.getConnection().prepareStatement(INSERT_NEW_BADANIE);
                    p.setInt(1, finalPatientId);
                    p.setInt(2, e.getId());
                    p.setInt(3, finalDoctorId);
                    p.executeUpdate();
                    logger.info("Wprowadzono nowe badanie");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (SQLException e) {
            databaseService.cleanUpConnections();
            throw new DatabaseException(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            databaseService.setAutoCommit(true);
        }
    }

    public void insertNewExamine(String name, String prise, String time) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_EXAMINES);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, Integer.valueOf(prise));
            preparedStatement.setInt(3, Integer.valueOf(time));

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

    public void insertNewDoctor(String name, String surname) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_DOCTORS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);

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

    public void insertNewCompany(String name, String nip, String adres) throws DatabaseException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        databaseService.setAutoCommit(false);
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_COMPANY);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, nip);
            preparedStatement.setString(3, adres);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            databaseService.cleanUpConnections();
            throw new DatabaseException(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            databaseService.setAutoCommit(true);
        }
    }

    public Integer selectCompanyIdFromLogin(String name) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer companyId = null;
        Integer userId = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_HASLA_USER_ID_BY_LOGIN);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userId = resultSet.getInt("uzytkownicy_id");
            }

            preparedStatement = databaseService.getConnection().prepareStatement(SELECT_COMPANY_ID_WHERE_NAME_EQUAL_USER_NAME);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                companyId = resultSet.getInt("id");
            }
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
        return companyId;
    }
}
