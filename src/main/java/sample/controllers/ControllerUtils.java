package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Kajetan on 2016-12-02.
 */
public class ControllerUtils {

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
