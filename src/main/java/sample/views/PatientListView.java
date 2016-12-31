package sample.views;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

@Data
public class PatientListView {
    private Integer id;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleIntegerProperty company;

    public PatientListView(Integer id, String name, String surname, Integer company) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.company = new SimpleIntegerProperty(company);
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

    public int getCompany() {
        return company.get();
    }

    public SimpleIntegerProperty companyProperty() {
        return company;
    }

    public void setCompany(int company) {
        this.company.set(company);
    }
}
