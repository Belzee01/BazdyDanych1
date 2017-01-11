package sample.controllers.forms;

import database.DatabaseController;
import database.services.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class UsersAddForm extends ParentForm implements Initializable {

    private static Logger logger = Logger.getLogger(UsersAddForm.class);

    @FXML
    private Button saveBtn;

    @FXML
    private Button abortBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveBtn.setOnAction(event -> {
            saveNewUser(nameField.getText(), surnameField.getText(), loginField.getText(), passwordField.getText());
            changeSceneContext(event, getClass().getClassLoader().getResource("userList.fxml"), databaseService);
        });

        abortBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("userList.fxml"), databaseService);
        });
    }

    private void saveNewUser(String name, String surname, String login, String password) {
        databaseController.insertNewUser(name, surname, login, password);
    }
}
