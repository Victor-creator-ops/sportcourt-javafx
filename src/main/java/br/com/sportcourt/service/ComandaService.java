package br.com.sportcourt.service;

import br.com.sportcourt.dao.ComandaDAO;
import br.com.sportcourt.dao.ItemComandaDAO;
import br.com.sportcourt.model.Comanda;
import br.com.sportcourt.model.ItemComanda;

import java.util.List;

public class ComandaService {

    private final ComandaDAO dao = new ComandaDAO();
    private final ItemComandaDAO itemDAO = new ItemComandaDAO();

    public void salvar(Comanda c) {
        dao.create(c);
    }

    public List<Comanda> listar() {
        return dao.findAll();
    }

    public void fecharComanda(Comanda c) {
        List<ItemComanda> itens = itemDAO.findByComanda(c.getId());

        double total = itens.stream()
                .mapToDouble(ItemComanda::getValorTotal)
                .sum();

        c.setTotal(total);
        c.setStatus("FECHADA");

        dao.update(c);
    }

    public void remover(int id) {
        dao.delete(id);
    }
}
