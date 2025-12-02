package br.com.sportcourt.controller;

import br.com.sportcourt.dao.ProdutoDAO;
import br.com.sportcourt.model.Comanda;
import br.com.sportcourt.model.ItemComanda;
import br.com.sportcourt.model.Produto;
import br.com.sportcourt.service.ComandaService;
import br.com.sportcourt.service.ItemComandaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ItemComandaController {

    @FXML
    private TableView<ItemComanda> tableItens;
    @FXML
    private TableColumn<ItemComanda, Integer> colProduto;
    @FXML
    private TableColumn<ItemComanda, Integer> colQtd;
    @FXML
    private TableColumn<ItemComanda, Double> colUnit;
    @FXML
    private TableColumn<ItemComanda, Double> colTotal;

    @FXML
    private TextField txtProdutoId;
    @FXML
    private TextField txtQuantidade;

    private final ItemComandaService service = new ItemComandaService();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    private Comanda comanda;

    public void setComanda(Comanda c) {
        this.comanda = c;
        carregarTabela();
    }

    @FXML
    public void initialize() {
        colProduto.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getProdutoId())
                        .asObject());
        colQtd.setCellValueFactory(
                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantidade())
                        .asObject());
        colUnit.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getValorUnit())
                        .asObject());
        colTotal.setCellValueFactory(
                c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getValorTotal())
                        .asObject());
    }

    private void carregarTabela() {
        if (comanda != null) {
            var itens = service.listar(comanda.getId());
            tableItens.setItems(FXCollections.observableArrayList(itens));
        }
    }

    @FXML
    public void onAdicionar() {
        try {
            int produtoId = Integer.parseInt(txtProdutoId.getText());
            int quantidade = Integer.parseInt(txtQuantidade.getText());

            Produto p = produtoDAO.findAll().stream().filter(prod -> prod.getId() == produtoId)
                    .findFirst().orElse(null);

            if (p == null) {
                new Alert(Alert.AlertType.ERROR, "Produto não encontrado!").show();
                return;
            }

            double unit = p.getPreco();
            double total = unit * quantidade;

            ItemComanda item = new ItemComanda();
            item.setComandaId(comanda.getId());
            item.setProdutoId(produtoId);
            item.setQuantidade(quantidade);
            item.setValorUnit(unit);
            item.setValorTotal(total);

            service.adicionar(item);

            comanda.setTotal(comanda.getTotal() + total);
            new ComandaService().atualizar(comanda);

            carregarTabela();

            txtProdutoId.clear();
            txtQuantidade.clear();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erro ao adicionar item").show();
        }
    }



    @FXML
    public void onRemover() {
        ItemComanda item = tableItens.getSelectionModel().getSelectedItem();
        if (item == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione um item").show();
            return;
        }

        service.remover(item.getId());
        carregarTabela();
    }

    @FXML
    public void onFechar() {
        Stage stage = (Stage) txtProdutoId.getScene().getWindow();
        stage.close();
    }
}
