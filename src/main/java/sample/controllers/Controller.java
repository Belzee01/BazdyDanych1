package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public Button loginBtn;

    @FXML
    public Button registerBtn;

    private static Logger logger = Logger.getLogger(Controller.class);

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

    @FXML
    public void handleLoginClick(ActionEvent event) {
//        Parent mainPage = null;
//        Scene scene = null;
//        try {
//            mainPage = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
//            scene = new Scene(mainPage);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(scene);
//        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
