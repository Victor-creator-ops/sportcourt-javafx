package br.com.sportcourt.model;

public class Quadra {

    private String id;
    private String tipo;
    private double valorHora;
    private boolean disponivel;

    public Quadra(String id, String tipo, double valorHora, boolean disponivel) {
        this.id = id;
        this.tipo = tipo;
        this.valorHora = valorHora;
        this.disponivel = disponivel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
