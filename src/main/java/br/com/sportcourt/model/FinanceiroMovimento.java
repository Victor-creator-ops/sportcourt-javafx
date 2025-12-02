package br.com.sportcourt.model;

import java.time.LocalDateTime;

public class FinanceiroMovimento {
    private int id;
    private String tipo;
    private String origem;
    private double valor;
    private LocalDateTime dataHora;

    public FinanceiroMovimento(int id, String tipo, String origem, double valor,
            LocalDateTime dataHora) {
        this.id = id;
        this.tipo = tipo;
        this.origem = origem;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    public FinanceiroMovimento() {}

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getOrigem() {
        return origem;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
