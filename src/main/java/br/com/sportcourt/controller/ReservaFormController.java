package br.com.sportcourt.controller;

import br.com.sportcourt.model.Reserva;
import br.com.sportcourt.model.Quadra;
import br.com.sportcourt.service.QuadraService;
import br.com.sportcourt.service.ReservaService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaFormController extends BaseController {

    @FXML
    private ComboBox<Quadra> comboQuadra;
    @FXML
    private TextField txtCliente;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField txtHoraInicio;
    @FXML
    private TextField txtHoraFim;
    @FXML
    private ComboBox<String> comboStatus;

    private Reserva reservaEditando;
    private final ReservaService service = new ReservaService();
    private final QuadraService quadraService = new QuadraService();

    @FXML
    public void initialize() {
        comboStatus.getItems().addAll("AGENDADO", "CONFIRMADO", "CANCELADO", "FINALIZADO");
        comboStatus.getSelectionModel().selectFirst();

        datePicker.setValue(LocalDate.now());

        carregarQuadras();
    }

    private void carregarQuadras() {
        var quadras = quadraService.listar();
        comboQuadra.getItems().setAll(quadras);

        comboQuadra.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Quadra item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " - " + item.getTipo());
                }
            }
        });
        comboQuadra.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Quadra item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " - " + item.getTipo());
                }
            }
        });

        if (!quadras.isEmpty()) {
            comboQuadra.getSelectionModel().selectFirst();
        } else {
            showWarning("Cadastre uma quadra antes de criar reservas.");
        }
    }

    public void setReserva(Reserva r) {
        reservaEditando = r;

        if (r != null) {
            comboQuadra.getSelectionModel().select(quadraService.buscarPorId(r.getQuadraId()));
            txtCliente.setText(r.getClienteNome());
            datePicker.setValue(r.getData());
            txtHoraInicio.setText(r.getHoraInicio().toString());
            txtHoraFim.setText(r.getHoraFim().toString());
            comboStatus.setValue(r.getStatus());
        }
    }

    @FXML
    public void onSalvar() {
        try {
            Quadra quadraSelecionada = comboQuadra.getValue();
            if (quadraSelecionada == null) {
                showError("Selecione uma quadra válida!");
                return;
            }

            if (txtCliente.getText().trim().isEmpty()) {
                showError("Nome do cliente é obrigatório!");
                return;
            }

            if (datePicker.getValue() == null) {
                showError("Data é obrigatória!");
                return;
            }

            LocalTime inicio, fim;
            try {
                inicio = LocalTime.parse(txtHoraInicio.getText());
                fim = LocalTime.parse(txtHoraFim.getText());

                if (fim.isBefore(inicio) || fim.equals(inicio)) {
                    showError("Hora fim deve ser após a hora início!");
                    return;
                }
            } catch (Exception e) {
                showError("Formato de hora inválido! Use HH:mm (ex: 14:30)");
                return;
            }

            String cliente = txtCliente.getText().trim();
            LocalDate data = datePicker.getValue();
            String status = comboStatus.getValue();

            if (reservaEditando == null) {
                reservaEditando = new Reserva();
            }

            reservaEditando.setQuadraId(quadraSelecionada.getId());
            reservaEditando.setClienteNome(cliente);
            reservaEditando.setData(data);
            reservaEditando.setHoraInicio(inicio);
            reservaEditando.setHoraFim(fim);
            reservaEditando.setStatus(status);

            service.salvar(reservaEditando);

            showInfo("Reserva salva com sucesso!");
            fecharJanela();

        } catch (IllegalStateException e) {
            showWarning(e.getMessage());
        } catch (Exception e) {
            showError("Erro ao salvar reserva: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancelar() {
        if (confirmAction("Cancelar", "Deseja realmente cancelar? As alterações serão perdidas.")) {
            fecharJanela();
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtCliente.getScene().getWindow();
        stage.close();
    }
}
