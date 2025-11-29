package br.com.sportcourt.controller;

import br.com.sportcourt.model.Produto;
import br.com.sportcourt.service.ProdutoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ProdutoListController {

    @FXML
    private TableView<Produto> tableProdutos;
    @FXML
    private TableColumn<Produto, Integer> colId;
    @FXML
    private TableColumn<Produto, String> colNome;
    @FXML
    private TableColumn<Produto, Double> colPreco;
    @FXML
    private TableColumn<Produto, String> colCategoria;
    @FXML
    private TableColumn<Produto, String> colAtivo;

    private final ProdutoService service = new ProdutoService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        colNome.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNome()));
        colPreco.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPreco()).asObject());
        colCategoria
                .setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategoria()));
        colAtivo.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isAtivo() ? "Sim" : "Não"));

        carregar();
    }

    private void carregar() {
        tableProdutos.setItems(FXCollections.observableArrayList(service.listar()));
    }

    @FXML
    public void onCadastrar() {
        abrirFormulario(null);
    }

    @FXML
    public void onEditar() {
        Produto p = tableProdutos.getSelectionModel().getSelectedItem();
        if (p == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione um produto").show();
            return;
        }
        abrirFormulario(p);
    }

    @FXML
    public void onExcluir() {
        Produto p = tableProdutos.getSelectionModel().getSelectedItem();
        if (p != null) {
            service.remover(p.getId());
            carregar();
        }
    }

    private void abrirFormulario(Produto p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProdutoFormView.fxml"));
            Parent root = loader.load();

            ProdutoFormController controller = loader.getController();
            controller.setProduto(p);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(p == null ? "Novo Produto" : "Editar Produto");
            stage.showAndWait();

            carregar();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
