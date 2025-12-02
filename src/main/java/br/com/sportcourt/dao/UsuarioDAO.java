package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario findByUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND ativo = 1";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password_hash"), rs.getString("email"), rs.getString("role"),
                        rs.getBoolean("ativo"), rs.getString("nome"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean inserir(Usuario u) {
        String sql =
                "INSERT INTO usuarios (username, password_hash, email, role, ativo, nome) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getSenha());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getRole());
            stmt.setBoolean(5, u.isAtivo());
            stmt.setString(6, u.getNome());

            boolean sucesso = stmt.executeUpdate() > 0;
            return sucesso;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Usuario> findAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY username";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password_hash"), rs.getString("email"), rs.getString("role"),
                        rs.getBoolean("ativo"), rs.getString("nome")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean atualizarRole(int id, String role) {
        String sql = "UPDATE usuarios SET role=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean resetarSenha(int id, String novaSenha) {
        String sql = "UPDATE usuarios SET password_hash=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaSenha);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
