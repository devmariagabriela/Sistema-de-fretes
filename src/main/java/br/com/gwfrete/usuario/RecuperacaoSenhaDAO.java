package br.com.gwfrete.usuario;

import br.com.gwfrete.usuario.PerfilUsuario;
import br.com.gwfrete.usuario.RecuperacaoSenha;
import br.com.gwfrete.usuario.StatusUsuario;
import br.com.gwfrete.usuario.Usuario;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RecuperacaoSenhaDAO {

    public void salvar(RecuperacaoSenha recuperacao) throws SQLException {
        String sql = "INSERT INTO recuperacao_senha (usuario_id, token, data_expiracao, usado) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            stmt.setLong(1, recuperacao.getUsuario().getId());
            stmt.setString(2, recuperacao.getToken());
            stmt.setTimestamp(3, Timestamp.valueOf(recuperacao.getDataExpiracao()));
            stmt.setBoolean(4, Boolean.TRUE.equals(recuperacao.getUsado()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    recuperacao.setId(rs.getLong(1));
                }
            }
        }
    }

    public RecuperacaoSenha buscarPorToken(String token) throws SQLException {
        String sql = "SELECT rs.id, rs.token, rs.data_expiracao, rs.usado, rs.data_criacao, rs.data_uso, "
                + "u.id AS usuario_id, u.nome, u.email, u.senha, u.perfil, u.status, u.data_criacao AS usuario_data_criacao "
                + "FROM recuperacao_senha rs "
                + "INNER JOIN usuario u ON u.id = rs.usuario_id "
                + "WHERE rs.token = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearRecuperacao(rs);
                }
            }
        }

        return null;
    }

    public void invalidarToken(Long id) throws SQLException {
        String sql = "UPDATE recuperacao_senha "
                + "SET usado = TRUE, data_uso = COALESCE(data_uso, CURRENT_TIMESTAMP) "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void invalidarTokensAbertosDoUsuario(Long usuarioId) throws SQLException {
        String sql = "UPDATE recuperacao_senha "
                + "SET usado = TRUE, data_uso = COALESCE(data_uso, CURRENT_TIMESTAMP) "
                + "WHERE usuario_id = ? AND usado = FALSE";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, usuarioId);
            stmt.executeUpdate();
        }
    }

    private RecuperacaoSenha mapearRecuperacao(ResultSet rs) throws SQLException {
        RecuperacaoSenha recuperacao = new RecuperacaoSenha();
        recuperacao.setId(rs.getLong("id"));
        recuperacao.setToken(rs.getString("token"));
        recuperacao.setUsado(rs.getBoolean("usado"));

        Timestamp dataExpiracao = rs.getTimestamp("data_expiracao");
        if (dataExpiracao != null) {
            recuperacao.setDataExpiracao(dataExpiracao.toLocalDateTime());
        }

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            recuperacao.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        Timestamp dataUso = rs.getTimestamp("data_uso");
        if (dataUso != null) {
            recuperacao.setDataUso(dataUso.toLocalDateTime());
        }

        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("usuario_id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setPerfil(PerfilUsuario.valueOf(rs.getString("perfil")));
        usuario.setStatus(StatusUsuario.valueOf(rs.getString("status")));

        Timestamp usuarioDataCriacao = rs.getTimestamp("usuario_data_criacao");
        if (usuarioDataCriacao != null) {
            usuario.setDataCriacao(usuarioDataCriacao.toLocalDateTime());
        }

        recuperacao.setUsuario(usuario);
        return recuperacao;
    }
}
