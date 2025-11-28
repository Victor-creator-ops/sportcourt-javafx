package br.com.sportcourt.controller;

import br.com.sportcourt.MainApp;
import br.com.sportcourt.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    private final AuthService authService = new AuthService();

    @FXML
    public void onLoginClick() {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        if (authService.login(user, pass)) {
            abrirDashboard();
        } else {
            new Alert(Alert.AlertType.ERROR, "Usuário ou senha inválidos.").show();
        }
    }

    private void abrirDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setTitle("SportCourt - Dashboard");
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
