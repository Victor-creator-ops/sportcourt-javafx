package br.com.sportcourt.controller;

import br.com.sportcourt.model.Reserva;
import br.com.sportcourt.service.ReservaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ReservaListController {

    @FXML
    private TableView<Reserva> tableReservas;
    @FXML
    private TableColumn<Reserva, Integer> colId;
    @FXML
    private TableColumn<Reserva, Integer> colQuadra;
    @FXML
    private TableColumn<Reserva, String> colCliente;
    @FXML
    private TableColumn<Reserva, String> colData;
    @FXML
    private TableColumn<Reserva, String> colInicio;
    @FXML
    private TableColumn<Reserva, String> colFim;
    @FXML
    private TableColumn<Reserva, String> colStatus;

    private final ReservaService service = new ReservaService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId())
                        .asObject());
        colQuadra.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuadraId())
                        .asObject());
        colCliente.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getClienteNome()));
        colData.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getData().toString()));
        colInicio.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getHoraInicio().toString()));
        colFim.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getHoraFim().toString()));
        colStatus.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        carregar();
    }

    private void carregar() {
        tableReservas.setItems(FXCollections.observableArrayList(service.listar()));
    }

    @FXML
    public void onCadastrar() {
        abrirFormulario(null);
    }

    @FXML
    public void onEditar() {
        Reserva r = tableReservas.getSelectionModel().getSelectedItem();
        if (r == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione uma reserva").show();
            return;
        }
        abrirFormulario(r);
    }

    @FXML
    public void onExcluir() {
        Reserva r = tableReservas.getSelectionModel().getSelectedItem();
        if (r != null) {
            service.remover(r.getId());
            carregar();
        }
    }

    private void abrirFormulario(Reserva r) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/view/ReservaFormView.fxml"));
            Parent root = loader.load();

            ReservaFormController controller = loader.getController();
            controller.setReserva(r);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(r == null ? "Nova Reserva" : "Editar Reserva");
            stage.showAndWait();

            carregar();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
