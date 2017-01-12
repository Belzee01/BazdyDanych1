package sample.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class CompanyMainController implements Initializable{

    @FXML
    public Button reportsBtn;

    @FXML
    public Button examinesBtn;

    @FXML
    public Button doctorsBtn;

    @FXML
    public Button exitBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exitBtn.setOnAction(event -> {
            Platform.exit();
        });

        doctorsBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("doctorsList.fxml"));
        });

        examinesBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("examinesList.fxml"));
        });

        reportsBtn.setOnAction(event -> {

        });
    }
}
