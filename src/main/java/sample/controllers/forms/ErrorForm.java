package sample.controllers.forms;

import javafx.scene.control.Alert;

/**
 * Created by DW on 2017-01-14.
 */
public class ErrorForm {
    /**
     * Displays modal alert with information
     * @param title Title of alert
     * @param content Information to be displayed
     */
    public static void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
