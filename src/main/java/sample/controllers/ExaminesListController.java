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

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class ExaminesListController implements Initializable{
    private static Logger logger = Logger.getLogger(ExaminesListController.class);

    private DatabaseService databaseService = null;

    private DatabaseController databaseController = null;

    @FXML
    private TableView<ExamineListView> tableView;

    @FXML
    private Button addNewBtn;

    @FXML
    private Button backBtn;

    private ObservableList<ExamineListView> data = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseService = new DatabaseService();
        databaseController = new DatabaseController(databaseService);

        databaseService.connectToDb();

        initializeTableView();

        backBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("adminMain.fxml"), databaseService);
        });

        addNewBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("forms/examineAddForm.fxml"), databaseService);
        });
    }

    private void initializeTableView() {
        TableColumn<ExamineListView, String> name = new TableColumn<>("Nazwa badania");
        TableColumn<ExamineListView, Integer> prise = new TableColumn<>("Cena badania [zł]");
        TableColumn<ExamineListView, Integer> time = new TableColumn<>("Czas badania [minuty]");
        TableColumn action = new TableColumn("Action");

        name.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        prise.setCellValueFactory(
                new PropertyValueFactory<>("prise")
        );
        time.setCellValueFactory(
                new PropertyValueFactory<>("time")
        );

        action.setCellValueFactory( new PropertyValueFactory<>( "DUMMY" ) );

        Callback<TableColumn<ExamineListView, String>, TableCell<ExamineListView, String>> cellFactory =
                new Callback<TableColumn<ExamineListView, String>, TableCell<ExamineListView, String>>()
                {
                    @Override
                    public TableCell<ExamineListView, String> call(TableColumn<ExamineListView, String> param) {
                        {
                            final TableCell<ExamineListView, String> cell = new TableCell<ExamineListView, String>() {

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
                                            ExamineListView examine = getTableView().getItems().get(getIndex());

                                            databaseController.deleteFromExamineList(examine.getId());

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

        data = databaseController.selectAllExamines();

        tableView.setItems(data);
        tableView.getColumns().addAll(name, prise, time, action);
    }
}
