package br.com.sportcourt.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {

    private int id;
    private String quadraId;
    private String clienteNome;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String status;

    public Reserva() {}

    public Reserva(int id, String quadraId, String clienteNome, LocalDate data,
            LocalTime horaInicio, LocalTime horaFim, String status) {
        this.id = id;
        this.quadraId = quadraId;
        this.clienteNome = clienteNome;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuadraId() {
        return quadraId;
    }

    public void setQuadraId(String quadraId) {
        this.quadraId = quadraId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
