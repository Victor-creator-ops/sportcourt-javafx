package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.Comanda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComandaDAO {

    public void create(Comanda c) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            boolean temCliente = colunaClienteExiste(conn);
            String sql;
            if (temCliente) {
                sql = "INSERT INTO comandas_bar (reserva_id, cliente_nome, total, status) VALUES (?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO comandas_bar (reserva_id, total, status) VALUES (?, ?, ?)";
            }
            PreparedStatement stmt =
                    conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            if (c.getReservaId() == null)
                stmt.setNull(1, Types.INTEGER);
            else
                stmt.setInt(1, c.getReservaId());

            int idx = 2;
            if (temCliente) {
                if (c.getClienteNome() == null || c.getClienteNome().isBlank())
                    stmt.setNull(idx++, Types.VARCHAR);
                else
                    stmt.setString(idx++, c.getClienteNome());
            }

            stmt.setDouble(idx++, c.getTotal());
            stmt.setString(idx, c.getStatus());

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

            boolean temCliente = colunaClienteExiste(conn);
            while (rs.next()) {
                lista.add(new Comanda(rs.getInt("id"),
                        rs.getObject("reserva_id") == null ? null : rs.getInt("reserva_id"),
                        temCliente ? rs.getString("cliente_nome") : null,
                        rs.getDouble("total"), rs.getString("status")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar comandas: " + e.getMessage(), e);
        }

        return lista;
    }

    public boolean update(Comanda c) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            boolean temCliente = colunaClienteExiste(conn);
            String sql;
            if (temCliente) {
                sql = "UPDATE comandas_bar SET reserva_id=?, cliente_nome=?, total=?, status=? WHERE id=?";
            } else {
                sql = "UPDATE comandas_bar SET reserva_id=?, total=?, status=? WHERE id=?";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);

            if (c.getReservaId() == null)
                stmt.setNull(1, Types.INTEGER);
            else
                stmt.setInt(1, c.getReservaId());

            int idx = 2;
            if (temCliente) {
                if (c.getClienteNome() == null || c.getClienteNome().isBlank())
                    stmt.setNull(idx++, Types.VARCHAR);
                else
                    stmt.setString(idx++, c.getClienteNome());
            }

            stmt.setDouble(idx++, c.getTotal());
            stmt.setString(idx++, c.getStatus());
            stmt.setInt(idx, c.getId());

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

    private boolean colunaClienteExiste(Connection conn) {
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, "comandas_bar",
                "cliente_nome")) {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            // ignora
        }
        return false;
    }
}
