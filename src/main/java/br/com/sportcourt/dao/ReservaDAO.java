package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.Reserva;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public void create(Reserva r) {
        String sql =
                "INSERT INTO reservas (quadra_id, cliente_nome, data, hora_inicio, hora_fim, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, r.getQuadraId());
            stmt.setString(2, r.getClienteNome());
            stmt.setDate(3, Date.valueOf(r.getData()));
            stmt.setTime(4, Time.valueOf(r.getHoraInicio()));
            stmt.setTime(5, Time.valueOf(r.getHoraFim()));
            stmt.setString(6, r.getStatus());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reserva> findAll() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Reserva(rs.getInt("id"), rs.getInt("quadra_id"),
                        rs.getString("cliente_nome"), rs.getDate("data").toLocalDate(),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fim").toLocalTime(), rs.getString("status")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void update(Reserva r) {
        String sql =
                "UPDATE reservas SET quadra_id=?, cliente_nome=?, data=?, hora_inicio=?, hora_fim=?, status=? WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, r.getQuadraId());
            stmt.setString(2, r.getClienteNome());
            stmt.setDate(3, Date.valueOf(r.getData()));
            stmt.setTime(4, Time.valueOf(r.getHoraInicio()));
            stmt.setTime(5, Time.valueOf(r.getHoraFim()));
            stmt.setString(6, r.getStatus());
            stmt.setInt(7, r.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM reservas WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
