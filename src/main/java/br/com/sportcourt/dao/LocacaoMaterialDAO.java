// File: src/main/java/br/com/sportcourt/dao/LocacaoMaterialDAO.java
package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.LocacaoMaterial;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LocacaoMaterialDAO {
    
    public void create(LocacaoMaterial locacao) {
        String sql = "INSERT INTO locacoes_materiais (material_id, reserva_id, cliente_nome, " +
                    "telefone_cliente, email_cliente, quantidade, data_abertura, " +
                    "data_fechamento_previsto, data_fechamento_real, valor_total, " +
                    "valor_caucao, multa_atraso, status, checkin_realizado, " +
                    "observacoes, atendente, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, locacao.getMaterialId());
            if (locacao.getReservaId() == 0) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, locacao.getReservaId());
            }
            stmt.setString(3, locacao.getClienteNome());
            stmt.setString(4, locacao.getTelefoneCliente());
            stmt.setString(5, locacao.getEmailCliente());
            stmt.setInt(6, locacao.getQuantidade());
            stmt.setTimestamp(7, Timestamp.valueOf(locacao.getDataAbertura()));
            stmt.setTimestamp(8, Timestamp.valueOf(locacao.getDataFechamentoPrevisto()));
            
            if (locacao.getDataFechamentoReal() != null) {
                stmt.setTimestamp(9, Timestamp.valueOf(locacao.getDataFechamentoReal()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }
            
            stmt.setDouble(10, locacao.getValorTotal());
            stmt.setDouble(11, locacao.getValorCaucao());
            stmt.setDouble(12, locacao.getMultaAtraso());
            stmt.setString(13, locacao.getStatus());
            stmt.setBoolean(14, locacao.isCheckinRealizado());
            stmt.setString(15, locacao.getObservacoes());
            stmt.setString(16, locacao.getAtendente());
            stmt.setTimestamp(17, Timestamp.valueOf(locacao.getCreatedAt()));
            stmt.setTimestamp(18, Timestamp.valueOf(locacao.getUpdatedAt()));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    locacao.setId(rs.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar locação: " + e.getMessage(), e);
        }
    }
    
    public List<LocacaoMaterial> findAll() {
        List<LocacaoMaterial> lista = new ArrayList<>();
        String sql = "SELECT * FROM locacoes_materiais ORDER BY data_abertura DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(mapResultSetToLocacao(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public boolean update(LocacaoMaterial locacao) {
        String sql = "UPDATE locacoes_materiais SET material_id=?, reserva_id=?, cliente_nome=?, " +
                    "telefone_cliente=?, email_cliente=?, quantidade=?, data_abertura=?, " +
                    "data_fechamento_previsto=?, data_fechamento_real=?, valor_total=?, " +
                    "valor_caucao=?, multa_atraso=?, status=?, checkin_realizado=?, " +
                    "observacoes=?, atendente=?, updated_at=? WHERE id=?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, locacao.getMaterialId());
            if (locacao.getReservaId() == 0) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, locacao.getReservaId());
            }
            stmt.setString(3, locacao.getClienteNome());
            stmt.setString(4, locacao.getTelefoneCliente());
            stmt.setString(5, locacao.getEmailCliente());
            stmt.setInt(6, locacao.getQuantidade());
            stmt.setTimestamp(7, Timestamp.valueOf(locacao.getDataAbertura()));
            stmt.setTimestamp(8, Timestamp.valueOf(locacao.getDataFechamentoPrevisto()));
            
            if (locacao.getDataFechamentoReal() != null) {
                stmt.setTimestamp(9, Timestamp.valueOf(locacao.getDataFechamentoReal()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }
            
            stmt.setDouble(10, locacao.getValorTotal());
            stmt.setDouble(11, locacao.getValorCaucao());
            stmt.setDouble(12, locacao.getMultaAtraso());
            stmt.setString(13, locacao.getStatus());
            stmt.setBoolean(14, locacao.isCheckinRealizado());
            stmt.setString(15, locacao.getObservacoes());
            stmt.setString(16, locacao.getAtendente());
            stmt.setTimestamp(17, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(18, locacao.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<LocacaoMaterial> findAbertas() {
        List<LocacaoMaterial> lista = new ArrayList<>();
        String sql = "SELECT * FROM locacoes_materiais WHERE status = 'ABERTA' ORDER BY data_fechamento_previsto";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(mapResultSetToLocacao(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public List<LocacaoMaterial> findAtrasadas() {
        List<LocacaoMaterial> lista = new ArrayList<>();
        String sql = "SELECT * FROM locacoes_materiais " +
                    "WHERE status = 'ABERTA' AND data_fechamento_previsto < NOW() " +
                    "ORDER BY data_fechamento_previsto";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(mapResultSetToLocacao(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public List<LocacaoMaterial> findByCliente(String clienteNome) {
        List<LocacaoMaterial> lista = new ArrayList<>();
        String sql = "SELECT * FROM locacoes_materiais WHERE cliente_nome LIKE ? ORDER BY data_abertura DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + clienteNome + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapResultSetToLocacao(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public LocacaoMaterial findById(int id) {
        String sql = "SELECT * FROM locacoes_materiais WHERE id=?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLocacao(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private LocacaoMaterial mapResultSetToLocacao(ResultSet rs) throws SQLException {
        LocacaoMaterial locacao = new LocacaoMaterial();
        locacao.setId(rs.getInt("id"));
        locacao.setMaterialId(rs.getInt("material_id"));
        locacao.setReservaId(rs.getInt("reserva_id"));
        locacao.setClienteNome(rs.getString("cliente_nome"));
        locacao.setTelefoneCliente(rs.getString("telefone_cliente"));
        locacao.setEmailCliente(rs.getString("email_cliente"));
        locacao.setQuantidade(rs.getInt("quantidade"));
        locacao.setDataAbertura(rs.getTimestamp("data_abertura").toLocalDateTime());
        locacao.setDataFechamentoPrevisto(rs.getTimestamp("data_fechamento_previsto").toLocalDateTime());
        
        Timestamp fechamentoReal = rs.getTimestamp("data_fechamento_real");
        if (fechamentoReal != null) {
            locacao.setDataFechamentoReal(fechamentoReal.toLocalDateTime());
        }
        
        locacao.setValorTotal(rs.getDouble("valor_total"));
        locacao.setValorCaucao(rs.getDouble("valor_caucao"));
        locacao.setMultaAtraso(rs.getDouble("multa_atraso"));
        locacao.setStatus(rs.getString("status"));
        locacao.setCheckinRealizado(rs.getBoolean("checkin_realizado"));
        locacao.setObservacoes(rs.getString("observacoes"));
        locacao.setAtendente(rs.getString("atendente"));
        locacao.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        locacao.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return locacao;
    }
}
