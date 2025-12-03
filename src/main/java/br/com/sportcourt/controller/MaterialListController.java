// File: src/main/java/br/com/sportcourt/controller/MaterialListController.java
package br.com.sportcourt.controller;

import br.com.sportcourt.model.MaterialEsportivo;
import br.com.sportcourt.service.MaterialEsportivoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class MaterialListController extends BaseController {

    @FXML
    private TableView<MaterialEsportivo> tableMateriais;
    @FXML
    private TableColumn<MaterialEsportivo, Integer> colId;
    @FXML
    private TableColumn<MaterialEsportivo, String> colNome;
    @FXML
    private TableColumn<MaterialEsportivo, String> colCategoria;
    @FXML
    private TableColumn<MaterialEsportivo, Double> colValorLocacao;
    @FXML
    private TableColumn<MaterialEsportivo, Integer> colQuantidadeTotal;
    @FXML
    private TableColumn<MaterialEsportivo, Integer> colQuantidadeDisponivel;
    @FXML
    private TableColumn<MaterialEsportivo, String> colEstado;
    @FXML
    private TableColumn<MaterialEsportivo, String> colAtivo;

    @FXML
    private ComboBox<String> comboFiltroCategoria;
    @FXML
    private TextField txtBusca;

    private final MaterialEsportivoService service = new MaterialEsportivoService();
    private ObservableList<MaterialEsportivo> dadosOriginais;
    private FilteredList<MaterialEsportivo> dadosFiltrados;

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

        colNome.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNome()));

        colCategoria.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategoria()));

        colValorLocacao.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getValorLocacao())
                        .asObject());

        colQuantidadeTotal.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(
                c.getValue().getQuantidadeTotal()).asObject());

        colQuantidadeDisponivel
                .setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(
                        c.getValue().getQuantidadeDisponivel()).asObject());

        colEstado.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEstado()));

        colAtivo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().isAtivo() ? "✅ Ativo" : "❌ Inativo"));

        // Adicionar formatação condicional
        tableMateriais.setRowFactory(tv -> new TableRow<MaterialEsportivo>() {
            @Override
            protected void updateItem(MaterialEsportivo material, boolean empty) {
                super.updateItem(material, empty);
                if (material == null || empty) {
                    setStyle("");
                } else if (material.getQuantidadeDisponivel() == 0) {
                    setStyle("-fx-background-color: #ffe6e6;"); // Vermelho claro
                } else if (material.getQuantidadeDisponivel() < 3) {
                    setStyle("-fx-background-color: #fff5e6;"); // Laranja claro
                } else if (material.isVencido()) {
                    setStyle("-fx-background-color: #e6f3ff; -fx-text-fill: #0066cc;"); // Azul
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void configurarFiltros() {
        comboFiltroCategoria.getItems().addAll("TODOS", "BOLA", "REDE", "UNIFORME", "EQUIPAMENTO",
                "PROTEÇÃO", "ACESSÓRIO");
        comboFiltroCategoria.getSelectionModel().selectFirst();
    }

    private void carregarDados() {
        try {
            List<MaterialEsportivo> materiais = service.listar();
            dadosOriginais = FXCollections.observableArrayList(materiais);
            dadosFiltrados = new FilteredList<>(dadosOriginais, p -> true);

            // Configurar filtro de busca
            txtBusca.textProperty().addListener((observable, oldValue, newValue) -> {
                aplicarFiltros();
            });

            // Configurar filtro de categoria
            comboFiltroCategoria.valueProperty().addListener((observable, oldValue, newValue) -> {
                aplicarFiltros();
            });

            // Aplicar filtros à tabela
            SortedList<MaterialEsportivo> sortedData = new SortedList<>(dadosFiltrados);
            sortedData.comparatorProperty().bind(tableMateriais.comparatorProperty());
            tableMateriais.setItems(sortedData);

            atualizarEstatisticas();

        } catch (Exception e) {
            showError("Erro ao carregar materiais: " + e.getMessage());
        }
    }

    private void aplicarFiltros() {
        String busca = txtBusca.getText().toLowerCase();
        String categoria = comboFiltroCategoria.getValue();

        dadosFiltrados.setPredicate(material -> {
            // Filtro por busca
            if (busca != null && !busca.isEmpty()) {
                boolean correspondeBusca = material.getNome().toLowerCase().contains(busca)
                        || material.getDescricao().toLowerCase().contains(busca)
                        || material.getCategoria().toLowerCase().contains(busca);
                if (!correspondeBusca) {
                    return false;
                }
            }

            // Filtro por categoria
            if (categoria != null && !"TODOS".equals(categoria)) {
                if (!categoria.equals(material.getCategoria())) {
                    return false;
                }
            }

            return true;
        });

        atualizarEstatisticas();
    }

    private void atualizarEstatisticas() {
        // Implementar se necessário
    }

    @FXML
    public void onCadastrar() {
        abrirFormulario(null);
    }

    @FXML
    public void onEditar() {
        MaterialEsportivo material = tableMateriais.getSelectionModel().getSelectedItem();
        if (material == null) {
            showWarning("Selecione um material para editar!");
            return;
        }
        abrirFormulario(material);
    }

    @FXML
    public void onExcluir() {
        MaterialEsportivo material = tableMateriais.getSelectionModel().getSelectedItem();
        if (material == null) {
            showWarning("Selecione um material para excluir!");
            return;
        }

        if (confirmAction("Confirmar Exclusão",
                "Deseja realmente excluir o material \"" + material.getNome() + "\"?")) {

            try {
                if (service.remover(material.getId())) {
                    showInfo("Material excluído com sucesso!");
                    carregarDados();
                } else {
                    showError("Não foi possível excluir o material.");
                }
            } catch (Exception e) {
                showError("Erro ao excluir material: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onReporEstoque() {
        MaterialEsportivo material = tableMateriais.getSelectionModel().getSelectedItem();
        if (material == null) {
            showWarning("Selecione um material para repor estoque!");
            return;
        }

        // Diálogo para entrada da quantidade
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Repor Estoque");
        dialog.setHeaderText("Reposição de " + material.getNome());
        dialog.setContentText("Quantidade a adicionar:");

        dialog.showAndWait().ifPresent(quantidadeStr -> {
            try {
                int quantidade = Integer.parseInt(quantidadeStr);
                if (quantidade <= 0) {
                    showWarning("Informe uma quantidade positiva!");
                    return;
                }

                // Diálogo para novo valor de locação
                TextInputDialog valorDialog =
                        new TextInputDialog(String.valueOf(material.getValorLocacao()));
                valorDialog.setTitle("Atualizar Valor");
                valorDialog.setHeaderText("Valor de locação por hora");
                valorDialog.setContentText("Novo valor (R$):");

                valorDialog.showAndWait().ifPresent(valorStr -> {
                    try {
                        double novoValor = Double.parseDouble(valorStr);

                        // Atualizar material
                        material.setQuantidadeTotal(material.getQuantidadeTotal() + quantidade);
                        material.setQuantidadeDisponivel(
                                material.getQuantidadeDisponivel() + quantidade);
                        material.setValorLocacao(novoValor);

                        service.salvar(material);

                        showInfo("Estoque reposto! Total: " + material.getQuantidadeTotal()
                                + " unidades disponíveis.");
                        carregarDados();

                    } catch (NumberFormatException e) {
                        showError("Valor inválido!");
                    }
                });

            } catch (NumberFormatException e) {
                showError("Quantidade inválida!");
            }
        });
    }

    @FXML
    public void onNovaLocacao() {
        MaterialEsportivo material = tableMateriais.getSelectionModel().getSelectedItem();
        if (material == null) {
            showWarning("Selecione um material para locação!");
            return;
        }

        if (material.getQuantidadeDisponivel() == 0) {
            showError("Material não disponível para locação!");
            return;
        }

        abrirFormularioLocacao(material);
    }

    @FXML
    public void onVerLocacoes() {
        abrirListaLocacoes();
    }

    @FXML
    public void onAtualizar() {
        carregarDados();
        showInfo("Dados atualizados!");
    }

    private void abrirFormulario(MaterialEsportivo material) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/MaterialFormView.fxml"));
            Parent root = loader.load();

            MaterialFormController controller = loader.getController();
            controller.setMaterial(material);

            Stage stage = new Stage();
            stage.setTitle(material == null ? "Novo Material" : "Editar Material");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            carregarDados();

        } catch (Exception e) {
            showError("Erro ao abrir formulário: " + e.getMessage());
        }
    }

    private void abrirFormularioLocacao(MaterialEsportivo material) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/LocacaoMaterialFormView.fxml"));
            Parent root = loader.load();

            LocacaoMaterialFormController controller = loader.getController();
            controller.setMaterial(material);

            Stage stage = new Stage();
            stage.setTitle("Nova Locação - " + material.getNome());
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            carregarDados();

        } catch (Exception e) {
            showError("Erro ao abrir formulário de locação: " + e.getMessage());
        }
    }

    private void abrirListaLocacoes() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/LocacaoMaterialListView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Locações de Materiais");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            showError("Erro ao abrir lista de locações: " + e.getMessage());
        }
    }
}
