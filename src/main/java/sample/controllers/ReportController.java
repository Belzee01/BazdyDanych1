package sample.controllers;

import database.DatabaseController;
import database.services.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;
import sample.ContextCatcher;
import sample.views.ReportListView;

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

    private ObservableList<ReportListView> data = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseService = new DatabaseService();
        databaseController = new DatabaseController(databaseService);

        databaseService.connectToDb();

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

        logger.info("Context id :" + ContextCatcher.getCompanyId());

        if(ContextCatcher.getCompanyId() == null)
            addNewBtn.setDisable(true);
        else
            addNewBtn.setDisable(false);

        addNewBtn.setOnAction(event -> {
            if(ContextCatcher.getCompanyId() != null) {
                databaseController.saveNewReportInDB(ContextCatcher.getCompanyId());
                data = databaseController.selectAllReports(ContextCatcher.getCompanyId());
                tableView.setItems(data);
            }
        });

        initializeTableView();
    }

    private void initializeTableView() {
        TableColumn name = new TableColumn("Numer raportu");
        TableColumn surname = new TableColumn("Data");

        tableView.setRowFactory(param -> {
            TableRow<ReportListView> tableRow = new TableRow();
            tableRow.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!tableRow.isEmpty())) {
                    ReportListView reportListView = tableRow.getItem();
                    logger.info("Report id selected: " + reportListView.getId());
                    ContextCatcher.setReportId(reportListView.getId());
                    changeSceneContext(event, getClass().getClassLoader().getResource("forms/reportsPreviewForm.fxml"), databaseService);
                }
            });
            return tableRow;
        });

        name.setCellValueFactory(
                new PropertyValueFactory<ReportListView, String>("id")
        );
        surname.setCellValueFactory(
                new PropertyValueFactory<ReportListView, String>("data")
        );

        if(ContextCatcher.getCompanyId() != null) {
           data = databaseController.selectAllReports(ContextCatcher.getCompanyId());
        } else {
            data = FXCollections.observableArrayList();
        }

        tableView.setItems(data);
        tableView.getColumns().addAll(name, surname);
    }
}
