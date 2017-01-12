package database.models;

import lombok.Builder;
import lombok.Data;

/**
 * Created by DW on 2017-01-12.
 */
@Data
@Builder
public class ReportContainerDTO {
    private Integer id;
    private Integer reportId;
    private Integer examineId;
}
