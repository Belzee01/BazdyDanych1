package sample.views;

import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

@Data
public class DoctorsListView {
    private Integer id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty surname;

    public DoctorsListView(Integer id, String name, String surname) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
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

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }
}
