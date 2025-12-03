// File: src/main/java/br/com/sportcourt/dao/MaterialEsportivoDAO.java
package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.MaterialEsportivo;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MaterialEsportivoDAO {
    
    public void create(MaterialEsportivo m) {
        String sql = "INSERT INTO materiais_esportivos (nome, descricao, categoria, " +
                    "valor_locacao, valor_caucao, quantidade_total, quantidade_disponivel, " +
                    "data_validade, estado, ativo, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, m.getNome());
            stmt.setString(2, m.getDescricao());
            stmt.setString(3, m.getCategoria());
            stmt.setDouble(4, m.getValorLocacao());
            stmt.setDouble(5, m.getValorCaucao());
            stmt.setInt(6, m.getQuantidadeTotal());
            stmt.setInt(7, m.getQuantidadeDisponivel());
            
            if (m.getDataValidade() != null) {
                stmt.setDate(8, Date.valueOf(m.getDataValidade()));
            } else {
                stmt.setNull(8, Types.DATE);
            }
            
            stmt.setString(9, m.getEstado());
            stmt.setBoolean(10, m.isAtivo());
            stmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setId(rs.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar material esportivo: " + e.getMessage(), e);
        }
    }
    
    public List<MaterialEsportivo> findAll() {
        List<MaterialEsportivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM materiais_esportivos ORDER BY nome";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(mapResultSetToMaterial(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar materiais: " + e.getMessage(), e);
        }
        
        return lista;
    }
    
    public boolean update(MaterialEsportivo m) {
        String sql = "UPDATE materiais_esportivos SET nome=?, descricao=?, categoria=?, " +
                    "valor_locacao=?, valor_caucao=?, quantidade_total=?, quantidade_disponivel=?, " +
                    "data_validade=?, estado=?, ativo=?, updated_at=? WHERE id=?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, m.getNome());
            stmt.setString(2, m.getDescricao());
            stmt.setString(3, m.getCategoria());
            stmt.setDouble(4, m.getValorLocacao());
            stmt.setDouble(5, m.getValorCaucao());
            stmt.setInt(6, m.getQuantidadeTotal());
            stmt.setInt(7, m.getQuantidadeDisponivel());
            
            if (m.getDataValidade() != null) {
                stmt.setDate(8, Date.valueOf(m.getDataValidade()));
            } else {
                stmt.setNull(8, Types.DATE);
            }
            
            stmt.setString(9, m.getEstado());
            stmt.setBoolean(10, m.isAtivo());
            stmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(12, m.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar material: " + e.getMessage(), e);
        }
    }
    
    public boolean delete(int id) {
        String sql = "DELETE FROM materiais_esportivos WHERE id=?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir material: " + e.getMessage(), e);
        }
    }
    
    public MaterialEsportivo findById(int id) {
        String sql = "SELECT * FROM materiais_esportivos WHERE id=?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMaterial(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar material por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    public List<MaterialEsportivo> findDisponiveis() {
        List<MaterialEsportivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM materiais_esportivos " +
                    "WHERE quantidade_disponivel > 0 AND ativo = 1 " +
                    "AND (data_validade IS NULL OR data_validade >= CURDATE()) " +
                    "ORDER BY nome";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(mapResultSetToMaterial(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public List<MaterialEsportivo> findVencidos() {
        List<MaterialEsportivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM materiais_esportivos " +
                    "WHERE data_validade IS NOT NULL AND data_validade < CURDATE() " +
                    "ORDER BY data_validade";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(mapResultSetToMaterial(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public List<MaterialEsportivo> findByCategoria(String categoria) {
        List<MaterialEsportivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM materiais_esportivos WHERE categoria = ? ORDER BY nome";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapResultSetToMaterial(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }
    
    private MaterialEsportivo mapResultSetToMaterial(ResultSet rs) throws SQLException {
        MaterialEsportivo m = new MaterialEsportivo();
        m.setId(rs.getInt("id"));
        m.setNome(rs.getString("nome"));
        m.setDescricao(rs.getString("descricao"));
        m.setCategoria(rs.getString("categoria"));
        m.setValorLocacao(rs.getDouble("valor_locacao"));
        m.setValorCaucao(rs.getDouble("valor_caucao"));
        m.setQuantidadeTotal(rs.getInt("quantidade_total"));
        m.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));
        
        Date dataValidade = rs.getDate("data_validade");
        if (dataValidade != null) {
            m.setDataValidade(dataValidade.toLocalDate());
        }
        
        m.setEstado(rs.getString("estado"));
        m.setAtivo(rs.getBoolean("ativo"));
        m.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        m.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return m;
    }
}
