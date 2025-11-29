package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.Comanda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComandaDAO {

    public void create(Comanda c) {
        String sql = "INSERT INTO comandas_bar (reserva_id, total, status) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (c.getReservaId() == null) stmt.setNull(1, Types.INTEGER);
            else stmt.setInt(1, c.getReservaId());

            stmt.setDouble(2, c.getTotal());
            stmt.setString(3, c.getStatus());

            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<Comanda> findAll() {
        List<Comanda> lista = new ArrayList<>();
        String sql = "SELECT * FROM comandas_bar";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Comanda(
                    rs.getInt("id"),
                    rs.getObject("reserva_id") == null ? null : rs.getInt("reserva_id"),
                    rs.getDouble("total"),
                    rs.getString("status")
                ));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return lista;
    }

    public void update(Comanda c) {
        String sql = "UPDATE comandas_bar SET reserva_id=?, total=?, status=? WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (c.getReservaId() == null) stmt.setNull(1, Types.INTEGER);
            else stmt.setInt(1, c.getReservaId());

            stmt.setDouble(2, c.getTotal());
            stmt.setString(3, c.getStatus());
            stmt.setInt(4, c.getId());

            stmt.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM comandas_bar WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }
}
