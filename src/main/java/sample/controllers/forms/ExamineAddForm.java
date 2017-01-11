package sample.controllers.forms;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class ExamineAddForm extends ParentForm implements Initializable {

    private static Logger logger = Logger.getLogger(ExamineAddForm.class);

    @FXML
    private Button saveBtn;

    @FXML
    private Button abortBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priseField;

    @FXML
    private TextField timeField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveBtn.setOnAction(event -> {
            saveNewExamineInDB(nameField.getText(), priseField.getText(), timeField.getText());
            changeSceneContext(event, getClass().getClassLoader().getResource("examinesList.fxml"), databaseService);
        });

        abortBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("examinesList.fxml"), databaseService);
        });
    }

    private void saveNewExamineInDB(String name, String prise, String time) {
        databaseController.insertNewExamine(name, prise, time);
    }
}
