package br.com.sportcourt.dao;

import br.com.sportcourt.model.FinanceiroMovimento;
import br.com.sportcourt.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinanceiroDAO {

    public boolean inserir(FinanceiroMovimento m) {
        String sql = "INSERT INTO financeiro (tipo, origem, valor, data_hora) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, m.getTipo());
            stmt.setString(2, m.getOrigem());
            stmt.setDouble(3, m.getValor());
            stmt.setTimestamp(4, Timestamp.valueOf(m.getDataHora()));

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<FinanceiroMovimento> listarTodos() {
        String sql = "SELECT * FROM financeiro ORDER BY data_hora DESC";
        List<FinanceiroMovimento> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FinanceiroMovimento m = new FinanceiroMovimento(rs.getInt("id"),
                        rs.getString("tipo"), rs.getString("origem"), rs.getDouble("valor"),
                        rs.getTimestamp("data_hora").toLocalDateTime());
                lista.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
