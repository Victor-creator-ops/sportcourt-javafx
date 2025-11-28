package br.com.sportcourt.controller;

import br.com.sportcourt.model.Quadra;
import br.com.sportcourt.service.QuadraService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class QuadraListController {

    @FXML
    private TableView<Quadra> tableQuadras;
    @FXML
    private TableColumn<Quadra, Integer> colId;
    @FXML
    private TableColumn<Quadra, String> colNome;
    @FXML
    private TableColumn<Quadra, String> colTipo;
    @FXML
    private TableColumn<Quadra, Double> colValor;
    @FXML
    private TableColumn<Quadra, String> colStatus;

    private final QuadraService service = new QuadraService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId())
                        .asObject());
        colNome.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNome()));
        colTipo.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTipo()));
        colValor.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getValorHora())
                        .asObject());
        colStatus.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        carregar();
    }

    private void carregar() {
        tableQuadras.setItems(FXCollections.observableArrayList(service.listar()));
    }

    @FXML
    public void onCadastrar() {
        abrirFormulario(null);
    }

    @FXML
    public void onEditar() {
        Quadra quadra = tableQuadras.getSelectionModel().getSelectedItem();
        if (quadra == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione algo aí").show();
            return;
        }
        abrirFormulario(quadra);
    }

    @FXML
    public void onExcluir() {
        Quadra q = tableQuadras.getSelectionModel().getSelectedItem();
        if (q != null) {
            service.remover(q.getId());
            carregar();
        }
    }

    private void abrirFormulario(Quadra q) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuadraFormView.fxml"));
            Parent root = loader.load();

            QuadraFormController controller = loader.getController();
            controller.setQuadra(q);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(q == null ? "Nova Quadra" : "Editar Quadra");
            stage.showAndWait();

            carregar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
