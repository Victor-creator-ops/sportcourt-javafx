package br.com.sportcourt.service;

import br.com.sportcourt.dao.UsuarioDAO;
import br.com.sportcourt.model.Usuario;

public class AuthService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuarioLogado;

    public boolean login(String username, String password) {
        Usuario u = usuarioDAO.findByUsernameAndPassword(username, password);
        if (u != null) {
            usuarioLogado = u;
            return true;
        }
        return false;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
