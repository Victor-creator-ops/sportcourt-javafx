package br.com.sportcourt.service;

import br.com.sportcourt.dao.UsuarioDAO;
import br.com.sportcourt.model.Usuario;

public class AuthService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticar(String nome, String senhaDigitada) {
        try {
            Usuario u = usuarioDAO.findByUsername(nome);

            if (u == null) {
                return null;
            }

            String senhaSalva = u.getSenhaHash();

            boolean senhaOk = senhaDigitada.equals(senhaSalva);

            return senhaOk ? u : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean cadastrar(String username, String senha, String email, String nome,
            String role) {

        try {
            if (usuarioDAO.findByUsername(username) != null) {
                return false;
            }

            Usuario novo = new Usuario(0, username, senha, email, role, true, nome);

            boolean sucesso = usuarioDAO.inserir(novo);

            return sucesso;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
