package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.Reserva;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public boolean existeConflito(String quadraId, LocalDate data, LocalTime inicio,
            LocalTime fim, Integer ignorarId) {
        String sql = "SELECT COUNT(*) FROM reservas "
                + "WHERE quadra_id = ? "
                + "AND data = ? "
                + "AND status <> 'CANCELADO' "
                + "AND NOT (hora_fim <= ? OR hora_inicio >= ?) "
                + (ignorarId != null ? "AND id <> ?" : "");

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, quadraId);
            stmt.setDate(2, Date.valueOf(data));
            stmt.setTime(3, Time.valueOf(inicio));
            stmt.setTime(4, Time.valueOf(fim));
            if (ignorarId != null) {
                stmt.setInt(5, ignorarId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar disponibilidade: " + e.getMessage(), e);
        }

        return false;
    }

    public void create(Reserva r) {
        String sql =
                "INSERT INTO reservas (quadra_id, cliente_nome, data, hora_inicio, hora_fim, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, r.getQuadraId());
            stmt.setString(2, r.getClienteNome());
            stmt.setDate(3, Date.valueOf(r.getData()));
            stmt.setTime(4, Time.valueOf(r.getHoraInicio()));
            stmt.setTime(5, Time.valueOf(r.getHoraFim()));
            stmt.setString(6, r.getStatus());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    r.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar reserva: " + e.getMessage(), e);
        }
    }

    public List<Reserva> findAll() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas ORDER BY data DESC, hora_inicio DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Reserva(rs.getInt("id"), rs.getString("quadra_id"),
                        rs.getString("cliente_nome"), rs.getDate("data").toLocalDate(),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fim").toLocalTime(), rs.getString("status")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar reservas: " + e.getMessage(), e);
        }

        return lista;
    }

    public void update(Reserva r) {
        String sql =
                "UPDATE reservas SET quadra_id=?, cliente_nome=?, data=?, hora_inicio=?, hora_fim=?, status=? WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, r.getQuadraId());
            stmt.setString(2, r.getClienteNome());
            stmt.setDate(3, Date.valueOf(r.getData()));
            stmt.setTime(4, Time.valueOf(r.getHoraInicio()));
            stmt.setTime(5, Time.valueOf(r.getHoraFim()));
            stmt.setString(6, r.getStatus());
            stmt.setInt(7, r.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar reserva: " + e.getMessage(), e);
        }
    }

    public Reserva findById(int id) {
        String sql = "SELECT * FROM reservas WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Reserva(rs.getInt("id"), rs.getString("quadra_id"),
                        rs.getString("cliente_nome"), rs.getDate("data").toLocalDate(),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fim").toLocalTime(), rs.getString("status"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar reserva: " + e.getMessage(), e);
        }

        return null;
    }

    public void delete(int id) {
        String sql = "DELETE FROM reservas WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir reserva: " + e.getMessage(), e);
        }
    }
}
