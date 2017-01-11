package sample.views;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Created by Kajetan on 2016-12-31.
 */
@Data
public class ExamineListView {
    private Integer id;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty prise;
    private final SimpleIntegerProperty time;

    public ExamineListView(Integer id, String name, Integer prise, Integer time) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.prise = new SimpleIntegerProperty(prise);
        this.time = new SimpleIntegerProperty(time);
    }

    @Override
    public String toString() {
        return getName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getPrise() {
        return prise.get();
    }

    public SimpleIntegerProperty priseProperty() {
        return prise;
    }

    public void setPrise(int prise) {
        this.prise.set(prise);
    }

    public int getTime() {
        return time.get();
    }

    public SimpleIntegerProperty timeProperty() {
        return time;
    }

    public void setTime(int time) {
        this.time.set(time);
    }
}
