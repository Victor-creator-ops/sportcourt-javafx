package br.com.sportcourt.service;

import br.com.sportcourt.dao.ProdutoDAO;
import br.com.sportcourt.model.Produto;

import java.util.List;

public class ProdutoService {

    private final ProdutoDAO dao = new ProdutoDAO();

    public void salvar(Produto p) {
        if (p.getId() == 0)
            dao.create(p);
        else
            dao.update(p);
    }

    public List<Produto> listar() {
        return dao.findAll();
    }

    public void remover(int id) {
        dao.delete(id);
    }
}
