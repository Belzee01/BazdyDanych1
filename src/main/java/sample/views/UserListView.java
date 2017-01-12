package sample.views;

import javafx.beans.property.SimpleStringProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class UserListView {
    private Integer id;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty login;
    private SimpleStringProperty type;

    public UserListView(Integer id, String name, String surname, String login, String type) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.login = new SimpleStringProperty(login);
        this.type = new SimpleStringProperty(type);
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
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
