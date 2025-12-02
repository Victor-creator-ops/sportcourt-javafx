package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.Quadra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuadraDAO {

    public void create(Quadra q) {
        String sql = "INSERT INTO quadras (id, tipo, valor_hora, disponivel) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, q.getId());
            stmt.setString(2, q.getTipo());
            stmt.setDouble(3, q.getValorHora());
            stmt.setBoolean(4, q.getDisponivel());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar quadra", e);
        }
    }

    public void update(Quadra q) {
        String sql = "UPDATE quadras SET tipo = ?, valor_hora = ?, disponivel = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, q.getTipo());
            stmt.setDouble(2, q.getValorHora());
            stmt.setBoolean(3, q.getDisponivel());
            stmt.setString(4, q.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar quadra", e);
        }
    }

    public List<Quadra> findAll() {
        List<Quadra> lista = new ArrayList<>();
        String sql = "SELECT id, tipo, valor_hora, disponivel FROM quadras ORDER BY id";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Quadra(rs.getString("id"), rs.getString("tipo"),
                        rs.getDouble("valor_hora"), rs.getBoolean("disponivel")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar quadras", e);
        }

        return lista;
    }

    public Quadra findById(String id) {
        String sql = "SELECT id, tipo, valor_hora, disponivel FROM quadras WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Quadra(rs.getString("id"), rs.getString("tipo"),
                        rs.getDouble("valor_hora"), rs.getBoolean("disponivel"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar quadra por ID", e);
        }

        return null;
    }

    public void delete(String id) {
        String sql = "DELETE FROM quadras WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir quadra", e);
        }
    }
}
