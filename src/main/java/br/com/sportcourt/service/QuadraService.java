package br.com.sportcourt.service;

import br.com.sportcourt.dao.QuadraDAO;
import br.com.sportcourt.model.Quadra;

import java.util.List;

public class QuadraService {

    private final QuadraDAO dao = new QuadraDAO();

    public void salvar(Quadra q) {
        if (q.getId() == 0) {
            dao.create(q);
        } else {
            dao.update(q);
        }
    }

    public List<Quadra> listar() {
        return dao.findAll();
    }

    public void remover(int id) {
        dao.delete(id);
    }
}
