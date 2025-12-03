// File: src/main/java/br/com/sportcourt/service/LocacaoMaterialService.java
package br.com.sportcourt.service;

import br.com.sportcourt.dao.LocacaoMaterialDAO;
import br.com.sportcourt.model.LocacaoMaterial;
import br.com.sportcourt.model.MaterialEsportivo;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LocacaoMaterialService {
    
    private final LocacaoMaterialDAO dao = new LocacaoMaterialDAO();
    private final MaterialEsportivoService materialService = new MaterialEsportivoService();
    private final FinanceiroService financeiroService = new FinanceiroService();
    
    public LocacaoMaterial criarLocacao(LocacaoMaterial locacao) {
        // Verificar disponibilidade do material
        MaterialEsportivo material = materialService.buscarPorId(locacao.getMaterialId());
        if (material == null || material.getQuantidadeDisponivel() < locacao.getQuantidade()) {
            throw new RuntimeException("Material não disponível em quantidade suficiente");
        }
        
        // Calcular valor total baseado no tempo de locação
        long horas = calcularHorasLocacao(locacao.getDataAbertura(), 
                                         locacao.getDataFechamentoPrevisto());
        double valorTotal = material.getValorLocacao() * locacao.getQuantidade() * horas;
        locacao.setValorTotal(valorTotal);
        locacao.setValorCaucao(material.getValorCaucao() * locacao.getQuantidade());
        
        // Atualizar quantidade disponível do material
        materialService.emprestarMaterial(locacao.getMaterialId(), locacao.getQuantidade());
        
        // Salvar locação
        dao.create(locacao);
        
        // Registrar entrada financeira (caução)
        if (locacao.getValorCaucao() > 0) {
            financeiroService.registrarEntrada(
                "CAUÇÃO LOCAÇÃO #" + locacao.getId(),
                locacao.getValorCaucao()
            );
        }
        
        return locacao;
    }
    
    public boolean finalizarLocacao(int locacaoId, String estadoMaterial, String observacoes) {
        LocacaoMaterial locacao = dao.findById(locacaoId);
        if (locacao == null || !"ABERTA".equals(locacao.getStatus())) {
            return false;
        }
        
        locacao.setDataFechamentoReal(LocalDateTime.now());
        locacao.setStatus("FECHADA");
        locacao.setObservacoes(observacoes);
        
        // Calcular multa por atraso se houver
        if (locacao.getDataFechamentoReal().isAfter(locacao.getDataFechamentoPrevisto())) {
            long horasAtraso = calcularHorasLocacao(locacao.getDataFechamentoPrevisto(), 
                                                   locacao.getDataFechamentoReal());
            MaterialEsportivo material = materialService.buscarPorId(locacao.getMaterialId());
            double multaPorHora = material.getValorLocacao() * 0.5; // 50% do valor por hora
            locacao.setMultaAtraso(multaPorHora * horasAtraso * locacao.getQuantidade());
        }
        
        // Devolver material ao estoque
        materialService.devolverMaterial(locacao.getMaterialId(), 
                                        locacao.getQuantidade(), 
                                        estadoMaterial);
        
        // Atualizar locação
        boolean sucesso = dao.update(locacao);
        
        // Registrar transações financeiras
        if (sucesso) {
            // Registrar valor total da locação
            financeiroService.registrarEntrada(
                "LOCAÇÃO #" + locacao.getId(),
                locacao.getValorTotal()
            );
            
            // Registrar multa se houver
            if (locacao.getMultaAtraso() > 0) {
                financeiroService.registrarEntrada(
                    "MULTA ATRASO LOCAÇÃO #" + locacao.getId(),
                    locacao.getMultaAtraso()
                );
            }
            
            // Devolver caução (registrar como saída)
            financeiroService.registrarSaida(
                "DEVOLUÇÃO CAUÇÃO LOCAÇÃO #" + locacao.getId(),
                locacao.getValorCaucao()
            );
        }
        
        return sucesso;
    }
    
    public boolean cancelarLocacao(int locacaoId, String motivo) {
        LocacaoMaterial locacao = dao.findById(locacaoId);
        if (locacao == null || !"ABERTA".equals(locacao.getStatus())) {
            return false;
        }
        
        locacao.setStatus("CANCELADA");
        locacao.setObservacoes("CANCELADO: " + motivo);
        
        // Devolver material ao estoque
        materialService.devolverMaterial(locacao.getMaterialId(), 
                                        locacao.getQuantidade(), 
                                        "BOM");
        
        // Devolver caução
        if (locacao.getValorCaucao() > 0) {
            financeiroService.registrarSaida(
                "DEVOLUÇÃO CAUÇÃO CANCELAMENTO #" + locacao.getId(),
                locacao.getValorCaucao()
            );
        }
        
        return dao.update(locacao);
    }
    
    public List<LocacaoMaterial> listarLocacoes() {
        return dao.findAll();
    }
    
    public List<LocacaoMaterial> listarLocacoesAbertas() {
        return dao.findAbertas();
    }
    
    public List<LocacaoMaterial> listarLocacoesAtrasadas() {
        return dao.findAtrasadas();
    }
    
    public List<LocacaoMaterial> buscarPorCliente(String clienteNome) {
        return dao.findByCliente(clienteNome);
    }
    
    public double calcularMultaAtraso(int locacaoId) {
        LocacaoMaterial locacao = dao.findById(locacaoId);
        if (locacao == null || !"ABERTA".equals(locacao.getStatus())) {
            return 0.0;
        }
        
        if (LocalDateTime.now().isAfter(locacao.getDataFechamentoPrevisto())) {
            long horasAtraso = calcularHorasLocacao(locacao.getDataFechamentoPrevisto(), 
                                                   LocalDateTime.now());
            MaterialEsportivo material = materialService.buscarPorId(locacao.getMaterialId());
            double multaPorHora = material.getValorLocacao() * 0.5;
            return multaPorHora * horasAtraso * locacao.getQuantidade();
        }
        
        return 0.0;
    }
    
    private long calcularHorasLocacao(LocalDateTime inicio, LocalDateTime fim) {
        return ChronoUnit.HOURS.between(inicio, fim);
    }
}
