package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.ItemComanda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemComandaDAO {

    public void create(ItemComanda item) {
        String sql = "INSERT INTO itens_comanda (comanda_id, produto_id, quantidade, valor_unit, valor_total) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getComandaId());
            stmt.setInt(2, item.getProdutoId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getValorUnit());
            stmt.setDouble(5, item.getValorTotal());

            stmt.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<ItemComanda> findByComanda(int comandaId) {
        List<ItemComanda> lista = new ArrayList<>();
        String sql = "SELECT * FROM itens_comanda WHERE comanda_id=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, comandaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new ItemComanda(
                    rs.getInt("id"),
                    rs.getInt("comanda_id"),
                    rs.getInt("produto_id"),
                    rs.getInt("quantidade"),
                    rs.getDouble("valor_unit"),
                    rs.getDouble("valor_total")
                ));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return lista;
    }

    public void delete(int id) {
        String sql = "DELETE FROM itens_comanda WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }
}
