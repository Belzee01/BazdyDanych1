package database.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DoctorsDTO {
    private Integer id;
    private String name;
    private String surname;
}
