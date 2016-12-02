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
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {

    @FXML
    private Button backBtn;

    @FXML
    private Button submitBtn;

    private DatabaseService databaseController;

    private static Logger logger = Logger.getLogger(RegisterController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseController = new DatabaseService();

        submitBtn.setOnAction((event) -> {
            databaseController.connectToDb();
            databaseController.closeConnection();
        });
    }

    @FXML
    public void onBackBtnClick(ActionEvent event) {
        Parent mainPage = null;
        Scene scene = null;
        try {
            mainPage = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
            scene = new Scene(mainPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
