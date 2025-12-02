package br.com.sportcourt.service;

import br.com.sportcourt.dao.UsuarioDAO;
import br.com.sportcourt.model.Usuario;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO dao = new UsuarioDAO();

    public List<Usuario> listar() {
        return dao.findAll();
    }

    public boolean atualizarRole(Usuario u, String novaRole) {
        boolean ok = dao.atualizarRole(u.getId(), novaRole);
        if (ok) {
            u.setRole(novaRole);
        }
        return ok;
    }

    public boolean resetarSenha(Usuario u, String novaSenha) {
        boolean ok = dao.resetarSenha(u.getId(), novaSenha);
        if (ok) {
            u.setSenha(novaSenha);
        }
        return ok;
    }
}
