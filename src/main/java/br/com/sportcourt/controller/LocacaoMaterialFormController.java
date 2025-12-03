// File: src/main/java/br/com/sportcourt/controller/LocacaoMaterialFormController.java
package br.com.sportcourt.controller;

import br.com.sportcourt.model.LocacaoMaterial;
import br.com.sportcourt.model.MaterialEsportivo;
import br.com.sportcourt.service.LocacaoMaterialService;
import br.com.sportcourt.service.MaterialEsportivoService;
import br.com.sportcourt.service.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class LocacaoMaterialFormController extends BaseController {

    @FXML private Label lblMaterialNome;
    @FXML private Label lblValorLocacao;
    @FXML private Label lblValorCaucao;
    @FXML private Label lblQuantidadeDisponivel;
    
    @FXML private TextField txtClienteNome;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtHorasLocacao;
    @FXML private DatePicker dateDataLocacao;
    @FXML private ComboBox<String> comboHoraInicio;
    @FXML private TextArea txtObservacoes;
    
    @FXML private Label lblValorTotal;
    @FXML private Label lblCaucaoTotal;
    @FXML private Label lblPrevisaoDevolucao;

    private MaterialEsportivo materialSelecionado;
    private final MaterialEsportivoService materialService = new MaterialEsportivoService();
    private final LocacaoMaterialService locacaoService = new LocacaoMaterialService();

    @FXML
    public void initialize() {
        configurarHoras();
        configurarListeners();
        dateDataLocacao.setValue(java.time.LocalDate.now());
    }

    private void configurarHoras() {
        for (int i = 8; i <= 22; i++) {
            for (int j = 0; j < 60; j += 30) {
                String hora = String.format("%02d:%02d", i, j);
                comboHoraInicio.getItems().add(hora);
            }
        }
        comboHoraInicio.getSelectionModel().selectFirst();
    }

    private void configurarListeners() {
        txtQuantidade.textProperty().addListener((obs, oldVal, newVal) -> {
            calcularValores();
        });
        
        txtHorasLocacao.textProperty().addListener((obs, oldVal, newVal) -> {
            calcularValores();
        });
        
        dateDataLocacao.valueProperty().addListener((obs, oldVal, newVal) -> {
            calcularPrevisaoDevolucao();
        });
        
        comboHoraInicio.valueProperty().addListener((obs, oldVal, newVal) -> {
            calcularPrevisaoDevolucao();
        });
    }

    public void setMaterial(MaterialEsportivo material) {
        this.materialSelecionado = material;
        
        if (material != null) {
            lblMaterialNome.setText(material.getNome());
            lblValorLocacao.setText(String.format("R$ %.2f/hora", material.getValorLocacao()));
            lblValorCaucao.setText(String.format("R$ %.2f/unidade", material.getValorCaucao()));
            lblQuantidadeDisponivel.setText(String.valueOf(material.getQuantidadeDisponivel()));
            
            txtQuantidade.setText("1");
            txtHorasLocacao.setText("2");
            
            calcularValores();
            calcularPrevisaoDevolucao();
        }
    }

    private void calcularValores() {
        try {
            if (materialSelecionado == null) return;
            
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            int horas = Integer.parseInt(txtHorasLocacao.getText());
            
            if (quantidade <= 0 || horas <= 0) return;
            
            double valorTotal = materialSelecionado.getValorLocacao() * quantidade * horas;
            double caucaoTotal = materialSelecionado.getValorCaucao() * quantidade;
            
            lblValorTotal.setText(String.format("R$ %.2f", valorTotal));
            lblCaucaoTotal.setText(String.format("R$ %.2f", caucaoTotal));
            
        } catch (NumberFormatException e) {
            // Ignora erro enquanto usuário digita
        }
    }

    private void calcularPrevisaoDevolucao() {
        try {
            if (dateDataLocacao.getValue() == null || comboHoraInicio.getValue() == null) return;
            
            String[] horaParts = comboHoraInicio.getValue().split(":");
            int hora = Integer.parseInt(horaParts[0]);
            int minuto = Integer.parseInt(horaParts[1]);
            
            LocalDateTime dataInicio = dateDataLocacao.getValue().atTime(hora, minuto);
            
            try {
                int horasLocacao = Integer.parseInt(txtHorasLocacao.getText());
                LocalDateTime previsao = dataInicio.plusHours(horasLocacao);
                
                lblPrevisaoDevolucao.setText(previsao.format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            } catch (NumberFormatException e) {
                lblPrevisaoDevolucao.setText("-");
            }
            
        } catch (Exception e) {
            lblPrevisaoDevolucao.setText("-");
        }
    }

    @FXML
    public void onSalvar() {
        try {
            // Validações
            if (materialSelecionado == null) {
                showError("Material não selecionado!");
                return;
            }
            
            if (txtClienteNome.getText().trim().isEmpty()) {
                showError("Nome do cliente é obrigatório!");
                txtClienteNome.requestFocus();
                return;
            }
            
            if (txtTelefone.getText().trim().isEmpty()) {
                showError("Telefone do cliente é obrigatório!");
                txtTelefone.requestFocus();
                return;
            }
            
            int quantidade;
            int horasLocacao;
            
            try {
                quantidade = Integer.parseInt(txtQuantidade.getText());
                if (quantidade <= 0) {
                    showError("Quantidade deve ser maior que zero!");
                    return;
                }
                if (quantidade > materialSelecionado.getQuantidadeDisponivel()) {
                    showError("Quantidade indisponível! Disponível: " + 
                             materialSelecionado.getQuantidadeDisponivel());
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Quantidade inválida!");
                return;
            }
            
            try {
                horasLocacao = Integer.parseInt(txtHorasLocacao.getText());
                if (horasLocacao <= 0) {
                    showError("Horas de locação devem ser maiores que zero!");
                    return;
                }
                if (horasLocacao > 24) {
                    if (!confirmAction("Confirmação", 
                            "Tem certeza que deseja locar por mais de 24 horas?")) {
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                showError("Horas de locação inválidas!");
                return;
            }
            
            if (dateDataLocacao.getValue() == null) {
                showError("Data da locação é obrigatória!");
                return;
            }
            
            if (comboHoraInicio.getValue() == null) {
                showError("Hora de início é obrigatória!");
                return;
            }
            
            // Criar locação
            LocacaoMaterial locacao = new LocacaoMaterial();
            locacao.setMaterialId(materialSelecionado.getId());
            locacao.setClienteNome(txtClienteNome.getText().trim());
            locacao.setTelefoneCliente(txtTelefone.getText().trim());
            locacao.setEmailCliente(txtEmail.getText().trim());
            locacao.setQuantidade(quantidade);
            
            // Configurar data e hora
            String[] horaParts = comboHoraInicio.getValue().split(":");
            int hora = Integer.parseInt(horaParts[0]);
            int minuto = Integer.parseInt(horaParts[1]);
            
            LocalDateTime dataAbertura = dateDataLocacao.getValue().atTime(hora, minuto);
            locacao.setDataAbertura(dataAbertura);
            locacao.setDataFechamentoPrevisto(dataAbertura.plusHours(horasLocacao));
            
            locacao.setObservacoes(txtObservacoes.getText().trim());
            
            // Obter usuário logado
            var usuario = Session.getUsuarioLogado();
            if (usuario != null) {
                locacao.setAtendente(usuario.getNome());
            }
            
            // Criar locação
            LocacaoMaterial locacaoCriada = locacaoService.criarLocacao(locacao);
            
            if (locacaoCriada != null) {
                showInfo("Locação criada com sucesso!\n" +
                        "Número: #" + locacaoCriada.getId() + "\n" +
                        "Valor total: R$ " + locacaoCriada.getValorTotal() + "\n" +
                        "Caução: R$ " + locacaoCriada.getValorCaucao());
                fecharJanela();
            } else {
                showError("Erro ao criar locação!");
            }
            
        } catch (Exception e) {
            showError("Erro ao salvar locação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancelar() {
        if (confirmAction("Cancelar", "Deseja realmente cancelar? As informações serão perdidas.")) {
            fecharJanela();
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) lblMaterialNome.getScene().getWindow();
        stage.close();
    }
}
