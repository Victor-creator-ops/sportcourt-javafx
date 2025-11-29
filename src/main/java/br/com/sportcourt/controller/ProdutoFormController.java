package br.com.sportcourt.controller;

import br.com.sportcourt.model.Produto;
import br.com.sportcourt.service.ProdutoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ProdutoFormController {

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtPreco;
    @FXML
    private TextField txtCategoria;
    @FXML
    private ComboBox<String> comboAtivo;

    private Produto produtoEditando;
    private final ProdutoService service = new ProdutoService();

    @FXML
    public void initialize() {
        comboAtivo.getItems().addAll("Sim", "Não");
    }

    public void setProduto(Produto p) {
        produtoEditando = p;

        if (p != null) {
            txtNome.setText(p.getNome());
            txtPreco.setText(String.valueOf(p.getPreco()));
            txtCategoria.setText(p.getCategoria());
            comboAtivo.setValue(p.isAtivo() ? "Sim" : "Não");
        }
    }

    @FXML
    public void onSalvar() {
        try {
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            String categoria = txtCategoria.getText();
            boolean ativo = comboAtivo.getValue().equals("Sim");

            if (produtoEditando == null)
                produtoEditando = new Produto();

            produtoEditando.setNome(nome);
            produtoEditando.setPreco(preco);
            produtoEditando.setCategoria(categoria);
            produtoEditando.setAtivo(ativo);

            service.salvar(produtoEditando);

            new Alert(Alert.AlertType.INFORMATION, "Produto salvo!").show();
            fechar();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erro ao salvar. Preço inválido.").show();
        }
    }

    @FXML
    public void onCancelar() {
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }
}
