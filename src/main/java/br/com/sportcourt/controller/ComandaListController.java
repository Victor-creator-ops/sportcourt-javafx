package br.com.sportcourt.controller;

import br.com.sportcourt.model.Comanda;
import br.com.sportcourt.service.ComandaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ComandaListController {

    @FXML private TableView<Comanda> tableComandas;
    @FXML private TableColumn<Comanda, Integer> colId;
    @FXML private TableColumn<Comanda, String> colReserva;
    @FXML private TableColumn<Comanda, Double> colTotal;
    @FXML private TableColumn<Comanda, String> colStatus;

    private final ComandaService service = new ComandaService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());

        colReserva.setCellValueFactory(c -> {
            Integer r = c.getValue().getReservaId();
            return new javafx.beans.property.SimpleStringProperty(r == null ? "-" : String.valueOf(r));
        });

        colTotal.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTotal()).asObject());
        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        carregarTabela();
    }

    private void carregarTabela() {
        tableComandas.setItems(FXCollections.observableArrayList(service.listar()));
    }

    @FXML
    public void onNova() {
        try {
            Comanda c = new Comanda();
            c.setReservaId(null);
            c.setTotal(0.0);
            c.setStatus("ABERTA");

            service.salvar(c);
            carregarTabela();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAbrirItens() {
        Comanda c = tableComandas.getSelectionModel().getSelectedItem();
        if (c == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione uma comanda").show();
            return;
        }

        abrirTelaItens(c);
    }

    private void abrirTelaItens(Comanda c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ItemComandaView.fxml"));
            Parent root = loader.load();

            ItemComandaController controller = loader.getController();
            controller.setComanda(c);

            Stage stage = new Stage();
            stage.setTitle("Itens da Comanda #" + c.getId());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            carregarTabela();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onFechar() {
        Comanda c = tableComandas.getSelectionModel().getSelectedItem();
        if (c == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione uma comanda").show();
            return;
        }

        service.fecharComanda(c);
        carregarTabela();
    }

    @FXML
    public void onExcluir() {
        Comanda c = tableComandas.getSelectionModel().getSelectedItem();
        if (c == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione uma comanda").show();
            return;
        }

        service.remover(c.getId());
        carregarTabela();
    }
}
