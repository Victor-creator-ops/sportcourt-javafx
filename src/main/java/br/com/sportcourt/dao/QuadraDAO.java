package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.Quadra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuadraDAO {

    public void create(Quadra q) {
        String sql = "INSERT INTO quadras (nome, tipo, valor_hora, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, q.getNome());
            stmt.setString(2, q.getTipo());
            stmt.setDouble(3, q.getValorHora());
            stmt.setString(4, q.getStatus());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Quadra> findAll() {
        List<Quadra> lista = new ArrayList<>();
        String sql = "SELECT * FROM quadras";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Quadra(rs.getInt("id"), rs.getString("nome"), rs.getString("tipo"),
                        rs.getDouble("valor_hora"), rs.getString("status")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void update(Quadra q) {
        String sql = "UPDATE quadras SET nome=?, tipo=?, valor_hora=?, status=? WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, q.getNome());
            stmt.setString(2, q.getTipo());
            stmt.setDouble(3, q.getValorHora());
            stmt.setString(4, q.getStatus());
            stmt.setInt(5, q.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM quadras WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
