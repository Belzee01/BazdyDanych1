package database.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CredentialsDTO {
    private Integer id;;
    private String login;
    private String password;
    private Integer user_id;
}
