package sample.views;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by DW on 2017-01-14.
 */
public class ReportCompanyListView {
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleIntegerProperty prise;

    public ReportCompanyListView(String name, String surname, Integer prise) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.prise = new SimpleIntegerProperty(prise);
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

    public int getPrise() {
        return prise.get();
    }

    public SimpleIntegerProperty priseProperty() {
        return prise;
    }

    public void setPrise(int prise) {
        this.prise.set(prise);
    }
}
