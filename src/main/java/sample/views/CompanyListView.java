package sample.views;

import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

@Data
public class CompanyListView {
    private Integer id;
    private SimpleStringProperty name;
    private SimpleStringProperty nip;
    private SimpleStringProperty address;

    public CompanyListView(Integer id, String name, String nip, String address) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.nip = new SimpleStringProperty(nip);
        this.address = new SimpleStringProperty(address);
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

    public String getNip() {
        return nip.get();
    }

    public SimpleStringProperty nipProperty() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip.set(nip);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }
}
