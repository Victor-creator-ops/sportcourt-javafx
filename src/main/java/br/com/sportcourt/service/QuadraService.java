package br.com.sportcourt.service;

import br.com.sportcourt.dao.QuadraDAO;
import br.com.sportcourt.model.Quadra;

import java.util.List;

public class QuadraService {

    private QuadraDAO dao = new QuadraDAO();

    public List<Quadra> listar() {
        return dao.findAll();
    }

    public Quadra buscarPorId(String id) {
        return dao.findById(id);
    }

    public void salvar(Quadra q) {
        Quadra existente = dao.findById(q.getId());
        if (existente != null) {
            dao.update(q);
        } else {
            dao.create(q);
        }
    }

    public void excluir(String id) {
        dao.delete(id);
    }
}
