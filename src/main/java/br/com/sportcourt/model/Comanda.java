package br.com.sportcourt.model;

public class Comanda {

    private int id;
    private Integer reservaId;
    private double total;
    private String status;

    public Comanda() {}

    public Comanda(int id, Integer reservaId, double total, String status) {
        this.id = id;
        this.reservaId = reservaId;
        this.total = total;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getReservaId() {
        return reservaId;
    }

    public void setReservaId(Integer reservaId) {
        this.reservaId = reservaId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
