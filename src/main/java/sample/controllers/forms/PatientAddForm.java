package sample.controllers.forms;

import database.DatabaseController;
import database.services.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import sample.views.AdminListView;
import sample.views.DoctorsListView;
import sample.views.ExamineListView;
import sample.views.UserListView;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class PatientAddForm extends ParentForm implements Initializable {

    private static Logger logger = Logger.getLogger(PatientAddForm.class);

    @FXML
    private Button saveBtn;

    @FXML
    private Button abortBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField companyField;

    @FXML
    private ComboBox examineCombo;

    @FXML
    private ComboBox doctorCombo;

    @FXML
    private TableView examineTable;

    private ObservableList<ExamineListView> data;

    private ObservableList<ExamineListView> options;

    private ObservableList<DoctorsListView> doctors;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveBtn.setOnAction(event -> {
            saveNewpatientInDB(nameField.getText(), surnameField.getText(), companyField.getText(), (DoctorsListView) doctorCombo.getValue(), data);
            changeSceneContext(event, getClass().getClassLoader().getResource("patientList.fxml"), databaseService);
        });

        abortBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("patientList.fxml"), databaseService);
        });

        examineCombo.setOnAction(event -> {
            if (examineCombo.getValue() != null) {
                ExamineListView view = (ExamineListView) examineCombo.getValue();
                data.add(view);
            }
        });

        initializeComboBox();

        initializeTableView();
    }

    private void saveNewpatientInDB(String name, String surname, String company, DoctorsListView doctor, List<ExamineListView> examines) {
        databaseController.insertPatient(name, surname, company, doctor, examines);
    }

    private void initializeComboBox() {
        options = databaseController.selectAllExamines();

        doctors = databaseController.selectAllDoctors();

        examineCombo.setItems(options);
        doctorCombo.setItems(doctors);
    }

    private void initializeTableView() {
        data = FXCollections.observableArrayList();

        TableColumn<ExamineListView, String> name = new TableColumn<>("Nazwa badania");
        TableColumn<ExamineListView, Integer> prise = new TableColumn<>("Cena badania [zł]");
        TableColumn<ExamineListView, Integer> time = new TableColumn<>("Czas badania [minuty]");

        name.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        prise.setCellValueFactory(
                new PropertyValueFactory<>("prise")
        );
        time.setCellValueFactory(
                new PropertyValueFactory<>("time")
        );

        examineTable.setItems(data);
        examineTable.getColumns().addAll(name, prise, time);
    }
}
