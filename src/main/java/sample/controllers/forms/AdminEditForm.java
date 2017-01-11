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

public class AdminEditForm extends ParentForm implements Initializable {

    private static Logger logger = Logger.getLogger(AdminEditForm.class);

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveBtn.setOnAction(event -> {
            saveNewAdminInDB(nameField.getText(), surnameField.getText(), loginField.getText());
            changeSceneContext(event, getClass().getClassLoader().getResource("adminList.fxml"), databaseService);
        });

        abortBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("adminList.fxml"), databaseService);
        });
    }

    private void saveNewAdminInDB(String name, String surname, String login) {
        databaseController.updateAdmin(name, surname, login);
    }
}
