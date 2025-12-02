package br.com.sportcourt.service;

import br.com.sportcourt.dao.ComandaDAO;
import br.com.sportcourt.model.Comanda;

import java.util.List;

public class ComandaService {

    private final ComandaDAO dao = new ComandaDAO();

    public void salvar(Comanda c) {
        dao.create(c);
    }

    public List<Comanda> listar() {
        return dao.findAll();
    }

    public void atualizar(Comanda c) {
        dao.update(c);
    }

    public void fecharComanda(Comanda c) {
        c.setStatus("FECHADA");
        dao.update(c);

        FinanceiroService fin = new FinanceiroService();
        fin.registrarEntrada("COMANDA #" + c.getId(), c.getTotal());
    }

    public void remover(int id) {
        dao.delete(id);
    }
}
