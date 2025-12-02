package br.com.sportcourt.service;

import br.com.sportcourt.model.Usuario;

public class Session {

    private static Usuario usuarioLogado;

    public static void setUsuarioLogado(Usuario u) {
        usuarioLogado = u;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void logout() {
        usuarioLogado = null;
    }
}
