package br.com.sportcourt.controller;

import br.com.sportcourt.model.FinanceiroMovimento;
import br.com.sportcourt.service.FinanceiroService;
import br.com.sportcourt.service.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;

import java.text.DecimalFormat;
import java.util.List;

public class FinanceiroListController extends BaseController {

        @FXML
        private Label lblEntradas;
        @FXML
        private Label lblSaidas;
        @FXML
        private Label lblSaldo;

        @FXML
        private Button btnNovaEntrada;
        @FXML
        private Button btnNovaSaida;

        @FXML
        private TableView<FinanceiroMovimento> tableMovimentos;
        @FXML
        private TableColumn<FinanceiroMovimento, String> colTipo;
        @FXML
        private TableColumn<FinanceiroMovimento, String> colOrigem;
        @FXML
        private TableColumn<FinanceiroMovimento, Double> colValor;
        @FXML
        private TableColumn<FinanceiroMovimento, String> colData;

        @FXML
        private AreaChart<String, Number> chartFluxo;

        private FinanceiroService service = new FinanceiroService();
        private DecimalFormat df = new DecimalFormat("R$ #,##0.00");

        @FXML
        public void initialize() {
                if (!podeUsarFinanceiro()) {
                        bloquearAcesso();
                        return;
                }
                configurarColunas();
                carregarDados();
        }

        private boolean podeUsarFinanceiro() {
                var u = Session.getUsuarioLogado();
                return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
        }

        private void bloquearAcesso() {
                if (btnNovaEntrada != null) {
                        btnNovaEntrada.setDisable(true);
                }
                if (btnNovaSaida != null) {
                        btnNovaSaida.setDisable(true);
                }
                if (tableMovimentos != null) {
                        tableMovimentos.setDisable(true);
                }
                if (chartFluxo != null) {
                        chartFluxo.setDisable(true);
                }
                showWarning("Acesso ao financeiro restrito a administradores.");
        }

        private void configurarColunas() {
                colTipo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                                c.getValue().getTipo()));
                colOrigem.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                                c.getValue().getOrigem()));
                colValor.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(
                                c.getValue().getValor()).asObject());
                colData.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                                c.getValue().getDataHora().toString()));
        }

        private void carregarDados() {
                try {
                        var movimentos = service.listar();
                        tableMovimentos.setItems(
                                        FXCollections.observableArrayList(movimentos));
                        carregarResumo(movimentos);
                        carregarGrafico(movimentos);
                } catch (Exception e) {
                        showError("Erro ao carregar financeiro: " + e.getMessage());
                }
        }

        private void carregarResumo(List<FinanceiroMovimento> movimentos) {
                double entradas = movimentos.stream().filter(m -> m.getTipo().equals("ENTRADA"))
                                .mapToDouble(FinanceiroMovimento::getValor).sum();

                double saidas = movimentos.stream().filter(m -> m.getTipo().equals("SAIDA"))
                                .mapToDouble(FinanceiroMovimento::getValor).sum();

                double saldo = entradas - saidas;

                lblEntradas.setText(df.format(entradas));
                lblSaidas.setText(df.format(saidas));
                lblSaldo.setText(df.format(saldo));
        }

        private void carregarGrafico(List<FinanceiroMovimento> movimentos) {
                try {
                        chartFluxo.getData().clear();

                        XYChart.Series<String, Number> serie = new XYChart.Series<>();
                        serie.setName("Fluxo de Caixa");

                        for (FinanceiroMovimento m : movimentos) {
                                serie.getData().add(new XYChart.Data<>(
                                                m.getDataHora().toLocalDate().toString(),
                                                m.getValor()));
                        }

                        chartFluxo.getData().add(serie);
                } catch (Exception e) {
                        System.out.println("Não foi possível carregar gráfico: " + e.getMessage());
                }
        }

        @FXML
        public void onAtualizar() {
                if (!podeUsarFinanceiro()) {
                        return;
                }
                carregarDados();
                showInfo("Dados atualizados!");
        }

        @FXML
        public void onNovaEntrada() {
                registrarMovimento("ENTRADA");
        }

        @FXML
        public void onNovaSaida() {
                registrarMovimento("SAIDA");
        }

        private void registrarMovimento(String tipo) {
                if (!podeUsarFinanceiro()) {
                        showWarning("Acesso ao financeiro restrito a administradores.");
                        return;
                }

                TextInputDialog origemDialog = new TextInputDialog();
                origemDialog.setTitle("Novo movimento");
                origemDialog.setHeaderText(tipo.equals("ENTRADA") ? "Registrar entrada"
                                : "Registrar saída");
                origemDialog.setContentText("Origem:");

                var origem = origemDialog.showAndWait();
                if (origem.isEmpty() || origem.get().isBlank()) {
                        showWarning("Informe a origem do movimento.");
                        return;
                }

                TextInputDialog valorDialog = new TextInputDialog();
                valorDialog.setTitle("Valor");
                valorDialog.setHeaderText("Informe o valor");
                valorDialog.setContentText("Valor em R$:");

                var valorStr = valorDialog.showAndWait();
                if (valorStr.isEmpty()) {
                        return;
                }

                double valor;
                try {
                        valor = Double.parseDouble(valorStr.get().replace(",", "."));
                } catch (NumberFormatException e) {
                        showError("Valor inválido.");
                        return;
                }

                if (valor <= 0) {
                        showWarning("O valor deve ser maior que zero.");
                        return;
                }

                if (tipo.equals("ENTRADA")) {
                        service.registrarEntrada(origem.get().trim(), valor);
                } else {
                        service.registrarSaida(origem.get().trim(), valor);
                }

                carregarDados();
                showInfo("Movimento registrado.");
        }
}
