package br.com.sportcourt.controller;

import br.com.sportcourt.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CadastroController extends BaseController {

    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtNome;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private PasswordField txtConfirmar;
    @FXML
    private Label lblErro;

    private AuthService auth = new AuthService();

    @FXML
    public void initialize() {
        txtUser.setText("novousuario");
        txtEmail.setText("novo@sportcourt.com");
        txtNome.setText("Novo Usuário");
        txtSenha.setText("senha123");
        txtConfirmar.setText("senha123");
    }

    @FXML
    private void onCadastrar() {
        String user = txtUser.getText().trim();
        String email = txtEmail.getText().trim();
        String nome = txtNome.getText().trim();
        String senha = txtSenha.getText();
        String confirmar = txtConfirmar.getText();

        // Validações
        if (user.isEmpty() || email.isEmpty() || nome.isEmpty() || senha.isEmpty()) {
            lblErro.setText("Preencha todos os campos!");
            lblErro.setStyle("-fx-text-fill: red;");
            return;
        }

        if (!senha.equals(confirmar)) {
            lblErro.setText("As senhas não coincidem!");
            lblErro.setStyle("-fx-text-fill: red;");
            txtSenha.selectAll();
            txtSenha.requestFocus();
            return;
        }

        if (senha.length() < 4) {
            lblErro.setText("Senha muito curta (mínimo 4 caracteres)!");
            lblErro.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean ok = auth.cadastrar(user, senha, email, nome, "OPERADOR");

        if (!ok) {
            lblErro.setText("Falha ao cadastrar. Usuário já existe.");
            lblErro.setStyle("-fx-text-fill: red;");
        } else {
            lblErro.setText("✅ Cadastro realizado com sucesso! Faça login.");
            lblErro.setStyle("-fx-text-fill: green;");

            txtUser.clear();
            txtEmail.clear();
            txtNome.clear();
            txtSenha.clear();
            txtConfirmar.clear();
        }
    }

    @FXML
    private void onVoltar() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/LoginView.fxml"));
            javafx.scene.Parent root = loader.load();
            txtUser.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
