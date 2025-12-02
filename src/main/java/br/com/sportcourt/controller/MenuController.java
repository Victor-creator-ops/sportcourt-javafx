package br.com.sportcourt.controller;

import br.com.sportcourt.service.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MenuController extends BaseController {

    @FXML
    private Label lblUsuario;
    @FXML
    private StackPane viewContainer;
    @FXML
    private BorderPane mainPane;

    @FXML
    public void initialize() {
        if (Session.getUsuarioLogado() != null) {
            var usuario = Session.getUsuarioLogado();
            String info = String.format("%s (%s)", usuario.getNome(), usuario.getRole());
            lblUsuario.setText(info);
        }
    }

    private void carregarView(String nomeFXML) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/br/com/sportcourt/view/" + nomeFXML));
            Parent root = loader.load();

            viewContainer.getChildren().clear();
            viewContainer.getChildren().add(root);

        } catch (IOException e) {
            showError("Erro ao carregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onQuadras() {
        carregarView("QuadraListView.fxml");
    }

    @FXML
    private void onReservas() {
        carregarView("ReservaListView.fxml");
    }

    @FXML
    private void onProdutos() {
        carregarView("ProdutoListView.fxml");
    }

    @FXML
    private void onComandas() {
        carregarView("ComandaListView.fxml");
    }

    @FXML
    private void onFinanceiro() {
        carregarView("FinanceiroListView.fxml");
    }

    @FXML
    private void onRelatorios() {
        showInfo("Relatórios em desenvolvimento!");
    }

    @FXML
    private void onConfiguracoes() {
        var u = Session.getUsuarioLogado();
        if (u == null || !"ADMIN".equalsIgnoreCase(u.getRole())) {
            showWarning("Apenas administradores podem acessar Configurações.");
            return;
        }
        carregarView("ConfiguracoesView.fxml");
    }

    @FXML
    private void onHome() {
        try {
            Parent root = FXMLLoader
                    .load(getClass().getResource("/br/com/sportcourt/view/DashboardHomeView.fxml"));
            viewContainer.getChildren().clear();
            viewContainer.getChildren().add(root);
        } catch (IOException e) {
            showError("Erro ao carregar dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void onLogout() {
        if (confirmAction("Sair", "Deseja realmente sair do sistema?")) {
            Session.logout();
            try {
                Parent loginView = FXMLLoader
                        .load(getClass().getResource("/br/com/sportcourt/view/LoginView.fxml"));
                mainPane.getScene().setRoot(loginView);
            } catch (IOException e) {
                showError("Erro ao fazer logout: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onSobre() {
        showAlert("Sobre o Sistema",
                "🏸 SportCourt - Sistema de Gestão\n\n" + "Versão: 1.0.0 (PIC)\n"
                        + "Desenvolvido para: Gestão de Quadras Esportivas\n" + "Funcionalidades:\n"
                        + "• Gestão de Quadras\n" + "• Controle de Reservas\n"
                        + "• Administração do Bar\n" + "• Controle Financeiro\n\n",
                javafx.scene.control.Alert.AlertType.INFORMATION);
    }
}
