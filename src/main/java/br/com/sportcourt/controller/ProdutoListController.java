package br.com.sportcourt.controller;

import br.com.sportcourt.model.Produto;
import br.com.sportcourt.service.ProdutoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ProdutoListController extends BaseController {

    @FXML
    private TableView<Produto> tableProdutos;
    @FXML
    private TableColumn<Produto, Integer> colId;
    @FXML
    private TableColumn<Produto, String> colNome;
    @FXML
    private TableColumn<Produto, Double> colPreco;
    @FXML
    private TableColumn<Produto, String> colCategoria;
    @FXML
    private TableColumn<Produto, String> colAtivo;

    private final ProdutoService service = new ProdutoService();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarDados();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId())
                        .asObject());

        colNome.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNome()));

        colPreco.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPreco())
                        .asObject());

        colCategoria.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategoria()));

        colAtivo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().isAtivo() ? "✅ Ativo" : "❌ Inativo"));
    }

    private void carregarDados() {
        try {
            tableProdutos.setItems(FXCollections.observableArrayList(service.listar()));
        } catch (Exception e) {
            showError("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    @FXML
    public void onCadastrar() {
        abrirFormulario(null);
    }

    @FXML
    public void onEditar() {
        Produto produto = tableProdutos.getSelectionModel().getSelectedItem();
        if (produto == null) {
            showWarning("Selecione um produto para editar!");
            return;
        }
        abrirFormulario(produto);
    }

    @FXML
    public void onExcluir() {
        Produto produto = tableProdutos.getSelectionModel().getSelectedItem();
        if (produto == null) {
            showWarning("Selecione um produto para excluir!");
            return;
        }

        if (confirmAction("Confirmar Exclusão",
                "Deseja realmente excluir o produto \"" + produto.getNome() + "\"?")) {

            try {
                service.remover(produto.getId());
                showInfo("Produto excluído com sucesso!");
                carregarDados();
            } catch (Exception e) {
                showError("Erro ao excluir produto: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onAtualizar() {
        carregarDados();
        showInfo("Dados atualizados!");
    }

    private void abrirFormulario(Produto produto) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/ProdutoFormView.fxml"));
            Parent root = loader.load();

            ProdutoFormController controller = loader.getController();
            controller.setProduto(produto);

            Stage stage = new Stage();
            stage.setTitle(produto == null ? "Novo Produto" : "Editar Produto");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            carregarDados();

        } catch (Exception e) {
            showError("Erro ao abrir formulário: " + e.getMessage());
        }
    }
}
