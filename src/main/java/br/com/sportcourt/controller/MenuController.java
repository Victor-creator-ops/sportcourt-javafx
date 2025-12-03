package br.com.sportcourt.controller;

import br.com.sportcourt.service.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
    private Button btnDashboard;
    @FXML
    private Button btnQuadras;
    @FXML
    private Button btnReservas;
    @FXML
    private Button btnProdutos;
    @FXML
    private Button btnComandas;
    @FXML
    private Button btnFinanceiro;
    @FXML
    private Button btnRelatorios;
    @FXML
    private Button btnConfiguracoes;
    @FXML
    private Button btnMateriais;

    @FXML
    public void initialize() {
        if (Session.getUsuarioLogado() != null) {
            var usuario = Session.getUsuarioLogado();
            String info = String.format("%s (%s)", usuario.getNome(), usuario.getRole());
            lblUsuario.setText(info);
        }
        aplicarPermissoes();
    }

    @FXML
    private void onMateriais() {
        if (bloqueadoParaAtendente()) {
            return;
        }
        carregarView("MaterialListView.fxml");
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

    private String getRole() {
        var u = Session.getUsuarioLogado();
        return u == null ? "" : u.getRole().toUpperCase();
    }

    private boolean isAtendente() {
        return "ATENDENTE".equals(getRole());
    }

    private boolean isOperador() {
        return "OPERADOR".equals(getRole());
    }

    private boolean isAdmin() {
        return "ADMIN".equals(getRole());
    }

    private boolean podeVerFinanceiro() {
        return isAdmin();
    }

    private void esconderBotao(Button b) {
        b.setDisable(true);
        b.setVisible(false);
        b.setManaged(false);
    }

    private void aplicarPermissoes() {
        if (isAtendente()) {
            esconderBotao(btnDashboard);
            esconderBotao(btnQuadras);
            esconderBotao(btnReservas);
            esconderBotao(btnProdutos);
            esconderBotao(btnMateriais);
            esconderBotao(btnFinanceiro);
            esconderBotao(btnRelatorios);
            esconderBotao(btnConfiguracoes);
            carregarView("ComandaListView.fxml");
        } else if (isOperador()) {
            esconderBotao(btnFinanceiro);
            btnConfiguracoes.setDisable(true);
        } else if (!isAdmin()) {
            btnConfiguracoes.setDisable(true);
        }
    }

    private boolean bloqueadoParaAtendente() {
        if (isAtendente()) {
            showWarning("Acesso restrito ao PDV para atendentes.");
            carregarView("ComandaListView.fxml");
            return true;
        }
        return false;
    }

    @FXML
    private void onQuadras() {
        if (bloqueadoParaAtendente()) {
            return;
        }
        carregarView("QuadraListView.fxml");
    }

    @FXML
    private void onReservas() {
        if (bloqueadoParaAtendente()) {
            return;
        }
        carregarView("ReservaListView.fxml");
    }

    @FXML
    private void onProdutos() {
        if (bloqueadoParaAtendente()) {
            return;
        }
        carregarView("ProdutoListView.fxml");
    }

    @FXML
    private void onComandas() {
        carregarView("ComandaListView.fxml");
    }

    @FXML
    private void onFinanceiro() {
        if (!podeVerFinanceiro()) {
            showWarning("Apenas administradores podem acessar o financeiro.");
            return;
        }
        carregarView("FinanceiroListView.fxml");
    }

    @FXML
    private void onRelatorios() {
        if (bloqueadoParaAtendente()) {
            return;
        }
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
        if (isAtendente()) {
            carregarView("ComandaListView.fxml");
            return;
        }
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
