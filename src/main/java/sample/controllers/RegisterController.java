package sample.controllers;

import database.DatabaseController;
import database.services.DatabaseService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class RegisterController implements Initializable {

    @FXML
    private Button backBtn;

    @FXML
    private Button submitBtn;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseService = new DatabaseService();
        databaseController = new DatabaseController(databaseService);
        
        submitBtn.setOnAction((event) -> {
            databaseService.connectToDb();

            databaseController.insertNewUser(nameText.getText(), surnameText.getText());
            databaseController.insertNewCredentials(loginText.getText(), passwordText.getText(), surnameText.getText());

            changeSceneContext(event, getClass().getClassLoader().getResource("sample.fxml"), databaseService);
        });
    }

    @FXML
    public void onBackBtnClick(ActionEvent event) {
        changeSceneContext(event, getClass().getClassLoader().getResource("sample.fxml"), databaseService);
    }
}
