package br.com.sportcourt.controller;

import br.com.sportcourt.model.Reserva;
import br.com.sportcourt.service.ReservaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class ReservaListController extends BaseController {

    @FXML
    private TableView<Reserva> tableReservas;
    @FXML
    private TableColumn<Reserva, Integer> colId;
    @FXML
    private TableColumn<Reserva, String> colQuadra;
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
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        configurarColunas();
        carregarDados();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId())
                        .asObject());

        colQuadra.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getQuadraId()));

        colCliente.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getClienteNome()));

        colData.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getData().format(dateFormatter)));

        colInicio.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getHoraInicio().toString()));

        colFim.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getHoraFim().toString()));

        colStatus.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));
    }

    private void carregarDados() {
        try {
            tableReservas.setItems(FXCollections.observableArrayList(service.listar()));
        } catch (Exception e) {
            showError("Erro ao carregar reservas: " + e.getMessage());
        }
    }

    @FXML
    public void onCadastrar() {
        abrirFormulario(null);
    }

    @FXML
    public void onEditar() {
        Reserva reserva = tableReservas.getSelectionModel().getSelectedItem();
        if (reserva == null) {
            showWarning("Selecione uma reserva para editar!");
            return;
        }
        abrirFormulario(reserva);
    }

    @FXML
    public void onExcluir() {
        Reserva reserva = tableReservas.getSelectionModel().getSelectedItem();
        if (reserva == null) {
            showWarning("Selecione uma reserva para excluir!");
            return;
        }

        if (confirmAction("Confirmar Exclusão", "Deseja realmente excluir a reserva #"
                + reserva.getId() + " do cliente " + reserva.getClienteNome() + "?")) {

            try {
                service.remover(reserva.getId());
                showInfo("Reserva excluída com sucesso!");
                carregarDados();
            } catch (Exception e) {
                showError("Erro ao excluir reserva: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onAtualizar() {
        carregarDados();
        showInfo("Dados atualizados!");
    }

    private void abrirFormulario(Reserva reserva) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/ReservaFormView.fxml"));
            Parent root = loader.load();

            ReservaFormController controller = loader.getController();
            controller.setReserva(reserva);

            Stage stage = new Stage();
            stage.setTitle(reserva == null ? "Nova Reserva" : "Editar Reserva");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            carregarDados();

        } catch (Exception e) {
            showError("Erro ao abrir formulário: " + e.getMessage());
        }
    }
}
