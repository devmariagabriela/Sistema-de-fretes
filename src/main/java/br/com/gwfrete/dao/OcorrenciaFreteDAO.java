package br.com.gwfrete.dao;

import br.com.gwfrete.model.Frete;
import br.com.gwfrete.model.OcorrenciaFrete;
import br.com.gwfrete.model.StatusFrete;
import br.com.gwfrete.model.TipoOcorrenciaFrete;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OcorrenciaFreteDAO {

    public void salvar(OcorrenciaFrete ocorrencia) throws SQLException {
        String sql = "INSERT INTO ocorrencia_frete (frete_id, tipo, data_hora, localizacao, descricao, "
                + "nome_recebedor, documento_recebedor) "
                + "VALUES (?, ?::tipo_ocorrencia_frete_enum, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosOcorrencia(stmt, ocorrencia);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ocorrencia.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<OcorrenciaFrete> listarPorFrete(Long freteId) throws SQLException {
        String sql = "SELECT o.id, o.frete_id, o.tipo, o.data_hora, o.localizacao, o.descricao, "
                + "o.nome_recebedor, o.documento_recebedor, o.data_criacao, f.codigo AS frete_codigo, "
                + "f.status AS frete_status "
                + "FROM ocorrencia_frete o "
                + "INNER JOIN frete f ON f.id = o.frete_id "
                + "WHERE o.frete_id = ? "
                + "ORDER BY o.data_hora DESC, o.id DESC";

        List<OcorrenciaFrete> ocorrencias = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, freteId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ocorrencias.add(mapearOcorrencia(rs));
                }
            }
        }

        return ocorrencias;
    }

    public OcorrenciaFrete buscarPorId(Long id) throws SQLException {
        String sql = "SELECT o.id, o.frete_id, o.tipo, o.data_hora, o.localizacao, o.descricao, "
                + "o.nome_recebedor, o.documento_recebedor, o.data_criacao, f.codigo AS frete_codigo, "
                + "f.status AS frete_status "
                + "FROM ocorrencia_frete o "
                + "INNER JOIN frete f ON f.id = o.frete_id "
                + "WHERE o.id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearOcorrencia(rs);
                }
            }
        }

        return null;
    }

    private void preencherParametrosOcorrencia(PreparedStatement stmt, OcorrenciaFrete ocorrencia)
            throws SQLException {

        stmt.setLong(1, ocorrencia.getFrete().getId());
        stmt.setString(2, ocorrencia.getTipo().name());
        stmt.setTimestamp(3, Timestamp.valueOf(ocorrencia.getDataHora()));
        stmt.setString(4, ocorrencia.getLocalizacao());
        stmt.setString(5, ocorrencia.getDescricao());
        stmt.setString(6, ocorrencia.getNomeRecebedor());
        stmt.setString(7, ocorrencia.getDocumentoRecebedor());
    }

    private OcorrenciaFrete mapearOcorrencia(ResultSet rs) throws SQLException {
        OcorrenciaFrete ocorrencia = new OcorrenciaFrete();
        ocorrencia.setId(rs.getLong("id"));
        ocorrencia.setTipo(TipoOcorrenciaFrete.valueOf(rs.getString("tipo")));
        ocorrencia.setLocalizacao(rs.getString("localizacao"));
        ocorrencia.setDescricao(rs.getString("descricao"));
        ocorrencia.setNomeRecebedor(rs.getString("nome_recebedor"));
        ocorrencia.setDocumentoRecebedor(rs.getString("documento_recebedor"));

        Timestamp dataHora = rs.getTimestamp("data_hora");
        if (dataHora != null) {
            ocorrencia.setDataHora(dataHora.toLocalDateTime());
        }

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            ocorrencia.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        Frete frete = new Frete();
        frete.setId(rs.getLong("frete_id"));
        frete.setCodigo(rs.getString("frete_codigo"));
        frete.setStatus(StatusFrete.valueOf(rs.getString("frete_status")));
        ocorrencia.setFrete(frete);

        return ocorrencia;
    }
}
