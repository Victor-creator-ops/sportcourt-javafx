package br.com.sportcourt.model;

public class Usuario {

    private int id;
    private String username;
    private String passwordHash;
    private String papel; // ADMIN, OPERADOR, GERENTE
    private boolean ativo;

    public Usuario() {}

    public Usuario(int id, String username, String passwordHash, String papel, boolean ativo) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.papel = papel;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
