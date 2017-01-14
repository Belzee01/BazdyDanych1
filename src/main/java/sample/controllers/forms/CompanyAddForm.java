package sample.controllers.forms;

import database.DatabaseController;
import database.exceptions.DatabaseException;
import database.services.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class CompanyAddForm extends ParentForm implements Initializable {

    private static Logger logger = Logger.getLogger(CompanyAddForm.class);

    @FXML
    private Button saveBtn;

    @FXML
    private Button abortBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField nipField;

    @FXML
    private TextField adresField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveBtn.setOnAction(event -> {
            saveNewCompanyInDB(nameField.getText(), nipField.getText(), adresField.getText());
            changeSceneContext(event, getClass().getClassLoader().getResource("companyList.fxml"), databaseService);
        });

        abortBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("companyList.fxml"), databaseService);
        });
    }

    private void saveNewCompanyInDB(String name, String surname, String login) {
        try {
            databaseController.insertNewCompany(name, surname, login);
        } catch (DatabaseException e) {
            ErrorForm.showError("Error", e.getMessage());
        }
    }
}
