package sample.controllers.forms;

import database.DatabaseController;
import database.services.DatabaseService;
import javafx.fxml.Initializable;
import org.apache.log4j.Logger;
import sample.controllers.AdminListController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Kajetan on 2017-01-06.
 */
public class ParentForm {

    private static Logger logger = Logger.getLogger(ParentForm.class);

    protected DatabaseService databaseService = null;

    protected DatabaseController databaseController = null;

    public ParentForm() {
        logger.info("INFO Parent Init");
        databaseService = new DatabaseService();
        databaseController = new DatabaseController(databaseService);

        databaseService.connectToDb();
    }
}
