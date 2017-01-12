package database.models;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by DW on 2017-01-12.
 */
@Data
@Builder
public class BadanieDTO {
    private Integer id;
    private Integer patientId;
    private Integer examineId;
    private Integer doctorId;
    private Timestamp data;
}
