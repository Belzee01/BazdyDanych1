package database.dataloader;

import database.models.*;
import database.services.DatabaseService;
import lombok.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static database.Queries.*;
import static database.Queries.DataLoaderQueries.CLEAN_UP_ALL_TABLES;

@Data
public class DataLoader {
    private List<CompanyDTO> companyDTOS = new ArrayList<>();
    private List<CredentialsDTO> credentialsDTOS = new ArrayList<>();
    private List<DoctorsDTO> doctorsDTOS = new ArrayList<>();
    private List<ExaminesDTO> examinesDTOS = new ArrayList<>();
    private List<ReportsDTO> reportsDTOS = new ArrayList<>();

    DatabaseService databaseService = null;

    public DataLoader(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    final AdminDTO[] admins =
            {
                    AdminDTO.builder().id(1).user_id(1).build(),
                    AdminDTO.builder().id(2).user_id(3).build()
            };

    final UserDTO[] users =
            {
                    UserDTO.builder().id(1).name("test1").surname("surnametest1").build(),
                    UserDTO.builder().id(2).name("test2").surname("surnametest2").build(),
                    UserDTO.builder().id(3).name("test3").surname("surnametest3").build()
            };

    final CredentialsDTO[] hasla =
            {
                    CredentialsDTO.builder().id(1).login("admin").password("admin").user_id(1).build(),
                    CredentialsDTO.builder().id(2).login("testLogin2").password("testPassword2").user_id(2).build(),
                    CredentialsDTO.builder().id(3).login("testLogin3").password("testPassword3").user_id(3).build()
            };

    public void cleanUpDatabase() {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(CLEAN_UP_ALL_TABLES);

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

    public void putMockDataInDatabase() {
        cleanUpDatabase();

        Arrays.stream(users).forEach(u -> {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_USERS);

                preparedStatement.setInt(1, u.getId());
                preparedStatement.setString(2, u.getName());
                preparedStatement.setString(3, u.getSurname());

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
        });

        Arrays.stream(hasla).forEach(u -> {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_HASLAS);

                preparedStatement.setInt(1, u.getId());
                preparedStatement.setString(2, u.getLogin());
                preparedStatement.setString(3, u.getPassword());
                preparedStatement.setInt(4, u.getUser_id());

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
        });

        Arrays.stream(admins).forEach(u -> {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_ADMINS);

                preparedStatement.setInt(1, u.getId());
                preparedStatement.setInt(2, u.getUser_id());


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
        });
    }
}
