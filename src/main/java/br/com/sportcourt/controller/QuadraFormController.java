package br.com.sportcourt.controller;

import br.com.sportcourt.model.Quadra;
import br.com.sportcourt.service.QuadraService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class QuadraFormController {

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtTipo;
    @FXML
    private TextField txtValorHora;
    @FXML
    private ComboBox<String> comboStatus;

    private Quadra quadraEditando;
    private final QuadraService service = new QuadraService();

    @FXML
    public void initialize() {
        comboStatus.getItems().addAll("DISPONIVEL", "MANUTENCAO", "INDISPONIVEL");
    }

    public void setQuadra(Quadra q) {
        quadraEditando = q;

        if (q != null) {
            txtNome.setText(q.getNome());
            txtTipo.setText(q.getTipo());
            txtValorHora.setText(String.valueOf(q.getValorHora()));
            comboStatus.setValue(q.getStatus());
        }
    }

    @FXML
    public void onSalvar() {
        try {
            String nome = txtNome.getText();
            String tipo = txtTipo.getText();
            double valor = Double.parseDouble(txtValorHora.getText());
            String status = comboStatus.getValue();

            if (nome.isEmpty() || tipo.isEmpty() || status == null) {
                new Alert(Alert.AlertType.ERROR, "Preencha tudo certinho").show();
                return;
            }

            if (quadraEditando == null) {
                quadraEditando = new Quadra();
            }

            quadraEditando.setNome(nome);
            quadraEditando.setTipo(tipo);
            quadraEditando.setValorHora(valor);
            quadraEditando.setStatus(status);

            service.salvar(quadraEditando);
            new Alert(Alert.AlertType.INFORMATION, "Salvo!").show();
            fechar();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Valor inválido").show();
        }
    }

    @FXML
    public void onCancelar() {
        fechar();
    }

    private void fechar() {
        Stage s = (Stage) txtNome.getScene().getWindow();
        s.close();
    }
}
