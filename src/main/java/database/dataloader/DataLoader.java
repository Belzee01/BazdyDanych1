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
                    CredentialsDTO.builder().id(2).login("user").password("user").user_id(2).build(),
                    CredentialsDTO.builder().id(3).login("testLogin3").password("testPassword3").user_id(3).build()
            };

    final ExaminesDTO[] examinesDTOS =
            {
                    ExaminesDTO.builder().id(1).name("Badanie ucha").prise(10).time(1).build(),
                    ExaminesDTO.builder().id(2).name("Badanie brzucha").prise(20).time(2).build(),
                    ExaminesDTO.builder().id(3).name("Badanie płuc").prise(10).time(3).build()
            };

    final DoctorsDTO[] doctorsDTOS =
            {
                    DoctorsDTO.builder().id(1).name("Adam").surname("Kowal").build(),
                    DoctorsDTO.builder().id(2).name("Robert").surname("Kowalski").build(),
                    DoctorsDTO.builder().id(3).name("Szymon").surname("Nowak").build()
            };

    final CompanyDTO[] companyDTOS =
            {
                    CompanyDTO.builder().id(1).name("Firma 1").nip("NIP 1").address("Adres 1").build(),
                    CompanyDTO.builder().id(2).name("Firma 2").nip("NIP 2").address("Adres 2").build(),
                    CompanyDTO.builder().id(3).name("Firma 3").nip("NIP 3").address("Adres 3").build()
            };

    final PatientDTO[] patientDTOS =
            {
                    PatientDTO.builder().id(1).name("Imie 1").surname("Nazwisko 1").companyId(1).build(),
                    PatientDTO.builder().id(2).name("Imie 2").surname("Nazwisko 2").companyId(2).build(),
                    PatientDTO.builder().id(3).name("Imie 3").surname("Nazwisko 3").companyId(3).build()
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

        Arrays.stream(examinesDTOS).forEach(e -> {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_EXAMINES);

                preparedStatement.setInt(1, e.getId());
                preparedStatement.setString(2, e.getName());
                preparedStatement.setInt(3, e.getPrise());
                preparedStatement.setInt(4, e.getTime());

                preparedStatement.executeUpdate();
            } catch (SQLException s) {
                s.printStackTrace();
                databaseService.cleanUpConnections();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException s) {
                        s.printStackTrace();
                    }
                }
            }
        });

        Arrays.stream(doctorsDTOS).forEach(d -> {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_DOCTORS);

                preparedStatement.setInt(1, d.getId());
                preparedStatement.setString(2, d.getName());
                preparedStatement.setString(3, d.getSurname());

                preparedStatement.executeUpdate();
            } catch (SQLException s) {
                s.printStackTrace();
                databaseService.cleanUpConnections();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException s) {
                        s.printStackTrace();
                    }
                }
            }
        });

        Arrays.stream(companyDTOS).forEach(c -> {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_COMPANY);

                preparedStatement.setInt(1, c.getId());
                preparedStatement.setString(2, c.getName());
                preparedStatement.setString(3, c.getNip());
                preparedStatement.setString(4, c.getAddress());

                preparedStatement.executeUpdate();
            } catch (SQLException s) {
                s.printStackTrace();
                databaseService.cleanUpConnections();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException s) {
                        s.printStackTrace();
                    }
                }
            }
        });

        Arrays.stream(patientDTOS).forEach(p -> {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_PATIENT);

                preparedStatement.setInt(1, p.getId());
                preparedStatement.setString(2, p.getName());
                preparedStatement.setString(3, p.getSurname());
                preparedStatement.setInt(4, p.getCompanyId());

                preparedStatement.executeUpdate();
            } catch (SQLException s) {
                s.printStackTrace();
                databaseService.cleanUpConnections();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException s) {
                        s.printStackTrace();
                    }
                }
            }
        });
    }
}
