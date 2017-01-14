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
            try {
                saveNewCompanyInDB(nameField.getText(), nipField.getText(), adresField.getText());
            } catch (DatabaseException e) {
                ErrorForm.showError("Error", e.getMessage());
                return;
            }
            changeSceneContext(event, getClass().getClassLoader().getResource("companyList.fxml"), databaseService);
        });

        abortBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("companyList.fxml"), databaseService);
        });
    }

    /**
     * Inserts new company into firmy table in databse
     * @param name Comapny name
     * @param surname Company nip
     * @param login Company address
     * @throws DatabaseException In case of invliad data exception will be thrown
     */
    private void saveNewCompanyInDB(String name, String surname, String login) throws DatabaseException {
            databaseController.insertNewCompany(name, surname, login);
    }
}
