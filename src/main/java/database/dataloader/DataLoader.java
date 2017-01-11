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
                    AdminDTO.builder().user_id(1).build(),
                    AdminDTO.builder().user_id(3).build()
            };

    final UserDTO[] users =
            {
                    UserDTO.builder().name("test1").surname("surnametest1").type("Standard").build(),
                    UserDTO.builder().name("test2").surname("surnametest2").type("Standard").build(),
                    UserDTO.builder().name("test3").surname("surnametest3").type("Standard").build()
            };

    final CredentialsDTO[] hasla =
            {
                    CredentialsDTO.builder().login("admin").password("admin").user_id(1).build(),
                    CredentialsDTO.builder().login("user").password("user").user_id(2).build(),
                    CredentialsDTO.builder().login("testLogin3").password("testPassword3").user_id(3).build()
            };

    final ExaminesDTO[] examinesDTOS =
            {
                    ExaminesDTO.builder().name("Badanie ucha").prise(10).time(1).build(),
                    ExaminesDTO.builder().name("Badanie brzucha").prise(20).time(2).build(),
                    ExaminesDTO.builder().name("Badanie płuc").prise(10).time(3).build()
            };

    final DoctorsDTO[] doctorsDTOS =
            {
                    DoctorsDTO.builder().name("Adam").surname("Kowal").build(),
                    DoctorsDTO.builder().name("Robert").surname("Kowalski").build(),
                    DoctorsDTO.builder().name("Szymon").surname("Nowak").build()
            };

    final CompanyDTO[] companyDTOS =
            {
                    CompanyDTO.builder().name("Firma 1").nip("NIP 1").address("Adres 1").build(),
                    CompanyDTO.builder().name("Firma 2").nip("NIP 2").address("Adres 2").build(),
                    CompanyDTO.builder().name("Firma 3").nip("NIP 3").address("Adres 3").build()
            };

    final PatientDTO[] patientDTOS =
            {
                    PatientDTO.builder().id(1).name("Imie 1").surname("Nazwisko 1").companyId(1).build(),
                    PatientDTO.builder().id(2).name("Imie 2").surname("Nazwisko 2").companyId(2).build(),
                    PatientDTO.builder().id(3).name("Imie 3").surname("Nazwisko 3").companyId(3).build()
            };

    final String[] sequences = {
        "uzytkownicy_id_seq", "admini_id_seq", "badania_id_seq", "badanie_id_seq", "firmy_id_seq", "hasla_id_seq", "lekarze_id_seq", "pacjent_id_seq", "raporty_id_seq"
    };

    public void cleanUpDatabase() {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(CLEAN_UP_ALL_TABLES);

            preparedStatement.executeUpdate();

            Arrays.stream(sequences).forEach(s -> {
                PreparedStatement seq = null;
                try {
                    seq = databaseService.getConnection().prepareStatement(String.format("ALTER SEQUENCE %s RESTART WITH 1;", s));
                    seq.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
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

                preparedStatement.setString(1, u.getName());
                preparedStatement.setString(2, u.getSurname());
                preparedStatement.setString(3, u.getType());

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

                preparedStatement.setString(1, u.getLogin());
                preparedStatement.setString(2, u.getPassword());
                preparedStatement.setInt(3, u.getUser_id());

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

                preparedStatement.setInt(1, u.getUser_id());


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

                preparedStatement.setString(1, e.getName());
                preparedStatement.setInt(2, e.getPrise());
                preparedStatement.setInt(3, e.getTime());

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

                preparedStatement.setString(1, d.getName());
                preparedStatement.setString(2, d.getSurname());

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

                preparedStatement.setString(1, c.getName());
                preparedStatement.setString(2, c.getNip());
                preparedStatement.setString(3, c.getAddress());

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

                preparedStatement.setString(1, p.getName());
                preparedStatement.setString(2, p.getSurname());
                preparedStatement.setInt(3, p.getCompanyId());

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
