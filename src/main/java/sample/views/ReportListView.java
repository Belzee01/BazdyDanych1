package sample.views;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by DW on 2017-01-13.
 */
@Data
public class ReportListView {
    private Integer id;
    private Timestamp data;
    private Integer comapnyId;

    public ReportListView(Integer id, Timestamp data, Integer companyId) {
        this.id = id;
        this.data = data;
        this.comapnyId = companyId;
    }
}
