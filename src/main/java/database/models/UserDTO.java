package database.models;

import database.services.DatabaseService;
import lombok.Builder;
import lombok.Data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static database.Queries.INSERT_NEW_USERS;

@Builder
@Data
public class UserDTO {
    private Integer id;
    private String name;
    private String surname;
    private String type;

    public void insert(DatabaseService databaseService, Integer id, String name, String surname, String type) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseService.getConnection().prepareStatement(INSERT_NEW_USERS);

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, surname);
            preparedStatement.setString(4, type);

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

    public void insert(DatabaseService databaseService, UserDTO userDTO) {
        insert(databaseService, userDTO.getId(), userDTO.getName(), userDTO.getSurname(), userDTO.getType());
    }
}
