package br.com.sportcourt.dao;

import br.com.sportcourt.config.DatabaseConfig;
import br.com.sportcourt.model.Usuario;

import java.sql.*;

public class UsuarioDAO {

    public Usuario findByUsernameAndPassword(String username, String password) {
        String sql =
                "SELECT * FROM usuarios WHERE username = ? AND password_hash = ? AND ativo = TRUE";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password_hash"), rs.getString("papel"),
                        rs.getBoolean("ativo"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
