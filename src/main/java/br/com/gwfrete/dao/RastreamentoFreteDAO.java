package br.com.gwfrete.dao;

import br.com.gwfrete.model.Frete;
import br.com.gwfrete.model.RastreamentoFrete;
import br.com.gwfrete.model.StatusFrete;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RastreamentoFreteDAO {

    public void salvar(RastreamentoFrete rastreamento) throws SQLException {
        String sql = "INSERT INTO rastreamento_frete "
                + "(frete_id, latitude, longitude, localizacao, observacao, data_hora) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosRastreamento(stmt, rastreamento);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    rastreamento.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<RastreamentoFrete> listarPorFrete(Long freteId) throws SQLException {
        String sql = sqlBase() + " WHERE rf.frete_id = ? ORDER BY rf.data_hora ASC, rf.id ASC";
        List<RastreamentoFrete> rastreamentos = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, freteId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rastreamentos.add(mapearRastreamento(rs));
                }
            }
        }

        return rastreamentos;
    }

    public RastreamentoFrete buscarUltimoPorFrete(Long freteId) throws SQLException {
        String sql = sqlBase() + " WHERE rf.frete_id = ? ORDER BY rf.data_hora DESC, rf.id DESC LIMIT 1";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, freteId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearRastreamento(rs);
                }
            }
        }

        return null;
    }

    private String sqlBase() {
        return "SELECT rf.id, rf.frete_id, rf.latitude, rf.longitude, rf.localizacao, "
                + "rf.observacao, rf.data_hora, rf.data_criacao, "
                + "f.codigo AS frete_codigo, f.status AS frete_status "
                + "FROM rastreamento_frete rf "
                + "INNER JOIN frete f ON f.id = rf.frete_id";
    }

    private void preencherParametrosRastreamento(PreparedStatement stmt, RastreamentoFrete rastreamento)
            throws SQLException {

        stmt.setLong(1, rastreamento.getFrete().getId());
        stmt.setBigDecimal(2, rastreamento.getLatitude());
        stmt.setBigDecimal(3, rastreamento.getLongitude());
        stmt.setString(4, rastreamento.getLocalizacao());
        stmt.setString(5, rastreamento.getObservacao());
        stmt.setTimestamp(6, Timestamp.valueOf(rastreamento.getDataHora()));
    }

    private RastreamentoFrete mapearRastreamento(ResultSet rs) throws SQLException {
        RastreamentoFrete rastreamento = new RastreamentoFrete();
        rastreamento.setId(rs.getLong("id"));
        rastreamento.setLatitude(rs.getBigDecimal("latitude"));
        rastreamento.setLongitude(rs.getBigDecimal("longitude"));
        rastreamento.setLocalizacao(rs.getString("localizacao"));
        rastreamento.setObservacao(rs.getString("observacao"));

        Timestamp dataHora = rs.getTimestamp("data_hora");
        if (dataHora != null) {
            rastreamento.setDataHora(dataHora.toLocalDateTime());
        }

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            rastreamento.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        Frete frete = new Frete();
        frete.setId(rs.getLong("frete_id"));
        frete.setCodigo(rs.getString("frete_codigo"));
        frete.setStatus(StatusFrete.valueOf(rs.getString("frete_status")));
        rastreamento.setFrete(frete);

        return rastreamento;
    }
}
