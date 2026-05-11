package br.com.gwfrete.usuario;

import br.com.gwfrete.usuario.PerfilUsuario;
import br.com.gwfrete.usuario.StatusUsuario;
import br.com.gwfrete.usuario.Usuario;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void salvar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, perfil, status) "
                + "VALUES (?, ?, ?, ?::perfil_usuario_enum, ?::status_usuario_enum)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosUsuario(stmt, usuario);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, email, senha, perfil, status, data_criacao "
                + "FROM usuario ORDER BY nome";

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }

        return usuarios;
    }

    public List<Usuario> listarComFiltros(String nome, String email, PerfilUsuario perfil, StatusUsuario status)
            throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT id, nome, email, senha, perfil, status, data_criacao "
                + "FROM usuario WHERE 1 = 1");
        List<Object> parametros = new ArrayList<>();

        if (textoPreenchido(nome)) {
            sql.append(" AND LOWER(nome) LIKE LOWER(?)");
            parametros.add("%" + nome.trim() + "%");
        }

        if (textoPreenchido(email)) {
            sql.append(" AND LOWER(email) LIKE LOWER(?)");
            parametros.add("%" + email.trim() + "%");
        }

        if (perfil != null) {
            sql.append(" AND perfil = ?::perfil_usuario_enum");
            parametros.add(perfil.name());
        }

        if (status != null) {
            sql.append(" AND status = ?::status_usuario_enum");
            parametros.add(status.name());
        }

        sql.append(" ORDER BY nome");
        return executarConsultaUsuarios(sql.toString(), parametros);
    }

    public Usuario buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nome, email, senha, perfil, status, data_criacao "
                + "FROM usuario WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }

        return null;
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id, nome, email, senha, perfil, status, data_criacao "
                + "FROM usuario WHERE LOWER(email) = LOWER(?)";

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

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario "
                + "SET nome = ?, email = ?, senha = ?, perfil = ?::perfil_usuario_enum, status = ?::status_usuario_enum "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametrosUsuario(stmt, usuario);
            stmt.setLong(6, usuario.getId());
            stmt.executeUpdate();
        }
    }

    public void inativar(Long id) throws SQLException {
        String sql = "UPDATE usuario SET status = 'INATIVO'::status_usuario_enum WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void ativar(Long id) throws SQLException {
        String sql = "UPDATE usuario SET status = 'ATIVO'::status_usuario_enum WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void atualizarSenha(Long id, String senhaCriptografada) throws SQLException {
        String sql = "UPDATE usuario SET senha = ? WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, senhaCriptografada);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

    private void preencherParametrosUsuario(PreparedStatement stmt, Usuario usuario) throws SQLException {
        stmt.setString(1, usuario.getNome());
        stmt.setString(2, usuario.getEmail());
        stmt.setString(3, usuario.getSenha());
        stmt.setString(4, usuario.getPerfil().name());
        stmt.setString(5, usuario.getStatus().name());
    }

    private List<Usuario> executarConsultaUsuarios(String sql, List<Object> parametros) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }
        }

        return usuarios;
    }

    private boolean textoPreenchido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setPerfil(PerfilUsuario.valueOf(rs.getString("perfil")));
        usuario.setStatus(StatusUsuario.valueOf(rs.getString("status")));

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            usuario.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        return usuario;
    }
}
