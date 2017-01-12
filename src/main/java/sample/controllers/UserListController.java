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
import sample.views.UserListView;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class UserListController implements Initializable {
    private static Logger logger = Logger.getLogger(UserListController.class);

    private DatabaseService databaseService = null;

    private DatabaseController databaseController = null;

    @FXML
    private TableView<UserListView> tableView;

    @FXML
    private Button backBtn;

    private ObservableList<UserListView> data = null;

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
    }

    private void initializeTableView() {
        TableColumn<UserListView, String> name = new TableColumn<>("Imie uzytkownika");
        TableColumn<UserListView, String> surname = new TableColumn<>("Nazwisko uzytkownika");
        TableColumn<UserListView, String> login = new TableColumn<>("Login uzytkownika");
        TableColumn<UserListView, String> type = new TableColumn<>("Typ uzytkownika");
        TableColumn action = new TableColumn("Action");

        name.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        surname.setCellValueFactory(
                new PropertyValueFactory<>("surname")
        );

        login.setCellValueFactory(
                new PropertyValueFactory<>("login")
        );

        type.setCellValueFactory(
                new PropertyValueFactory<>("type")
        );

        action.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<UserListView, String>, TableCell<UserListView, String>> cellFactory =
                new Callback<TableColumn<UserListView, String>, TableCell<UserListView, String>>() {
                    @Override
                    public TableCell<UserListView, String> call(TableColumn<UserListView, String> param) {
                        {
                            final TableCell<UserListView, String> cell = new TableCell<UserListView, String>() {

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
                                            UserListView user = getTableView().getItems().get(getIndex());

                                            databaseController.deleteFromUserList(user.getId());

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

        data = databaseController.selectAllUsers();

        tableView.setItems(data);
        tableView.getColumns().addAll(name, surname, login, type, action);
    }
}
