package br.com.gwfrete.dao;

import br.com.gwfrete.model.Frete;
import br.com.gwfrete.model.Motorista;
import br.com.gwfrete.model.StatusFrete;
import br.com.gwfrete.model.Veiculo;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class FreteDAO {

    public void salvar(Frete frete) throws SQLException {
        String sql = "INSERT INTO frete (codigo, origem, destino, descricao_carga, peso_kg, valor_frete, "
                + "data_saida, data_entrega, status, motorista_id, veiculo_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::status_frete_enum, ?, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosFrete(stmt, frete);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    frete.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Frete> listarTodos() throws SQLException {
        String sql = "SELECT f.id, f.codigo, f.origem, f.destino, f.descricao_carga, f.peso_kg, "
                + "f.valor_frete, f.data_saida, f.data_entrega, f.status, f.motorista_id, f.veiculo_id, "
                + "f.data_criacao, m.nome AS motorista_nome, v.placa AS veiculo_placa, v.modelo AS veiculo_modelo "
                + "FROM frete f "
                + "INNER JOIN motorista m ON m.id = f.motorista_id "
                + "INNER JOIN veiculo v ON v.id = f.veiculo_id "
                + "ORDER BY f.data_criacao DESC";

        List<Frete> fretes = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                fretes.add(mapearFrete(rs));
            }
        }

        return fretes;
    }

    public Frete buscarPorId(Long id) throws SQLException {
        String sql = "SELECT f.id, f.codigo, f.origem, f.destino, f.descricao_carga, f.peso_kg, "
                + "f.valor_frete, f.data_saida, f.data_entrega, f.status, f.motorista_id, f.veiculo_id, "
                + "f.data_criacao, m.nome AS motorista_nome, v.placa AS veiculo_placa, v.modelo AS veiculo_modelo "
                + "FROM frete f "
                + "INNER JOIN motorista m ON m.id = f.motorista_id "
                + "INNER JOIN veiculo v ON v.id = f.veiculo_id "
                + "WHERE f.id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFrete(rs);
                }
            }
        }

        return null;
    }

    public Frete buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT f.id, f.codigo, f.origem, f.destino, f.descricao_carga, f.peso_kg, "
                + "f.valor_frete, f.data_saida, f.data_entrega, f.status, f.motorista_id, f.veiculo_id, "
                + "f.data_criacao, m.nome AS motorista_nome, v.placa AS veiculo_placa, v.modelo AS veiculo_modelo "
                + "FROM frete f "
                + "INNER JOIN motorista m ON m.id = f.motorista_id "
                + "INNER JOIN veiculo v ON v.id = f.veiculo_id "
                + "WHERE UPPER(f.codigo) = UPPER(?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFrete(rs);
                }
            }
        }

        return null;
    }

    public void atualizar(Frete frete) throws SQLException {
        String sql = "UPDATE frete "
                + "SET codigo = ?, origem = ?, destino = ?, descricao_carga = ?, peso_kg = ?, valor_frete = ?, "
                + "data_saida = ?, data_entrega = ?, status = ?::status_frete_enum, motorista_id = ?, veiculo_id = ? "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametrosFrete(stmt, frete);
            stmt.setLong(12, frete.getId());
            stmt.executeUpdate();
        }
    }

    private void preencherParametrosFrete(PreparedStatement stmt, Frete frete) throws SQLException {
        stmt.setString(1, frete.getCodigo());
        stmt.setString(2, frete.getOrigem());
        stmt.setString(3, frete.getDestino());
        stmt.setString(4, frete.getDescricaoCarga());
        stmt.setBigDecimal(5, frete.getPesoKg());
        stmt.setBigDecimal(6, frete.getValorFrete());
        preencherTimestamp(stmt, 7, frete.getDataSaida());
        preencherTimestamp(stmt, 8, frete.getDataEntrega());
        stmt.setString(9, frete.getStatus().name());
        stmt.setLong(10, frete.getMotorista().getId());
        stmt.setLong(11, frete.getVeiculo().getId());
    }

    private void preencherTimestamp(PreparedStatement stmt, int indice, java.time.LocalDateTime data)
            throws SQLException {

        if (data == null) {
            stmt.setNull(indice, Types.TIMESTAMP);
            return;
        }

        stmt.setTimestamp(indice, Timestamp.valueOf(data));
    }

    private Frete mapearFrete(ResultSet rs) throws SQLException {
        Frete frete = new Frete();
        frete.setId(rs.getLong("id"));
        frete.setCodigo(rs.getString("codigo"));
        frete.setOrigem(rs.getString("origem"));
        frete.setDestino(rs.getString("destino"));
        frete.setDescricaoCarga(rs.getString("descricao_carga"));
        frete.setPesoKg(rs.getBigDecimal("peso_kg"));
        frete.setValorFrete(rs.getBigDecimal("valor_frete"));
        frete.setStatus(StatusFrete.valueOf(rs.getString("status")));

        Timestamp dataSaida = rs.getTimestamp("data_saida");
        if (dataSaida != null) {
            frete.setDataSaida(dataSaida.toLocalDateTime());
        }

        Timestamp dataEntrega = rs.getTimestamp("data_entrega");
        if (dataEntrega != null) {
            frete.setDataEntrega(dataEntrega.toLocalDateTime());
        }

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            frete.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        Motorista motorista = new Motorista();
        motorista.setId(rs.getLong("motorista_id"));
        motorista.setNome(rs.getString("motorista_nome"));
        frete.setMotorista(motorista);

        Veiculo veiculo = new Veiculo();
        veiculo.setId(rs.getLong("veiculo_id"));
        veiculo.setPlaca(rs.getString("veiculo_placa"));
        veiculo.setModelo(rs.getString("veiculo_modelo"));
        frete.setVeiculo(veiculo);

        return frete;
    }
}
