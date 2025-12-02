package br.com.sportcourt.model;

public class ItemComanda {

    private int id;
    private int comandaId;
    private int produtoId;
    private int quantidade;
    private double valorUnit;
    private double valorTotal;

    public ItemComanda() {}

    public ItemComanda(int id, int comandaId, int produtoId, int quantidade, double valorUnit,
            double valorTotal) {
        this.id = id;
        this.comandaId = comandaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.valorUnit = valorUnit;
        this.valorTotal = valorTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComandaId() {
        return comandaId;
    }

    public void setComandaId(int comandaId) {
        this.comandaId = comandaId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorUnit() {
        return valorUnit;
    }

    public void setValorUnit(double valorUnit) {
        this.valorUnit = valorUnit;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
