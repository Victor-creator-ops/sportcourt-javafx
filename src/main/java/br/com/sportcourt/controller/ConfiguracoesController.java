package br.com.sportcourt.controller;

import br.com.sportcourt.model.Usuario;
import br.com.sportcourt.service.Session;
import br.com.sportcourt.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ConfiguracoesController extends BaseController {

    @FXML
    private TableView<Usuario> tableUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> colId;
    @FXML
    private TableColumn<Usuario, String> colUsername;
    @FXML
    private TableColumn<Usuario, String> colNome;
    @FXML
    private TableColumn<Usuario, String> colEmail;
    @FXML
    private TableColumn<Usuario, String> colRole;
    @FXML
    private TableColumn<Usuario, Boolean> colAtivo;
    @FXML
    private ComboBox<String> comboRole;
    @FXML
    private Button btnAlterarRole;
    @FXML
    private Button btnResetSenha;

    private final UsuarioService service = new UsuarioService();

    @FXML
    public void initialize() {
        configurarTabela();
        comboRole.getItems().addAll("ADMIN", "OPERADOR", "ATENDENTE");
        comboRole.getSelectionModel().selectFirst();
        carregarUsuarios();
        aplicarRestricaoAdmin();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));
    }

    private void carregarUsuarios() {
        tableUsuarios
                .setItems(FXCollections.observableArrayList(service.listar()));
    }

    private boolean isAdmin() {
        var u = Session.getUsuarioLogado();
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    private void aplicarRestricaoAdmin() {
        boolean admin = isAdmin();
        btnAlterarRole.setDisable(!admin);
        btnResetSenha.setDisable(!admin);
        comboRole.setDisable(!admin);

        if (!admin) {
            showWarning("Apenas administradores podem alterar usuários.");
        }
    }

    @FXML
    private void onSelecionarUsuario(javafx.scene.input.MouseEvent event) {
        Usuario u = tableUsuarios.getSelectionModel().getSelectedItem();
        if (u != null) {
            comboRole.setValue(u.getRole());
        }
    }

    @FXML
    private void onAlterarRole() {
        if (!isAdmin()) {
            showWarning("Você não tem permissão para alterar o tipo de usuário.");
            return;
        }

        Usuario u = tableUsuarios.getSelectionModel().getSelectedItem();
        if (u == null) {
            showWarning("Selecione um usuário.");
            return;
        }

        String novaRole = comboRole.getValue();
        if (novaRole == null || novaRole.isBlank()) {
            showWarning("Selecione uma role.");
            return;
        }

        boolean ok = service.atualizarRole(u, novaRole);
        if (ok) {
            tableUsuarios.refresh();
            showInfo("Role atualizada para " + novaRole + ".");
        } else {
            showError("Não foi possível atualizar a role.");
        }
    }

    @FXML
    private void onResetSenha() {
        if (!isAdmin()) {
            showWarning("Você não tem permissão para resetar senhas.");
            return;
        }

        Usuario u = tableUsuarios.getSelectionModel().getSelectedItem();
        if (u == null) {
            showWarning("Selecione um usuário.");
            return;
        }

        String novaSenha = "1234";

        if (confirmAction("Resetar senha",
                "Deseja resetar a senha de " + u.getUsername() + " para '" + novaSenha + "'?")) {
            boolean ok = service.resetarSenha(u, novaSenha);
            if (ok) {
                showInfo("Senha redefinida para '" + novaSenha + "'.");
            } else {
                showError("Não foi possível redefinir a senha.");
            }
        }
    }

    @FXML
    private void onAtualizar() {
        carregarUsuarios();
    }
}
