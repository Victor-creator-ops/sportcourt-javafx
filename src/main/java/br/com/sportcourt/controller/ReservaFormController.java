package br.com.sportcourt.controller;

import br.com.sportcourt.model.Reserva;
import br.com.sportcourt.service.ReservaService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalTime;

public class ReservaFormController {

    @FXML
    private TextField txtQuadraId;
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

    @FXML
    public void initialize() {
        comboStatus.getItems().addAll("AGENDADO", "CANCELADO", "FINALIZADO");
    }

    public void setReserva(Reserva r) {
        reservaEditando = r;

        if (r != null) {
            txtQuadraId.setText(String.valueOf(r.getQuadraId()));
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
            int quadraId = Integer.parseInt(txtQuadraId.getText());
            String cliente = txtCliente.getText();
            var data = datePicker.getValue();
            LocalTime inicio = LocalTime.parse(txtHoraInicio.getText());
            LocalTime fim = LocalTime.parse(txtHoraFim.getText());
            String status = comboStatus.getValue();

            if (reservaEditando == null)
                reservaEditando = new Reserva();

            reservaEditando.setQuadraId(quadraId);
            reservaEditando.setClienteNome(cliente);
            reservaEditando.setData(data);
            reservaEditando.setHoraInicio(inicio);
            reservaEditando.setHoraFim(fim);
            reservaEditando.setStatus(status);

            service.salvar(reservaEditando);

            new Alert(Alert.AlertType.INFORMATION, "Reserva salva!").show();
            fechar();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erro ao salvar. Verifique os dados.").show();
        }
    }

    @FXML
    public void onCancelar() {
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) txtCliente.getScene().getWindow();
        stage.close();
    }
}
