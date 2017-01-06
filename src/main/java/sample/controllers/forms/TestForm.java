package sample.controllers.forms;

import database.DatabaseController;
import database.services.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.apache.log4j.Logger;
import sample.controllers.AdminListController;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controllers.ControllerUtils.changeSceneContext;

/**
 * Created by Kajetan on 2017-01-06.
 */
public class TestForm extends ParentForm implements Initializable {

    private static Logger logger = Logger.getLogger(TestForm.class);

    @FXML
    private Button backButton;

    public TestForm() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("INFO TestForm INIT");

        backButton.setOnAction(event -> {
            changeSceneContext(event, getClass().getClassLoader().getResource("adminMain.fxml"));
            databaseService.closeConnection();
        });
    }
}
