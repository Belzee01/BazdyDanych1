package sample.controllers;

import database.services.DatabaseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ControllerUtils {

    /**
     * Changes stage based on URL we pass
     * @param event Event that caused method to be invoked
     * @param url URL to specific FXML file that describes new stage
     * @param databaseService Instance of DatabaseService. After changing stage connection to database is closed
     */
    public static void changeSceneContext(ActionEvent event, URL url, DatabaseService databaseService) {
        databaseService.closeConnection();
        Parent mainPage = null;
        Scene scene = null;
        try {
            mainPage = FXMLLoader.load(url);
            scene = new Scene(mainPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Changes stage based on URL we pass
     * @param event Event that caused method to be invoked
     * @param url URL to specific FXML file that describes new stage
     * @param databaseService Instance of DatabaseService. After changing stage connection to database is closed
     */
    public static void changeSceneContext(MouseEvent event, URL url, DatabaseService databaseService) {
        databaseService.closeConnection();
        Parent mainPage = null;
        Scene scene = null;
        try {
            mainPage = FXMLLoader.load(url);
            scene = new Scene(mainPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Changes stage based on URL we pass
     * @param event Event that caused method to be invoked
     * @param url URL to specific FXML file that describes new stage
     */
    public static void changeSceneContext(ActionEvent event, URL url) {
        Parent mainPage = null;
        Scene scene = null;
        try {
            mainPage = FXMLLoader.load(url);
            scene = new Scene(mainPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
