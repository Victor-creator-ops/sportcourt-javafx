package br.com.sportcourt.controller;

import br.com.sportcourt.model.Comanda;
import br.com.sportcourt.service.ComandaService;
import br.com.sportcourt.service.ReservaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ComandaListController extends BaseController {

    @FXML
    private TableView<Comanda> tableComandas;
    @FXML
    private TableColumn<Comanda, Integer> colId;
    @FXML
    private TableColumn<Comanda, String> colReserva;
    @FXML
    private TableColumn<Comanda, String> colCliente;
    @FXML
    private TableColumn<Comanda, Double> colTotal;
    @FXML
    private TableColumn<Comanda, String> colStatus;
    @FXML
    private TextField txtCliente;
    @FXML
    private TextField txtReservaId;

    private final ComandaService service = new ComandaService();
    private final ReservaService reservaService = new ReservaService();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarDados();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId())
                        .asObject());

        colReserva.setCellValueFactory(c -> {
            Integer r = c.getValue().getReservaId();
            return new javafx.beans.property.SimpleStringProperty(
                    r == null ? "-" : String.valueOf(r));
        });

        colCliente.setCellValueFactory(c -> {
            String cli = c.getValue().getClienteNome();
            return new javafx.beans.property.SimpleStringProperty(
                    cli == null || cli.isBlank() ? "-" : cli);
        });

        colTotal.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTotal())
                        .asObject());
        colStatus.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));
    }

    private void carregarDados() {
        try {
            tableComandas.setItems(FXCollections.observableArrayList(service.listar()));
        } catch (Exception e) {
            showError("Erro ao carregar comandas: " + e.getMessage());
        }
    }

    @FXML
    public void onNova() {
        try {
            Comanda c = new Comanda();
            String cliente = txtCliente.getText() == null ? "" : txtCliente.getText().trim();
            String reserva = txtReservaId.getText() == null ? "" : txtReservaId.getText().trim();

            Integer reservaId = null;
            if (!reserva.isBlank()) {
                try {
                    reservaId = Integer.parseInt(reserva);
                } catch (NumberFormatException e) {
                    showWarning("Informe um número válido para a reserva.");
                    return;
                }

                var reservaEncontrada = reservaService.buscarPorId(reservaId);
                if (reservaEncontrada == null) {
                    showWarning("Reserva não encontrada.");
                    return;
                }

                if (cliente.isBlank()) {
                    cliente = reservaEncontrada.getClienteNome();
                }
            }

            c.setReservaId(reservaId);
            c.setClienteNome(cliente.isBlank() ? null : cliente);
            c.setTotal(0.0);
            c.setStatus("ABERTA");

            service.salvar(c);
            carregarDados();
            showInfo("Nova comanda criada!");
            txtCliente.clear();
            txtReservaId.clear();

        } catch (Exception e) {
            showError("Erro ao criar comanda: " + e.getMessage());
        }
    }

    @FXML
    public void onAbrirItens() {
        Comanda c = tableComandas.getSelectionModel().getSelectedItem();
        if (c == null) {
            showWarning("Selecione uma comanda");
            return;
        }

        abrirTelaItens(c);
    }

    private void abrirTelaItens(Comanda c) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/ItemComandaView.fxml"));
            Parent root = loader.load();

            ItemComandaController controller = loader.getController();
            controller.setComanda(c);

            Stage stage = new Stage();
            stage.setTitle("Itens da Comanda #" + c.getId());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            carregarDados();

        } catch (Exception e) {
            showError("Erro ao abrir tela de itens: " + e.getMessage());
        }
    }

    @FXML
    public void onFechar() {
        Comanda c = tableComandas.getSelectionModel().getSelectedItem();
        if (c == null) {
            showWarning("Selecione uma comanda");
            return;
        }

        service.fecharComanda(c);
        carregarDados();
        showInfo("Comanda #" + c.getId() + " fechada!");
    }

    @FXML
    public void onExcluir() {
        Comanda c = tableComandas.getSelectionModel().getSelectedItem();
        if (c == null) {
            showWarning("Selecione uma comanda");
            return;
        }

        if (confirmAction("Confirmar Exclusão",
                "Deseja realmente excluir a comanda #" + c.getId() + "?")) {

            service.remover(c.getId());
            carregarDados();
            showInfo("Comanda excluída!");
        }
    }

    @FXML
    public void onAtualizar() {
        carregarDados();
        showInfo("Dados atualizados!");
    }
}
