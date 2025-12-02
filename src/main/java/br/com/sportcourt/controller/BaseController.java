package br.com.sportcourt.controller;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class BaseController {

    protected void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showInfo(String message) {
        showAlert("Informação", message, Alert.AlertType.INFORMATION);
    }

    protected void showWarning(String message) {
        showAlert("Aviso", message, Alert.AlertType.WARNING);
    }

    protected void showError(String message) {
        showAlert("Erro", message, Alert.AlertType.ERROR);
    }

    protected void showError(String title, String message) {
        showAlert(title, message, Alert.AlertType.ERROR);
    }

    protected void closeWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    protected boolean confirmAction(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().orElse(
                javafx.scene.control.ButtonType.CANCEL) == javafx.scene.control.ButtonType.OK;
    }
}
