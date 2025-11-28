package br.com.sportcourt.model;

public class Quadra {

    private int id;
    private String nome;
    private String tipo;
    private double valorHora;
    private String status;

    public Quadra() {}

    public Quadra(int id, String nome, String tipo, double valorHora, String status) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.valorHora = valorHora;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValorHora() {
        return valorHora;
    }

    public void setValorHora(double valorHora) {
        this.valorHora = valorHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
