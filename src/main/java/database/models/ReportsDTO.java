package database.models;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ReportsDTO {
    private Integer id;
    private Timestamp data;
    private Integer firmId;
}
