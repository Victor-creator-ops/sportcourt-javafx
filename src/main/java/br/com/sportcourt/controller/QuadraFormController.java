package br.com.sportcourt.controller;

import br.com.sportcourt.model.Quadra;
import br.com.sportcourt.service.QuadraService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class QuadraFormController extends BaseController {

    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtTipo;
    @FXML
    private TextField txtValor;
    @FXML
    private ComboBox<String> cbStatus;

    private QuadraService service = new QuadraService();
    private Quadra quadraEditando;
    private boolean isEditMode = false;

    @FXML
    public void initialize() {
        cbStatus.getItems().addAll("Disponível", "Indisponível");
        cbStatus.getSelectionModel().selectFirst();
    }

    public void setQuadraParaEdicao(Quadra quadra) {
        if (quadra != null) {
            this.quadraEditando = quadra;
            this.isEditMode = true;

            txtCodigo.setText(quadra.getId());
            txtTipo.setText(quadra.getTipo());
            txtValor.setText(String.valueOf(quadra.getValorHora()));
            cbStatus.setValue(quadra.getDisponivel() ? "Disponível" : "Indisponível");

            txtCodigo.setDisable(true);
        }
    }

    @FXML
    private void onSalvar() {
        try {
            if (txtCodigo.getText().trim().isEmpty()) {
                showError("Código da quadra é obrigatório!");
                return;
            }

            if (txtTipo.getText().trim().isEmpty()) {
                showError("Tipo da quadra é obrigatório!");
                return;
            }

            double valor;
            try {
                valor = Double.parseDouble(txtValor.getText());
                if (valor <= 0) {
                    showError("Valor por hora deve ser maior que zero!");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Valor por hora inválido!");
                return;
            }

            Quadra quadra;
            if (isEditMode) {
                quadra = quadraEditando;
            } else {
                quadra = new Quadra(txtCodigo.getText().trim(), txtTipo.getText().trim(), valor,
                        cbStatus.getValue().equals("Disponível"));
            }

            quadra.setTipo(txtTipo.getText().trim());
            quadra.setValorHora(valor);
            quadra.setDisponivel(cbStatus.getValue().equals("Disponível"));

            service.salvar(quadra);

            showInfo(isEditMode ? "Quadra atualizada com sucesso!"
                    : "Quadra cadastrada com sucesso!");
            fecharJanela();

        } catch (Exception e) {
            showError("Erro ao salvar quadra: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        if (confirmAction("Cancelar", "Deseja realmente cancelar? As alterações serão perdidas.")) {
            fecharJanela();
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtCodigo.getScene().getWindow();
        stage.close();
    }
}
