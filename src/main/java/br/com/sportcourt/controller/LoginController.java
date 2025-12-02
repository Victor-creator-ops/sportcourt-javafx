package br.com.sportcourt.controller;

import br.com.sportcourt.service.AuthService;
import br.com.sportcourt.service.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends BaseController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Label lblErro;

    private AuthService auth = new AuthService();

    @FXML
    public void initialize() {
        txtUsuario.setText("");
        txtSenha.setText("");

        txtUsuario.requestFocus();
    }

    @FXML
    private void onLogin() {
        try {
            String user = txtUsuario.getText().trim();
            String senha = txtSenha.getText();

            if (user.isEmpty() || senha.isEmpty()) {
                lblErro.setText("Preencha todos os campos!");
                lblErro.setStyle("-fx-text-fill: red;");
                return;
            }

            var usuario = auth.autenticar(user, senha);

            if (usuario != null) {

                Session.setUsuarioLogado(usuario);

                txtUsuario.clear();
                txtSenha.clear();
                lblErro.setText("");

                carregarDashboard();

            } else {
                lblErro.setText("Usuário ou senha incorretos!");
                lblErro.setStyle("-fx-text-fill: red;");
                txtSenha.selectAll();
                txtSenha.requestFocus();
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblErro.setText("Erro no sistema. Tente novamente.");
            lblErro.setStyle("-fx-text-fill: red;");
        }
    }

    private void carregarDashboard() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/br/com/sportcourt/view/DashboardView.fxml"));
        Parent root = loader.load();
        txtUsuario.getScene().setRoot(root);
    }

    @FXML
    private void onCadastrar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/CadastroView.fxml"));
            Parent root = loader.load();
            txtUsuario.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            lblErro.setText("Erro ao abrir cadastro.");
        }
    }

    @FXML
    private void onEnterPressed() {
        onLogin();
    }
}
