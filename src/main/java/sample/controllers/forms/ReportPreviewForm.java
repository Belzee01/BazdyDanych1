package sample.controllers.forms;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;
import sample.ContextCatcher;
import sample.views.ReportCompanyListView;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

/**
 * Created by DW on 2017-01-14.
 */
public class ReportPreviewForm extends ParentForm implements Initializable {
    private static Logger logger = Logger.getLogger(ReportPreviewForm.class);

    @FXML
    private TableView tableView;

    @FXML
    private Button backBtn;

    @FXML
    private TextField sumField;

    private ObservableList<ReportCompanyListView> data = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        backBtn.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("reportsMain.fxml"), databaseService);
        });

        if (ContextCatcher.getCompanyId() == null) {
            sumField.setText("0");
        } else {
            sumField.setText(String.valueOf(databaseController.selectSumForCompany(ContextCatcher.getCompanyId(), ContextCatcher.getReportId())));
        }

        initializeTableView();
    }

    /**
     * Initializes all properties of tableView and binds column to actual data. Selects specific data such as patients for given company
     */
    private void initializeTableView() {
        TableColumn name = new TableColumn("Imie pacjenta");
        TableColumn surname = new TableColumn("Nazwisko pacjenta");
        TableColumn examine = new TableColumn("Nazwa badania");
        TableColumn prise = new TableColumn("Cena");

        name.setCellValueFactory(
                new PropertyValueFactory<ReportCompanyListView, String>("name")
        );
        surname.setCellValueFactory(
                new PropertyValueFactory<ReportCompanyListView, String>("surname")
        );

        examine.setCellValueFactory(
                new PropertyValueFactory<ReportCompanyListView, String>("examine")
        );

        prise.setCellValueFactory(
                new PropertyValueFactory<ReportCompanyListView, Integer>("prise")
        );

        data = databaseController.selectPatientsForReport(ContextCatcher.getCompanyId(), ContextCatcher.getReportId());

        tableView.setItems(data);
        tableView.getColumns().addAll(name, surname, examine, prise);
    }
}

