package br.com.sportcourt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class DashboardController extends BaseController {

    @FXML
    private StackPane viewContainer;

    private void carregarView(String nomeFXML) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/br/com/sportcourt/view/" + nomeFXML));

            Parent root = loader.load();
            viewContainer.getChildren().setAll(root);

        } catch (Exception e) {
            showError("Erro ao carregar view: " + e.getMessage());
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
    private void onHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/DashboardHomeView.fxml"));
            Parent root = loader.load();
            viewContainer.getChildren().setAll(root);
        } catch (Exception e) {
            showError("Erro ao carregar dashboard: " + e.getMessage());
        }
    }
}
