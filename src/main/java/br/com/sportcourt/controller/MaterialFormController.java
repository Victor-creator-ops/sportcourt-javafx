// File: src/main/java/br/com/sportcourt/controller/MaterialFormController.java
package br.com.sportcourt.controller;

import br.com.sportcourt.model.MaterialEsportivo;
import br.com.sportcourt.service.MaterialEsportivoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MaterialFormController extends BaseController {

    @FXML private TextField txtNome;
    @FXML private TextArea txtDescricao;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private TextField txtValorLocacao;
    @FXML private TextField txtValorCaucao;
    @FXML private TextField txtQuantidadeTotal;
    @FXML private DatePicker dateDataValidade;
    @FXML private ComboBox<String> comboEstado;
    @FXML private ComboBox<String> comboAtivo;

    private MaterialEsportivo materialEditando;
    private final MaterialEsportivoService service = new MaterialEsportivoService();

    @FXML
    public void initialize() {
        comboCategoria.getItems().addAll("BOLA", "REDE", "UNIFORME", "EQUIPAMENTO", 
                                        "PROTEÇÃO", "ACESSÓRIO", "OUTROS");
        comboCategoria.getSelectionModel().selectFirst();
        
        comboEstado.getItems().addAll("NOVO", "BOM", "REGULAR", "RUIM", "PÉSSIMO");
        comboEstado.getSelectionModel().select("BOM");
        
        comboAtivo.getItems().addAll("Sim", "Não");
        comboAtivo.getSelectionModel().selectFirst();
        
        dateDataValidade.setValue(LocalDate.now().plusMonths(6));
    }

    public void setMaterial(MaterialEsportivo m) {
        materialEditando = m;

        if (m != null) {
            txtNome.setText(m.getNome());
            txtDescricao.setText(m.getDescricao());
            comboCategoria.setValue(m.getCategoria());
            txtValorLocacao.setText(String.valueOf(m.getValorLocacao()));
            txtValorCaucao.setText(String.valueOf(m.getValorCaucao()));
            txtQuantidadeTotal.setText(String.valueOf(m.getQuantidadeTotal()));
            
            if (m.getDataValidade() != null) {
                dateDataValidade.setValue(m.getDataValidade());
            }
            
            comboEstado.setValue(m.getEstado());
            comboAtivo.setValue(m.isAtivo() ? "Sim" : "Não");
            
            // Desabilitar edição de quantidade total em edição
            txtQuantidadeTotal.setDisable(true);
        }
    }

    @FXML
    public void onSalvar() {
        try {
            // Validações
            if (txtNome.getText().trim().isEmpty()) {
                showError("Nome do material é obrigatório!");
                txtNome.requestFocus();
                return;
            }
            
            double valorLocacao;
            double valorCaucao;
            int quantidadeTotal;
            
            try {
                valorLocacao = Double.parseDouble(txtValorLocacao.getText());
                if (valorLocacao <= 0) {
                    showError("Valor de locação deve ser maior que zero!");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Valor de locação inválido!");
                return;
            }
            
            try {
                valorCaucao = Double.parseDouble(txtValorCaucao.getText());
                if (valorCaucao < 0) {
                    showError("Valor de caução não pode ser negativo!");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Valor de caução inválido!");
                return;
            }
            
            try {
                quantidadeTotal = Integer.parseInt(txtQuantidadeTotal.getText());
                if (quantidadeTotal <= 0) {
                    showError("Quantidade total deve ser maior que zero!");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Quantidade total inválida!");
                return;
            }
            
            String nome = txtNome.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String categoria = comboCategoria.getValue();
            String estado = comboEstado.getValue();
            boolean ativo = comboAtivo.getValue().equals("Sim");
            LocalDate dataValidade = dateDataValidade.getValue();
            
            if (materialEditando == null) {
                materialEditando = new MaterialEsportivo();
                materialEditando.setQuantidadeDisponivel(quantidadeTotal);
            }
            
            materialEditando.setNome(nome);
            materialEditando.setDescricao(descricao);
            materialEditando.setCategoria(categoria);
            materialEditando.setValorLocacao(valorLocacao);
            materialEditando.setValorCaucao(valorCaucao);
            materialEditando.setQuantidadeTotal(quantidadeTotal);
            materialEditando.setDataValidade(dataValidade);
            materialEditando.setEstado(estado);
            materialEditando.setAtivo(ativo);
            
            service.salvar(materialEditando);
            
            showInfo("Material salvo com sucesso!");
            fecharJanela();
            
        } catch (Exception e) {
            showError("Erro ao salvar material: " + e.getMessage());
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
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }
}
