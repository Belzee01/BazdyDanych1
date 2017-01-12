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
import sample.views.CompanyListView;
import sample.views.ExamineListView;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

public class CompanyListController implements Initializable {
    private static Logger logger = Logger.getLogger(CompanyListController.class);

    private DatabaseService databaseService = null;

    private DatabaseController databaseController = null;

    @FXML
    private TableView<CompanyListView> tableView;

    @FXML
    private Button addNewBtn;

    @FXML
    private Button backBtn;

    private ObservableList<CompanyListView> data = null;

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
            changeSceneContext(event, getClass().getClassLoader().getResource("forms/companyAddForm.fxml"), databaseService);
        });
    }

    private void initializeTableView() {
        TableColumn<CompanyListView, String> name = new TableColumn<>("Nazwa firmy");
        TableColumn<CompanyListView, Integer> nip = new TableColumn<>("NIP");
        TableColumn<CompanyListView, Integer> address = new TableColumn<>("Adres");
        TableColumn action = new TableColumn("Action");

        name.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        nip.setCellValueFactory(
                new PropertyValueFactory<>("nip")
        );
        address.setCellValueFactory(
                new PropertyValueFactory<>("address")
        );

        action.setCellValueFactory( new PropertyValueFactory<>( "DUMMY" ) );

        Callback<TableColumn<CompanyListView, String>, TableCell<CompanyListView, String>> cellFactory =
                new Callback<TableColumn<CompanyListView, String>, TableCell<CompanyListView, String>>()
                {
                    @Override
                    public TableCell<CompanyListView, String> call(TableColumn<CompanyListView, String> param) {
                        {
                            final TableCell<CompanyListView, String> cell = new TableCell<CompanyListView, String>() {

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
                                            CompanyListView company = getTableView().getItems().get(getIndex());

                                            databaseController.deleteFromCompanyList(company.getId());

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

        data = databaseController.selectAllCompanies();

        tableView.setItems(data);
        tableView.getColumns().addAll(name, nip, address, action);
        switch (ContextCatcher.getAccountType()) {
            case ADMIN:
                tableView.getColumns().addAll(name, nip, address, action);
                break;

            case STANDARD:
                tableView.getColumns().addAll(name, nip, address);
                break;

            case COMPANY:
                tableView.getColumns().addAll(name, nip, address);
                break;
        }
    }
}

