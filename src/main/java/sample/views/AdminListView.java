package sample.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import lombok.Builder;
import lombok.Data;

@Data
public class AdminListView {
    private Integer id;
    private final SimpleStringProperty login;
    private final SimpleStringProperty name;
    private final SimpleStringProperty surname;

    public AdminListView(Integer adminId, String login, String name, String surname) {
        this.id = adminId;
        this.login = new SimpleStringProperty(login);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
    }

    public String getLogin() {
        return login.get();
    }

    public String getName() {
        return name.get();
    }

    public String getSurname() {
        return surname.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }
}
