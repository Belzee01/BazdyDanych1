package database.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExaminesDTO {
    private Integer id;
    private String name;
    private Integer prise;
    private Integer time;

}
