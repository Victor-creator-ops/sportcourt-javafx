package br.com.sportcourt.controller;

import br.com.sportcourt.model.Quadra;
import br.com.sportcourt.service.QuadraService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class QuadraListController extends BaseController {

    @FXML
    private TableView<Quadra> tableQuadras;
    @FXML
    private TableColumn<Quadra, String> colCodigo;
    @FXML
    private TableColumn<Quadra, String> colTipo;
    @FXML
    private TableColumn<Quadra, Double> colValorHora;
    @FXML
    private TableColumn<Quadra, String> colStatus;

    private QuadraService service = new QuadraService();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarDados();
    }

    private void configurarColunas() {
        colCodigo.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));

        colTipo.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTipo()));

        colValorHora.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getValorHora())
                        .asObject());

        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDisponivel() ? "Disponível" : "Indisponível"));
    }

    private void carregarDados() {
        try {
            tableQuadras.setItems(FXCollections.observableArrayList(service.listar()));
        } catch (Exception e) {
            showError("Erro ao carregar quadras: " + e.getMessage());
        }
    }

    @FXML
    private void onCadastrar() {
        abrirFormulario(null);
    }

    @FXML
    private void onEditar() {
        Quadra selecionada = tableQuadras.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            showWarning("Selecione uma quadra para editar!");
            return;
        }
        abrirFormulario(selecionada);
    }

    @FXML
    private void onExcluir() {
        Quadra selecionada = tableQuadras.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            showWarning("Selecione uma quadra para excluir!");
            return;
        }

        if (confirmAction("Confirmar Exclusão",
                "Deseja realmente excluir a quadra " + selecionada.getId() + "?")) {

            try {
                service.excluir(selecionada.getId());
                showInfo("Quadra excluída com sucesso!");
                carregarDados();
            } catch (Exception e) {
                showError("Erro ao excluir quadra: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onAtualizar() {
        carregarDados();
    }

    private void abrirFormulario(Quadra quadra) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/QuadraFormView.fxml"));
            Parent root = loader.load();

            QuadraFormController controller = loader.getController();
            if (quadra != null) {
                controller.setQuadraParaEdicao(quadra);
            }

            Stage stage = new Stage();
            stage.setTitle(quadra == null ? "Nova Quadra" : "Editar Quadra");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            carregarDados();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erro ao abrir formulário: " + e.getMessage());
        }
    }
}
