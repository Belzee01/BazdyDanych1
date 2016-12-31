package database.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CompanyDTO {
    private Integer id;
    private String name;
    private String nip;
    private String address;
}
