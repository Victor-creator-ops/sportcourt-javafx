package br.com.sportcourt.controller;

import br.com.sportcourt.model.FinanceiroMovimento;
import br.com.sportcourt.service.FinanceiroService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.text.DecimalFormat;

public class FinanceiroListController extends BaseController {

        @FXML
        private Label lblEntradas;
        @FXML
        private Label lblSaidas;
        @FXML
        private Label lblSaldo;

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
                configurarColunas();
                carregarDados();
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
                        tableMovimentos.setItems(
                                        FXCollections.observableArrayList(service.listar()));
                        carregarResumo();
                        carregarGrafico();
                } catch (Exception e) {
                        showError("Erro ao carregar financeiro: " + e.getMessage());
                }
        }

        private void carregarResumo() {
                double entradas =
                                service.listar().stream().filter(m -> m.getTipo().equals("ENTRADA"))
                                                .mapToDouble(FinanceiroMovimento::getValor).sum();

                double saidas = service.listar().stream().filter(m -> m.getTipo().equals("SAIDA"))
                                .mapToDouble(FinanceiroMovimento::getValor).sum();

                double saldo = entradas - saidas;

                lblEntradas.setText(df.format(entradas));
                lblSaidas.setText(df.format(saidas));
                lblSaldo.setText(df.format(saldo));
        }

        private void carregarGrafico() {
                try {
                        chartFluxo.getData().clear();

                        XYChart.Series<String, Number> serie = new XYChart.Series<>();
                        serie.setName("Fluxo de Caixa");

                        for (FinanceiroMovimento m : service.listar()) {
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
                carregarDados();
                showInfo("Dados atualizados!");
        }
}
