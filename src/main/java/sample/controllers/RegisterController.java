package sample.controllers;

import database.DatabaseController;
import database.services.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import sample.ACCOUNT_TYPE;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class RegisterController implements Initializable {

    @FXML
    private Button backBtn;

    @FXML
    private Button submitBtn;

    @FXML
    private ComboBox typeCombo;

    @FXML
    private TextField nameText;

    @FXML
    private TextField surnameText;

    @FXML
    private TextField loginText;

    @FXML
    private TextField passwordText;

    @FXML
    private TextField passwordConfirmationText;

    private DatabaseService databaseService;

    private DatabaseController databaseController;

    private static Logger logger = Logger.getLogger(RegisterController.class);

    private ObservableList<ACCOUNT_TYPE> account_types = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseService = new DatabaseService();
        databaseController = new DatabaseController(databaseService);

        typeCombo.setOnAction(event -> {
            String type = typeCombo.getValue().toString();

            if (type.equals("Company")) {
                nameText.setPromptText("Nazwa firmy");
                surnameText.setPromptText("NIP firmy");
            } else {
                nameText.setPromptText("Imie");
                surnameText.setPromptText("Nazwisko");
            }
        });

        submitBtn.setOnAction((event) -> {
            databaseService.connectToDb();

            String type = typeCombo.getValue().toString();

            if (type.equals("Company")) {
                databaseController.insertNewUserAsCompany(nameText.getText(), surnameText.getText(), type);
                databaseController.insertNewCredentials(loginText.getText(), passwordText.getText(), surnameText.getText());
            } else {
                databaseController.insertNewUser(nameText.getText(), surnameText.getText(), type);
                databaseController.insertNewCredentials(loginText.getText(), passwordText.getText(), surnameText.getText());
            }
            changeSceneContext(event, getClass().getClassLoader().getResource("sample.fxml"), databaseService);
        });

        initializeType();
    }

    @FXML
    public void onBackBtnClick(ActionEvent event) {
        changeSceneContext(event, getClass().getClassLoader().getResource("sample.fxml"), databaseService);
    }

    private void initializeType() {
        account_types.addAll(ACCOUNT_TYPE.STANDARD, ACCOUNT_TYPE.COMPANY);

        typeCombo.setItems(account_types);
    }
}
