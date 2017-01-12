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
import sample.ContextCatcher;
import sample.views.AdminListView;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class ReportController implements Initializable {

    private static Logger logger = Logger.getLogger(ReportController.class);

    private DatabaseService databaseService = null;

    private DatabaseController databaseController = null;

    @FXML
    private TableView tableView;

    @FXML
    private Button addNewBtn;

    @FXML
    private Button backBtn;

    private ObservableList<AdminListView> data = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseService = new DatabaseService();
        databaseController = new DatabaseController(databaseService);

        databaseService.connectToDb();

        initializeTableView();

        backBtn.setOnAction(event -> {
            switch (ContextCatcher.getAccountType()) {
                case ADMIN:
                    changeSceneContext(event, getClass().getClassLoader().getResource("adminMain.fxml"), databaseService);
                    break;

                case COMPANY:
                    changeSceneContext(event, getClass().getClassLoader().getResource("companyMain.fxml"), databaseService);
                    break;

                case STANDARD:
                    changeSceneContext(event, getClass().getClassLoader().getResource("userMain.fxml"), databaseService);
                    break;
            }
        });

        addNewBtn.setOnAction(event -> {
        });
    }

    private void initializeTableView() {
        TableColumn login = new TableColumn("Login");
        TableColumn name = new TableColumn("Name");
        TableColumn surname = new TableColumn("Surname");
        TableColumn action = new TableColumn("Action");

        login.setCellValueFactory(
                new PropertyValueFactory<AdminListView, String>("login")
        );
        name.setCellValueFactory(
                new PropertyValueFactory<AdminListView, String>("name")
        );
        surname.setCellValueFactory(
                new PropertyValueFactory<AdminListView, String>("surname")
        );

        action.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<AdminListView, String>, TableCell<AdminListView, String>> cellFactory =
                new Callback<TableColumn<AdminListView, String>, TableCell<AdminListView, String>>() {
                    @Override
                    public TableCell<AdminListView, String> call(TableColumn<AdminListView, String> param) {
                        {
                            final TableCell<AdminListView, String> cell = new TableCell<AdminListView, String>() {

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
                                            AdminListView person = getTableView().getItems().get(getIndex());

                                            databaseController.deleteFromAdminList(person.getId());

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

        data = databaseController.selectAllAdmins();

        tableView.setItems(data);
        tableView.getColumns().addAll(login, name, surname, action);
    }
}
