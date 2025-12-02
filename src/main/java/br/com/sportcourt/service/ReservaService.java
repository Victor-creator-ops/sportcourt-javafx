package br.com.sportcourt.service;

import br.com.sportcourt.dao.ReservaDAO;
import br.com.sportcourt.model.Reserva;

import java.util.List;

public class ReservaService {

    private final ReservaDAO dao = new ReservaDAO();

    public void salvar(Reserva r) {
        if (r.getId() == 0) {
            dao.create(r);
        } else {
            dao.update(r);
        }
    }

    public List<Reserva> listar() {
        return dao.findAll();
    }

    public void confirmarPagamento(Reserva r, double valor) {
        r.setStatus("PAGA");
        dao.update(r);

        FinanceiroService fin = new FinanceiroService();
        fin.registrarEntrada("RESERVA #" + r.getId(), valor);
    }


    public void remover(int id) {
        dao.delete(id);
    }
}
