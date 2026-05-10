package br.com.gwfrete.dao;

import br.com.gwfrete.model.ManutencaoVeiculo;
import br.com.gwfrete.model.StatusManutencao;
import br.com.gwfrete.model.StatusVeiculo;
import br.com.gwfrete.model.TipoManutencao;
import br.com.gwfrete.model.TipoVeiculo;
import br.com.gwfrete.model.Veiculo;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoVeiculoDAO {

    public void salvar(ManutencaoVeiculo manutencao) throws SQLException {
        String sql = "INSERT INTO manutencao_veiculo (veiculo_id, tipo, status, descricao, oficina, custo, "
                + "data_agendada, data_inicio, data_conclusao, quilometragem) "
                + "VALUES (?, ?::tipo_manutencao_enum, ?::status_manutencao_enum, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosManutencao(stmt, manutencao);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    manutencao.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<ManutencaoVeiculo> listarTodos() throws SQLException {
        String sql = sqlBase() + " ORDER BY mv.data_agendada DESC, mv.data_criacao DESC";
        List<ManutencaoVeiculo> manutencoes = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                manutencoes.add(mapearManutencao(rs));
            }
        }

        return manutencoes;
    }

    public ManutencaoVeiculo buscarPorId(Long id) throws SQLException {
        String sql = sqlBase() + " WHERE mv.id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearManutencao(rs);
                }
            }
        }

        return null;
    }

    public void atualizar(ManutencaoVeiculo manutencao) throws SQLException {
        String sql = "UPDATE manutencao_veiculo "
                + "SET veiculo_id = ?, tipo = ?::tipo_manutencao_enum, status = ?::status_manutencao_enum, "
                + "descricao = ?, oficina = ?, custo = ?, data_agendada = ?, data_inicio = ?, "
                + "data_conclusao = ?, quilometragem = ? "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametrosManutencao(stmt, manutencao);
            stmt.setLong(11, manutencao.getId());
            stmt.executeUpdate();
        }
    }

    public List<ManutencaoVeiculo> listarPorVeiculo(Long veiculoId) throws SQLException {
        String sql = sqlBase() + " WHERE mv.veiculo_id = ? ORDER BY mv.data_agendada DESC, mv.data_criacao DESC";
        List<ManutencaoVeiculo> manutencoes = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, veiculoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    manutencoes.add(mapearManutencao(rs));
                }
            }
        }

        return manutencoes;
    }

    private String sqlBase() {
        return "SELECT mv.id, mv.veiculo_id, mv.tipo, mv.status, mv.descricao, mv.oficina, mv.custo, "
                + "mv.data_agendada, mv.data_inicio, mv.data_conclusao, mv.quilometragem, mv.data_criacao, "
                + "v.placa AS veiculo_placa, v.modelo AS veiculo_modelo, v.marca AS veiculo_marca, "
                + "v.ano AS veiculo_ano, v.capacidade_kg AS veiculo_capacidade_kg, "
                + "v.tipo AS veiculo_tipo, v.status AS veiculo_status, v.quilometragem AS veiculo_quilometragem "
                + "FROM manutencao_veiculo mv "
                + "INNER JOIN veiculo v ON v.id = mv.veiculo_id";
    }

    private void preencherParametrosManutencao(PreparedStatement stmt, ManutencaoVeiculo manutencao)
            throws SQLException {

        stmt.setLong(1, manutencao.getVeiculo().getId());
        stmt.setString(2, manutencao.getTipo().name());
        stmt.setString(3, manutencao.getStatus().name());
        stmt.setString(4, manutencao.getDescricao());
        stmt.setString(5, manutencao.getOficina());
        stmt.setBigDecimal(6, manutencao.getCusto());
        stmt.setDate(7, Date.valueOf(manutencao.getDataAgendada()));
        preencherData(stmt, 8, manutencao.getDataInicio());
        preencherData(stmt, 9, manutencao.getDataConclusao());
        preencherLong(stmt, 10, manutencao.getQuilometragem());
    }

    private void preencherData(PreparedStatement stmt, int indice, java.time.LocalDate data) throws SQLException {
        if (data == null) {
            stmt.setNull(indice, Types.DATE);
            return;
        }

        stmt.setDate(indice, Date.valueOf(data));
    }

    private void preencherLong(PreparedStatement stmt, int indice, Long valor) throws SQLException {
        if (valor == null) {
            stmt.setNull(indice, Types.BIGINT);
            return;
        }

        stmt.setLong(indice, valor);
    }

    private ManutencaoVeiculo mapearManutencao(ResultSet rs) throws SQLException {
        ManutencaoVeiculo manutencao = new ManutencaoVeiculo();
        manutencao.setId(rs.getLong("id"));
        manutencao.setTipo(TipoManutencao.valueOf(rs.getString("tipo")));
        manutencao.setStatus(StatusManutencao.valueOf(rs.getString("status")));
        manutencao.setDescricao(rs.getString("descricao"));
        manutencao.setOficina(rs.getString("oficina"));
        manutencao.setCusto(rs.getBigDecimal("custo"));

        Date dataAgendada = rs.getDate("data_agendada");
        if (dataAgendada != null) {
            manutencao.setDataAgendada(dataAgendada.toLocalDate());
        }

        Date dataInicio = rs.getDate("data_inicio");
        if (dataInicio != null) {
            manutencao.setDataInicio(dataInicio.toLocalDate());
        }

        Date dataConclusao = rs.getDate("data_conclusao");
        if (dataConclusao != null) {
            manutencao.setDataConclusao(dataConclusao.toLocalDate());
        }

        Long quilometragem = rs.getLong("quilometragem");
        manutencao.setQuilometragem(rs.wasNull() ? null : quilometragem);

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            manutencao.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        Veiculo veiculo = new Veiculo();
        veiculo.setId(rs.getLong("veiculo_id"));
        veiculo.setPlaca(rs.getString("veiculo_placa"));
        veiculo.setModelo(rs.getString("veiculo_modelo"));
        veiculo.setMarca(rs.getString("veiculo_marca"));
        veiculo.setAno(rs.getInt("veiculo_ano"));
        veiculo.setCapacidadeKg(rs.getBigDecimal("veiculo_capacidade_kg"));
        veiculo.setTipo(TipoVeiculo.valueOf(rs.getString("veiculo_tipo")));
        veiculo.setStatus(StatusVeiculo.valueOf(rs.getString("veiculo_status")));
        veiculo.setQuilometragem(rs.getLong("veiculo_quilometragem"));
        manutencao.setVeiculo(veiculo);

        return manutencao;
    }
}
