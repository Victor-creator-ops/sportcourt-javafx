// File: src/main/java/br/com/sportcourt/service/MaterialEsportivoService.java
package br.com.sportcourt.service;

import br.com.sportcourt.dao.MaterialEsportivoDAO;
import br.com.sportcourt.model.MaterialEsportivo;
import java.time.LocalDate;
import java.util.List;

public class MaterialEsportivoService {
    
    private final MaterialEsportivoDAO dao = new MaterialEsportivoDAO();
    
    public void salvar(MaterialEsportivo material) {
        if (material.getId() == 0) {
            dao.create(material);
        } else {
            dao.update(material);
        }
    }
    
    public List<MaterialEsportivo> listar() {
        return dao.findAll();
    }
    
    public List<MaterialEsportivo> listarDisponiveis() {
        return dao.findDisponiveis();
    }
    
    public List<MaterialEsportivo> listarVencidos() {
        return dao.findVencidos();
    }
    
    public List<MaterialEsportivo> listarPorCategoria(String categoria) {
        return dao.findByCategoria(categoria);
    }
    
    public MaterialEsportivo buscarPorId(int id) {
        return dao.findById(id);
    }
    
    public boolean remover(int id) {
        return dao.delete(id);
    }
    
    public boolean emprestarMaterial(int materialId, int quantidade) {
        MaterialEsportivo material = dao.findById(materialId);
        if (material != null && material.getQuantidadeDisponivel() >= quantidade) {
            material.setQuantidadeDisponivel(material.getQuantidadeDisponivel() - quantidade);
            return dao.update(material);
        }
        return false;
    }
    
    public boolean devolverMaterial(int materialId, int quantidade, String estadoDevolucao) {
        MaterialEsportivo material = dao.findById(materialId);
        if (material != null) {
            int novaQuantidade = material.getQuantidadeDisponivel() + quantidade;
            if (novaQuantidade <= material.getQuantidadeTotal()) {
                material.setQuantidadeDisponivel(novaQuantidade);
                
                // Atualizar estado se o material voltou danificado
                if (!"BOM".equals(estadoDevolucao)) {
                    material.setEstado(estadoDevolucao);
                }
                
                return dao.update(material);
            }
        }
        return false;
    }
    
    public void registrarReposicao(int materialId, int quantidadeAdicional, 
                                  double novoValorLocacao, LocalDate novaDataValidade) {
        MaterialEsportivo material = dao.findById(materialId);
        if (material != null) {
            material.setQuantidadeTotal(material.getQuantidadeTotal() + quantidadeAdicional);
            material.setQuantidadeDisponivel(material.getQuantidadeDisponivel() + quantidadeAdicional);
            
            if (novoValorLocacao > 0) {
                material.setValorLocacao(novoValorLocacao);
            }
            
            if (novaDataValidade != null) {
                material.setDataValidade(novaDataValidade);
            }
            
            dao.update(material);
        }
    }
    
    public double calcularValorTotalLocacao(int materialId, int quantidade, int horas) {
        MaterialEsportivo material = dao.findById(materialId);
        if (material != null) {
            return material.getValorLocacao() * quantidade * horas;
        }
        return 0.0;
    }
}
