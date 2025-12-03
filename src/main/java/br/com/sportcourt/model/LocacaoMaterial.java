// File: src/main/java/br/com/sportcourt/model/LocacaoMaterial.java
package br.com.sportcourt.model;

import java.time.LocalDateTime;

public class LocacaoMaterial {
    private int id;
    private int materialId;
    private int reservaId; // Opcional - pode ser locação sem reserva
    private String clienteNome;
    private String telefoneCliente;
    private String emailCliente;
    private int quantidade;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamentoPrevisto;
    private LocalDateTime dataFechamentoReal;
    private double valorTotal;
    private double valorCaucao;
    private double multaAtraso;
    private String status; // ABERTA, FECHADA, CANCELADA, ATRASADA
    private boolean checkinRealizado;
    private String observacoes;
    private String atendente; // Usuário que realizou a locação
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Construtores
    public LocacaoMaterial() {
        this.dataAbertura = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "ABERTA";
        this.checkinRealizado = false;
    }
    
    // Getters e Setters (implementar todos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    
    public int getReservaId() { return reservaId; }
    public void setReservaId(int reservaId) { this.reservaId = reservaId; }
    
    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
    
    public String getTelefoneCliente() { return telefoneCliente; }
    public void setTelefoneCliente(String telefoneCliente) { this.telefoneCliente = telefoneCliente; }
    
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }
    
    public LocalDateTime getDataFechamentoPrevisto() { return dataFechamentoPrevisto; }
    public void setDataFechamentoPrevisto(LocalDateTime dataFechamentoPrevisto) { 
        this.dataFechamentoPrevisto = dataFechamentoPrevisto; 
    }
    
    public LocalDateTime getDataFechamentoReal() { return dataFechamentoReal; }
    public void setDataFechamentoReal(LocalDateTime dataFechamentoReal) { 
        this.dataFechamentoReal = dataFechamentoReal; 
    }
    
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
    
    public double getValorCaucao() { return valorCaucao; }
    public void setValorCaucao(double valorCaucao) { this.valorCaucao = valorCaucao; }
    
    public double getMultaAtraso() { return multaAtraso; }
    public void setMultaAtraso(double multaAtraso) { this.multaAtraso = multaAtraso; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public boolean isCheckinRealizado() { return checkinRealizado; }
    public void setCheckinRealizado(boolean checkinRealizado) { 
        this.checkinRealizado = checkinRealizado; 
    }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public String getAtendente() { return atendente; }
    public void setAtendente(String atendente) { this.atendente = atendente; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos auxiliares
    public boolean isAtrasada() {
        if (dataFechamentoPrevisto == null || "FECHADA".equals(status)) {
            return false;
        }
        return LocalDateTime.now().isAfter(dataFechamentoPrevisto);
    }
    
    public double getValorPendente() {
        return valorTotal + multaAtraso - valorCaucao;
    }
}
