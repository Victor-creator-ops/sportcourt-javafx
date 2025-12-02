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
                PreparedStatement stmt =
                        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (c.getReservaId() == null)
                stmt.setNull(1, Types.INTEGER);
            else
                stmt.setInt(1, c.getReservaId());

            stmt.setDouble(2, c.getTotal());
            stmt.setString(3, c.getStatus());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    c.setId(generatedKeys.getInt(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar comanda: " + e.getMessage(), e);
        }
    }

    public List<Comanda> findAll() {
        List<Comanda> lista = new ArrayList<>();
        String sql = "SELECT * FROM comandas_bar ORDER BY id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Comanda(rs.getInt("id"),
                        rs.getObject("reserva_id") == null ? null : rs.getInt("reserva_id"),
                        rs.getDouble("total"), rs.getString("status")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar comandas: " + e.getMessage(), e);
        }

        return lista;
    }

    public boolean update(Comanda c) {
        String sql = "UPDATE comandas_bar SET reserva_id=?, total=?, status=? WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (c.getReservaId() == null)
                stmt.setNull(1, Types.INTEGER);
            else
                stmt.setInt(1, c.getReservaId());

            stmt.setDouble(2, c.getTotal());
            stmt.setString(3, c.getStatus());
            stmt.setInt(4, c.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar comanda: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM comandas_bar WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir comanda: " + e.getMessage(), e);
        }
    }
}
