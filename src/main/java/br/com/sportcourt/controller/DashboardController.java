package br.com.sportcourt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

public class DashboardController {

    @FXML
    private BorderPane root;

    @FXML
    public void onQuadras() {
        carregarView("/view/QuadraListView.fxml");
    }

    @FXML
    public void onReservas() {
        carregarView("/view/ReservaListView.fxml");
    }

    @FXML
    public void onProdutos() {
        carregarView("/view/ProdutoListView.fxml");
    }

    @FXML
    public void onComandas() {
        carregarView("/view/ComandaListView.fxml");
    }

    @FXML
    public void onFinanceiro() {
        carregarView("/view/FinanceiroView.fxml");
    }

    private void carregarView(String caminho) {
        try {
            Node tela = FXMLLoader.load(getClass().getResource(caminho));
            root.setCenter(tela);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao carregar tela: " + caminho).show();
        }
    }
}
