package sample.controllers;

import database.DatabaseController;
import database.dataloader.DataLoader;
import database.exceptions.DatabaseException;
import database.services.DatabaseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class Controller implements Initializable {
    @FXML
    public Button loginBtn;

    @FXML
    public Button registerBtn;

    @FXML
    public Button loaderBtn;

    @FXML
    public TextField loginText;

    @FXML
    TextField passwordText;

    private static Logger logger = Logger.getLogger(Controller.class);

    private DatabaseService databaseService = null;

    private DatabaseController databaseController = null;

    private DataLoader dataLoader = null;

    @FXML
    public void handleRegisterClick(ActionEvent event) {
        Parent mainPage = null;
        Scene scene = null;
        logger.info("Inside Controller");
        try {
            mainPage = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
            scene = new Scene(mainPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        databaseService = new DatabaseService();
        databaseController = new DatabaseController(databaseService);
        dataLoader = new DataLoader(databaseService);

        databaseService.connectToDb();

        loginBtn.setOnAction(event -> {
            try {
                if (databaseController.checkCredentials(loginText.getText(), passwordText.getText())) {
                    changeSceneContext(event, getClass().getClassLoader().getResource("adminMain.fxml"));
                } else {
                    changeSceneContext(event, getClass().getClassLoader().getResource("userMain.fxml"));
                }
            } catch (DatabaseException e) {
                e.printStackTrace();
            } finally {
                databaseService.closeConnection();
            }
        });

        loaderBtn.setOnAction(event -> {
            dataLoader.putMockDataInDatabase();
        });
    }


}
