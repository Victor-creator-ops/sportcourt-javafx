package br.com.sportcourt.service;

import br.com.sportcourt.dao.FinanceiroDAO;
import br.com.sportcourt.model.FinanceiroMovimento;

import java.time.LocalDateTime;
import java.util.List;

public class FinanceiroService {

    private FinanceiroDAO dao = new FinanceiroDAO();

    public void registrarEntrada(String origem, double valor) {
        FinanceiroMovimento m = new FinanceiroMovimento();
        m.setTipo("ENTRADA");
        m.setOrigem(origem);
        m.setValor(valor);
        m.setDataHora(LocalDateTime.now());
        dao.inserir(m);
    }

    public void registrarSaida(String origem, double valor) {
        FinanceiroMovimento m = new FinanceiroMovimento();
        m.setTipo("SAIDA");
        m.setOrigem(origem);
        m.setValor(valor);
        m.setDataHora(LocalDateTime.now());
        dao.inserir(m);
    }

    public List<FinanceiroMovimento> listar() {
        return dao.listarTodos();
    }
}
