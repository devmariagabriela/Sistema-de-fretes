package br.com.gwfrete.dao;

import br.com.gwfrete.model.Notificacao;
import br.com.gwfrete.model.StatusNotificacao;
import br.com.gwfrete.model.TipoNotificacao;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoDAO {

    public void salvar(Notificacao notificacao) throws SQLException {
        String sql = "INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "VALUES (?::tipo_notificacao_enum, ?::status_notificacao_enum, ?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosNotificacao(stmt, notificacao);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    notificacao.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Notificacao> listarTodas() throws SQLException {
        String sql = sqlBase() + " WHERE status <> 'ARQUIVADA' ORDER BY data_criacao DESC";
        return listarPorSql(sql);
    }

    public List<Notificacao> listarNaoLidas() throws SQLException {
        String sql = sqlBase() + " WHERE status = 'NAO_LIDA' ORDER BY data_criacao DESC";
        return listarPorSql(sql);
    }

    public Notificacao buscarPorId(Long id) throws SQLException {
        String sql = sqlBase() + " WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearNotificacao(rs);
                }
            }
        }

        return null;
    }

    public void marcarComoLida(Long id) throws SQLException {
        String sql = "UPDATE notificacao "
                + "SET status = 'LIDA'::status_notificacao_enum, data_leitura = CURRENT_TIMESTAMP "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void arquivar(Long id) throws SQLException {
        String sql = "UPDATE notificacao SET status = 'ARQUIVADA'::status_notificacao_enum WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private List<Notificacao> listarPorSql(String sql) throws SQLException {
        List<Notificacao> notificacoes = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                notificacoes.add(mapearNotificacao(rs));
            }
        }

        return notificacoes;
    }

    private String sqlBase() {
        return "SELECT id, tipo, status, titulo, mensagem, referencia_id, referencia_tipo, "
                + "data_criacao, data_leitura FROM notificacao";
    }

    private void preencherParametrosNotificacao(PreparedStatement stmt, Notificacao notificacao) throws SQLException {
        stmt.setString(1, notificacao.getTipo().name());
        stmt.setString(2, notificacao.getStatus().name());
        stmt.setString(3, notificacao.getTitulo());
        stmt.setString(4, notificacao.getMensagem());
        preencherLong(stmt, 5, notificacao.getReferenciaId());
        stmt.setString(6, notificacao.getReferenciaTipo());
    }

    private void preencherLong(PreparedStatement stmt, int indice, Long valor) throws SQLException {
        if (valor == null) {
            stmt.setNull(indice, Types.BIGINT);
            return;
        }

        stmt.setLong(indice, valor);
    }

    private Notificacao mapearNotificacao(ResultSet rs) throws SQLException {
        Notificacao notificacao = new Notificacao();
        notificacao.setId(rs.getLong("id"));
        notificacao.setTipo(TipoNotificacao.valueOf(rs.getString("tipo")));
        notificacao.setStatus(StatusNotificacao.valueOf(rs.getString("status")));
        notificacao.setTitulo(rs.getString("titulo"));
        notificacao.setMensagem(rs.getString("mensagem"));

        Long referenciaId = rs.getLong("referencia_id");
        notificacao.setReferenciaId(rs.wasNull() ? null : referenciaId);
        notificacao.setReferenciaTipo(rs.getString("referencia_tipo"));

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            notificacao.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        Timestamp dataLeitura = rs.getTimestamp("data_leitura");
        if (dataLeitura != null) {
            notificacao.setDataLeitura(dataLeitura.toLocalDateTime());
        }

        return notificacao;
    }
}
