package br.com.gwfrete.dao;

import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.Usuario;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id, nome, email, senha, perfil, status FROM usuario WHERE LOWER(email) = LOWER(?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }

        return null;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setPerfil(PerfilUsuario.valueOf(rs.getString("perfil")));
        usuario.setAtivo("ATIVO".equalsIgnoreCase(rs.getString("status")));
        return usuario;
    }
}
