package sample.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class UserMainController implements Initializable{

    @FXML
    public Button examinesBtn;

    @FXML
    public Button pacientsBtn;

    @FXML
    public Button comapniesBtn;

    @FXML
    public Button doctorsBtn;

    @FXML
    public Button exitBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exitBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("sample.fxml"));
        });

        comapniesBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("companyList.fxml"));
        });

        doctorsBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("doctorsList.fxml"));
        });

        pacientsBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("patientList.fxml"));
        });

        examinesBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("examinesList.fxml"));
        });
    }
}
