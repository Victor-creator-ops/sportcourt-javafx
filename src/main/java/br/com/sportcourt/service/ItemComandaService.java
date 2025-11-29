package br.com.sportcourt.service;

import br.com.sportcourt.dao.ItemComandaDAO;
import br.com.sportcourt.model.ItemComanda;

import java.util.List;

public class ItemComandaService {

    private final ItemComandaDAO dao = new ItemComandaDAO();

    public void adicionar(ItemComanda item) {
        dao.create(item);
    }

    public List<ItemComanda> listar(int comandaId) {
        return dao.findByComanda(comandaId);
    }

    public void remover(int id) {
        dao.delete(id);
    }
}
