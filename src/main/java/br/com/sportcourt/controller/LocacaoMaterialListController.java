// File: src/main/java/br/com/sportcourt/controller/LocacaoMaterialListController.java
package br.com.sportcourt.controller;

import br.com.sportcourt.model.LocacaoMaterial;
import br.com.sportcourt.model.MaterialEsportivo;
import br.com.sportcourt.service.LocacaoMaterialService;
import br.com.sportcourt.service.MaterialEsportivoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class LocacaoMaterialListController extends BaseController {

    @FXML
    private TableView<LocacaoMaterial> tableLocacoes;
    @FXML
    private TableColumn<LocacaoMaterial, Integer> colId;
    @FXML
    private TableColumn<LocacaoMaterial, String> colCliente;
    @FXML
    private TableColumn<LocacaoMaterial, String> colMaterial;
    @FXML
    private TableColumn<LocacaoMaterial, Integer> colQuantidade;
    @FXML
    private TableColumn<LocacaoMaterial, String> colDataAbertura;
    @FXML
    private TableColumn<LocacaoMaterial, String> colDataPrevista;
    @FXML
    private TableColumn<LocacaoMaterial, String> colDataReal;
    @FXML
    private TableColumn<LocacaoMaterial, Double> colValorTotal;
    @FXML
    private TableColumn<LocacaoMaterial, String> colStatus;

    @FXML
    private ComboBox<String> comboFiltroStatus;
    @FXML
    private TextField txtBuscaCliente;

    private final LocacaoMaterialService service = new LocacaoMaterialService();
    private final MaterialEsportivoService materialService = new MaterialEsportivoService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private ObservableList<LocacaoMaterial> dadosOriginais;
    private FilteredList<LocacaoMaterial> dadosFiltrados;

    @FXML
    public void initialize() {
        configurarColunas();
        configurarFiltros();
        carregarDados();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId())
                        .asObject());

        colCliente.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getClienteNome()));

        colMaterial.setCellValueFactory(c -> {
            MaterialEsportivo material = materialService.buscarPorId(c.getValue().getMaterialId());
            return new javafx.beans.property.SimpleStringProperty(
                    material != null ? material.getNome() : "N/A");
        });

        colQuantidade.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantidade())
                        .asObject());

        colDataAbertura.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDataAbertura().format(formatter)));

        colDataPrevista.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDataFechamentoPrevisto().format(formatter)));

        colDataReal.setCellValueFactory(c -> {
            if (c.getValue().getDataFechamentoReal() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getDataFechamentoReal().format(formatter));
            } else {
                return new javafx.beans.property.SimpleStringProperty("-");
            }
        });

        colValorTotal.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(
                c.getValue().getValorTotal() + c.getValue().getMultaAtraso()).asObject());

        colStatus.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        // Formatação condicional para locações atrasadas
        tableLocacoes.setRowFactory(tv -> new TableRow<LocacaoMaterial>() {
            @Override
            protected void updateItem(LocacaoMaterial locacao, boolean empty) {
                super.updateItem(locacao, empty);
                if (locacao == null || empty) {
                    setStyle("");
                } else if (locacao.isAtrasada()) {
                    setStyle("-fx-background-color: #ffe6e6; -fx-font-weight: bold;"); // Vermelho
                } else if ("FECHADA".equals(locacao.getStatus())) {
                    setStyle("-fx-background-color: #e6ffe6;"); // Verde
                } else if ("CANCELADA".equals(locacao.getStatus())) {
                    setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #999;"); // Cinza
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void configurarFiltros() {
        comboFiltroStatus.getItems().addAll("TODAS", "ABERTA", "FECHADA", "CANCELADA");
        comboFiltroStatus.getSelectionModel().selectFirst();
    }

    private void carregarDados() {
        try {
            List<LocacaoMaterial> locacoes = service.listarLocacoes();
            dadosOriginais = FXCollections.observableArrayList(locacoes);
            dadosFiltrados = new FilteredList<>(dadosOriginais, p -> true);

            // Configurar filtro de busca
            txtBuscaCliente.textProperty().addListener((observable, oldValue, newValue) -> {
                aplicarFiltros();
            });

            // Configurar filtro de status
            comboFiltroStatus.valueProperty().addListener((observable, oldValue, newValue) -> {
                aplicarFiltros();
            });

            // Aplicar filtros à tabela
            SortedList<LocacaoMaterial> sortedData = new SortedList<>(dadosFiltrados);
            sortedData.comparatorProperty().bind(tableLocacoes.comparatorProperty());
            tableLocacoes.setItems(sortedData);

        } catch (Exception e) {
            showError("Erro ao carregar locações: " + e.getMessage());
        }
    }

    private void aplicarFiltros() {
        String busca = txtBuscaCliente.getText().toLowerCase();
        String status = comboFiltroStatus.getValue();

        dadosFiltrados.setPredicate(locacao -> {
            // Filtro por busca no nome do cliente
            if (busca != null && !busca.isEmpty()) {
                if (!locacao.getClienteNome().toLowerCase().contains(busca)) {
                    return false;
                }
            }

            // Filtro por status
            if (status != null && !"TODAS".equals(status)) {
                if (!status.equals(locacao.getStatus())) {
                    return false;
                }
            }

            return true;
        });
    }

    @FXML
    public void onDevolver() {
        LocacaoMaterial locacao = tableLocacoes.getSelectionModel().getSelectedItem();
        if (locacao == null) {
            showWarning("Selecione uma locação para devolver!");
            return;
        }

        if (!"ABERTA".equals(locacao.getStatus())) {
            showWarning("Esta locação já foi finalizada ou cancelada!");
            return;
        }

        // Diálogo para estado do material
        ChoiceDialog<String> estadoDialog =
                new ChoiceDialog<>("BOM", "BOM", "REGULAR", "RUIM", "PÉSSIMO");
        estadoDialog.setTitle("Devolução de Material");
        estadoDialog.setHeaderText("Estado do material ao devolver");
        estadoDialog.setContentText("Selecione o estado:");

        estadoDialog.showAndWait().ifPresent(estado -> {
            // Diálogo para observações
            TextInputDialog obsDialog = new TextInputDialog();
            obsDialog.setTitle("Observações");
            obsDialog.setHeaderText("Observações da devolução");
            obsDialog.setContentText("Observações:");

            obsDialog.showAndWait().ifPresent(observacoes -> {
                try {
                    if (service.finalizarLocacao(locacao.getId(), estado, observacoes)) {
                        double totalPago = locacao.getValorTotal() + locacao.getMultaAtraso();
                        showInfo("Locação #" + locacao.getId() + " finalizada!\n"
                                + "Valor total: R$ " + totalPago + "\n" + "Caução devolvida: R$ "
                                + locacao.getValorCaucao());
                        carregarDados();
                    } else {
                        showError("Erro ao finalizar locação!");
                    }
                } catch (Exception e) {
                    showError("Erro: " + e.getMessage());
                }
            });
        });
    }

    @FXML
    public void onCancelar() {
        LocacaoMaterial locacao = tableLocacoes.getSelectionModel().getSelectedItem();
        if (locacao == null) {
            showWarning("Selecione uma locação para cancelar!");
            return;
        }

        if (!"ABERTA".equals(locacao.getStatus())) {
            showWarning("Só é possível cancelar locações abertas!");
            return;
        }

        TextInputDialog motivoDialog = new TextInputDialog();
        motivoDialog.setTitle("Cancelar Locação");
        motivoDialog.setHeaderText("Informe o motivo do cancelamento");
        motivoDialog.setContentText("Motivo:");

        motivoDialog.showAndWait().ifPresent(motivo -> {
            if (confirmAction("Confirmar Cancelamento",
                    "Deseja realmente cancelar a locação #" + locacao.getId() + "?")) {

                if (service.cancelarLocacao(locacao.getId(), motivo)) {
                    showInfo("Locação cancelada! Caução devolvida.");
                    carregarDados();
                } else {
                    showError("Erro ao cancelar locação!");
                }
            }
        });
    }

    @FXML
    public void onVerDetalhes() {
        LocacaoMaterial locacao = tableLocacoes.getSelectionModel().getSelectedItem();
        if (locacao == null) {
            showWarning("Selecione uma locação para ver detalhes!");
            return;
        }

        MaterialEsportivo material = materialService.buscarPorId(locacao.getMaterialId());
        StringBuilder detalhes = new StringBuilder();

        detalhes.append("=== DETALHES DA LOCAÇÃO #").append(locacao.getId()).append(" ===\n\n");
        detalhes.append("Cliente: ").append(locacao.getClienteNome()).append("\n");
        detalhes.append("Telefone: ").append(locacao.getTelefoneCliente()).append("\n");
        detalhes.append("Email: ").append(locacao.getEmailCliente()).append("\n\n");

        detalhes.append("Material: ").append(material != null ? material.getNome() : "N/A")
                .append("\n");
        detalhes.append("Quantidade: ").append(locacao.getQuantidade()).append("\n");
        detalhes.append("Valor locação/hora: R$ ")
                .append(material != null ? material.getValorLocacao() : 0).append("\n\n");

        detalhes.append("Data abertura: ").append(locacao.getDataAbertura().format(formatter))
                .append("\n");
        detalhes.append("Previsão devolução: ")
                .append(locacao.getDataFechamentoPrevisto().format(formatter)).append("\n");

        if (locacao.getDataFechamentoReal() != null) {
            detalhes.append("Devolução real: ")
                    .append(locacao.getDataFechamentoReal().format(formatter)).append("\n");
        }

        detalhes.append("\n=== VALORES ===\n");
        detalhes.append("Valor total: R$ ").append(String.format("%.2f", locacao.getValorTotal()))
                .append("\n");
        detalhes.append("Caução: R$ ").append(String.format("%.2f", locacao.getValorCaucao()))
                .append("\n");
        detalhes.append("Multa atraso: R$ ").append(String.format("%.2f", locacao.getMultaAtraso()))
                .append("\n");
        detalhes.append("Valor pendente: R$ ")
                .append(String.format("%.2f", locacao.getValorPendente())).append("\n\n");

        detalhes.append("Status: ").append(locacao.getStatus()).append("\n");
        if (locacao.isCheckinRealizado()) {
            detalhes.append("✓ Check-in realizado\n");
        }
        if (locacao.getObservacoes() != null && !locacao.getObservacoes().isEmpty()) {
            detalhes.append("\nObservações:\n").append(locacao.getObservacoes());
        }

        TextArea textArea = new TextArea(detalhes.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(500, 400);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);

        Stage stage = new Stage();
        stage.setTitle("Detalhes da Locação #" + locacao.getId());
        stage.setScene(new javafx.scene.Scene(scrollPane));
        stage.show();
    }

    @FXML
    public void onAtualizar() {
        carregarDados();
        showInfo("Dados atualizados!");
    }

    @FXML
    public void onExportarRelatorio() {
        List<LocacaoMaterial> locacoes = tableLocacoes.getItems();
        if (locacoes.isEmpty()) {
            showWarning("Não há dados para exportar!");
            return;
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATÓRIO DE LOCAÇÕES DE MATERIAIS\n");
        relatorio.append("===================================\n\n");

        double totalReceita = 0;
        double totalMultas = 0;
        int locacoesAbertas = 0;
        int locacoesAtrasadas = 0;

        for (LocacaoMaterial locacao : locacoes) {
            relatorio.append("Locação #").append(locacao.getId()).append("\n");
            relatorio.append("Cliente: ").append(locacao.getClienteNome()).append("\n");
            relatorio.append("Status: ").append(locacao.getStatus()).append("\n");
            relatorio.append("Valor: R$ ").append(String.format("%.2f", locacao.getValorTotal()))
                    .append("\n");
            relatorio.append("Multa: R$ ").append(String.format("%.2f", locacao.getMultaAtraso()))
                    .append("\n");
            relatorio.append("---\n");

            totalReceita += locacao.getValorTotal();
            totalMultas += locacao.getMultaAtraso();

            if ("ABERTA".equals(locacao.getStatus())) {
                locacoesAbertas++;
                if (locacao.isAtrasada()) {
                    locacoesAtrasadas++;
                }
            }
        }

        relatorio.append("\n=== RESUMO ===\n");
        relatorio.append("Total de locações: ").append(locacoes.size()).append("\n");
        relatorio.append("Locações abertas: ").append(locacoesAbertas).append("\n");
        relatorio.append("Locações atrasadas: ").append(locacoesAtrasadas).append("\n");
        relatorio.append("Total receita: R$ ").append(String.format("%.2f", totalReceita))
                .append("\n");
        relatorio.append("Total multas: R$ ").append(String.format("%.2f", totalMultas))
                .append("\n");

        // Mostrar relatório
        TextArea textArea = new TextArea(relatorio.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(600, 500);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);

        Stage stage = new Stage();
        stage.setTitle("Relatório de Locações");

        // Botão para copiar
        Button btnCopiar = new Button("Copiar para Área de Transferência");
        btnCopiar.setOnAction(e -> {
            javafx.scene.input.Clipboard clipboard =
                    javafx.scene.input.Clipboard.getSystemClipboard();
            javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            content.putString(relatorio.toString());
            clipboard.setContent(content);
            showInfo("Relatório copiado!");
        });

        VBox vbox = new VBox(10, scrollPane, btnCopiar);
        vbox.setPadding(new javafx.geometry.Insets(10));

        stage.setScene(new javafx.scene.Scene(vbox));
        stage.show();
    }
}
