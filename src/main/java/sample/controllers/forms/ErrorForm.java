package sample.controllers.forms;

import javafx.scene.control.Alert;

/**
 * Created by DW on 2017-01-14.
 */
public class ErrorForm {
    public static void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
