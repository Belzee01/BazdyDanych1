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
import sample.views.DoctorsListView;
import sample.views.ExamineListView;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class DoctorsListController implements Initializable{
    private static Logger logger = Logger.getLogger(DoctorsListController.class);

    private DatabaseService databaseService = null;

    private DatabaseController databaseController = null;

    @FXML
    private TableView<DoctorsListView> tableView;

    @FXML
    private Button addNewBtn;

    @FXML
    private Button backBtn;

    private ObservableList<DoctorsListView> data = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseService = new DatabaseService();
        databaseController = new DatabaseController(databaseService);

        databaseService.connectToDb();

        initializeTableView();

        backBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("adminMain.fxml"), databaseService);
        });
    }

    private void initializeTableView() {
        TableColumn<DoctorsListView, String> name = new TableColumn<>("Imię");
        TableColumn<DoctorsListView, Integer> surname = new TableColumn<>("Nazwisko");
        TableColumn action = new TableColumn("Action");

        name.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        surname.setCellValueFactory(
                new PropertyValueFactory<>("surname")
        );

        action.setCellValueFactory( new PropertyValueFactory<>( "DUMMY" ) );

        Callback<TableColumn<DoctorsListView, String>, TableCell<DoctorsListView, String>> cellFactory =
                new Callback<TableColumn<DoctorsListView, String>, TableCell<DoctorsListView, String>>()
                {
                    @Override
                    public TableCell<DoctorsListView, String> call(TableColumn<DoctorsListView, String> param) {
                        {
                            final TableCell<DoctorsListView, String> cell = new TableCell<DoctorsListView, String>() {

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
                                            DoctorsListView examine = getTableView().getItems().get(getIndex());

                                            databaseController.deleteFromDoctorsList(examine.getId());

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

        data = databaseController.selectAllDoctors();

        tableView.setItems(data);
        tableView.getColumns().addAll(name, surname, action);
    }
}

