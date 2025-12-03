package br.com.sportcourt.model;

public class Comanda {

    private int id;
    private Integer reservaId;
    private String clienteNome;
    private double total;
    private String status;

    public Comanda() {}

    public Comanda(int id, Integer reservaId, String clienteNome, double total, String status) {
        this.id = id;
        this.reservaId = reservaId;
        this.clienteNome = clienteNome;
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

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
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
