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

public class DoctorsAddForm extends ParentForm implements Initializable {

    private static Logger logger = Logger.getLogger(DoctorsAddForm.class);

    @FXML
    private Button saveBtn;

    @FXML
    private Button abortBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveBtn.setOnAction(event -> {
            saveNewDoctorInDB(nameField.getText(), surnameField.getText());
            changeSceneContext(event, getClass().getClassLoader().getResource("doctorsList.fxml"), databaseService);
        });

        abortBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("doctorsList.fxml"), databaseService);
        });
    }

    private void saveNewDoctorInDB(String name, String surname) {
        databaseController.insertNewDoctor(name, surname);
    }
}
