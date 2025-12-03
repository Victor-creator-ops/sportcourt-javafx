// File: src/main/java/br/com/sportcourt/model/MaterialEsportivo.java
package br.com.sportcourt.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaterialEsportivo {
    private int id;
    private String nome;
    private String descricao;
    private String categoria; // BOLA, REDE, UNIFORME, EQUIPAMENTO
    private double valorLocacao;
    private double valorCaucao;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private LocalDate dataValidade; // Para materiais perecíveis
    private String estado; // NOVO, BOM, REGULAR, RUIM
    private boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Construtores
    public MaterialEsportivo() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.ativo = true;
    }
    
    public MaterialEsportivo(String nome, String descricao, String categoria, 
                            double valorLocacao, double valorCaucao,
                            int quantidadeTotal, LocalDate dataValidade, String estado) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valorLocacao = valorLocacao;
        this.valorCaucao = valorCaucao;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal;
        this.dataValidade = dataValidade;
        this.estado = estado;
    }
    
    // Getters e Setters (implementar todos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public double getValorLocacao() { return valorLocacao; }
    public void setValorLocacao(double valorLocacao) { this.valorLocacao = valorLocacao; }
    
    public double getValorCaucao() { return valorCaucao; }
    public void setValorCaucao(double valorCaucao) { this.valorCaucao = valorCaucao; }
    
    public int getQuantidadeTotal() { return quantidadeTotal; }
    public void setQuantidadeTotal(int quantidadeTotal) { 
        this.quantidadeTotal = quantidadeTotal; 
    }
    
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public void setQuantidadeDisponivel(int quantidadeDisponivel) { 
        this.quantidadeDisponivel = quantidadeDisponivel; 
    }
    
    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos auxiliares
    public int getQuantidadeEmprestada() {
        return quantidadeTotal - quantidadeDisponivel;
    }
    
    public boolean isDisponivel() {
        return ativo && quantidadeDisponivel > 0;
    }
    
    public boolean isVencido() {
        return dataValidade != null && dataValidade.isBefore(LocalDate.now());
    }
}
