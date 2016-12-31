package database.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdminDTO {
    private Integer id;
    private Integer user_id;
}
