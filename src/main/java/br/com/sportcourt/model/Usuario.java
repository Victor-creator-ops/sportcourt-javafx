package br.com.sportcourt.model;

public class Usuario {

    private int id;
    private String username;
    private String senha;
    private String email;
    private String role;
    private boolean ativo;
    private String nome;

    public Usuario(int id, String username, String senha, String email, String role, boolean ativo,
            String nome) {
        this.id = id;
        this.username = username;
        this.senha = senha;
        this.email = email;
        this.role = role;
        this.ativo = ativo;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getSenha() {
        return senha;
    }

    public String getSenhaHash() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }
}
