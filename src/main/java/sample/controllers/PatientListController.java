package sample.controllers;

import database.DatabaseController;
import database.services.DatabaseService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import sample.views.ExamineListView;
import sample.views.PatientListView;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

/**
 * Created by Kajetan on 2016-12-02.
 */
public class PatientListController implements Initializable{
    private static Logger logger = Logger.getLogger(PatientListController.class);

    private DatabaseService databaseService = null;

    private DatabaseController databaseController = null;

    @FXML
    private TableView<PatientListView> tableView;

    @FXML
    private Button addNewBtn;

    @FXML
    private Button backBtn;

    private ObservableList<PatientListView> data = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseService = new DatabaseService();
        databaseController = new DatabaseController(databaseService);

        databaseService.connectToDb();

        initializeTableView();

        backBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("adminMain.fxml"));
            databaseService.closeConnection();
        });
    }

    private void initializeTableView() {
        TableColumn<PatientListView, String> name = new TableColumn<>("Imie pacjenta");
        TableColumn<PatientListView, String> surname = new TableColumn<>("Nazwisko pacjenta");
        TableColumn<PatientListView, Integer> company = new TableColumn<>("Firma pacjenta");
        TableColumn action = new TableColumn("Action");

        name.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        surname.setCellValueFactory(
                new PropertyValueFactory<>("surname")
        );
        company.setCellValueFactory(
                new PropertyValueFactory<>("company")
        );

        action.setCellValueFactory( new PropertyValueFactory<>( "DUMMY" ) );

        Callback<TableColumn<PatientListView, String>, TableCell<PatientListView, String>> cellFactory =
                new Callback<TableColumn<PatientListView, String>, TableCell<PatientListView, String>>()
                {
                    @Override
                    public TableCell<PatientListView, String> call(TableColumn<PatientListView, String> param) {
                        {
                            final TableCell<PatientListView, String> cell = new TableCell<PatientListView, String>() {

                                final Button btn = new Button("Remove");

                                @Override
                                public void updateItem(String item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (empty) {
                                        setGraphic(null);
                                        setText(null);
                                    } else {
                                        btn.setOnAction((ActionEvent event) ->
                                        {
                                            PatientListView patient = getTableView().getItems().get(getIndex());

                                            databaseController.deleteFromPatientList(patient.getId());

                                            data.remove(getIndex());
                                        });
                                        setGraphic(btn);
                                        setText(null);
                                    }
                                }
                            };
                            return cell;
                        }
                    }
                };

        action.setCellFactory(cellFactory);

        data = databaseController.selectAllPatients();

        tableView.setItems(data);
        tableView.getColumns().addAll(name, surname, company, action);
    }
}
