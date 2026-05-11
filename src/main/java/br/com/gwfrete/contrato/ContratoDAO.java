package br.com.gwfrete.contrato;

import br.com.gwfrete.cliente.Cliente;
import br.com.gwfrete.contrato.Contrato;
import br.com.gwfrete.contrato.StatusContrato;
import br.com.gwfrete.cliente.TipoCliente;
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

public class ContratoDAO {

    public void salvar(Contrato contrato) throws SQLException {
        String sql = "INSERT INTO contrato (cliente_id, numero, descricao, valor_mensal, data_inicio, "
                + "data_fim, reajuste_percentual, status, observacao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?::status_contrato_enum, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosContrato(stmt, contrato);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    contrato.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Contrato> listarTodos() throws SQLException {
        String sql = sqlBase() + " WHERE co.status = 'ATIVO'::status_contrato_enum ORDER BY co.data_criacao DESC";
        List<Contrato> contratos = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                contratos.add(mapearContrato(rs));
            }
        }

        return contratos;
    }

    public List<Contrato> listarComFiltros(StatusContrato status) throws SQLException {
        StringBuilder sql = new StringBuilder(sqlBase());
        List<Object> parametros = new ArrayList<>();

        if (status != null) {
            sql.append(" WHERE co.status = ?::status_contrato_enum");
            parametros.add(status.name());
        } else {
            sql.append(" WHERE co.status = 'ATIVO'::status_contrato_enum");
        }

        sql.append(" ORDER BY co.data_criacao DESC");

        List<Contrato> contratos = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contratos.add(mapearContrato(rs));
                }
            }
        }

        return contratos;
    }

    public Contrato buscarPorId(Long id) throws SQLException {
        String sql = sqlBase() + " WHERE co.id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearContrato(rs);
                }
            }
        }

        return null;
    }

    public Contrato buscarPorNumero(String numero) throws SQLException {
        String sql = sqlBase() + " WHERE UPPER(co.numero) = UPPER(?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numero);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearContrato(rs);
                }
            }
        }

        return null;
    }

    public void atualizar(Contrato contrato) throws SQLException {
        String sql = "UPDATE contrato "
                + "SET cliente_id = ?, numero = ?, descricao = ?, valor_mensal = ?, data_inicio = ?, "
                + "data_fim = ?, reajuste_percentual = ?, status = ?::status_contrato_enum, observacao = ? "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametrosContrato(stmt, contrato);
            stmt.setLong(10, contrato.getId());
            stmt.executeUpdate();
        }
    }

    public void inativar(Long id) throws SQLException {
        String sql = "UPDATE contrato "
                + "SET status = 'ENCERRADO'::status_contrato_enum, data_fim = COALESCE(data_fim, CURRENT_DATE) "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void suspender(Long id) throws SQLException {
        alterarStatus(id, StatusContrato.SUSPENSO, false);
    }

    public void cancelar(Long id) throws SQLException {
        alterarStatus(id, StatusContrato.CANCELADO, true);
    }

    private void alterarStatus(Long id, StatusContrato status, boolean preencherDataFim) throws SQLException {
        String sql = preencherDataFim
                ? "UPDATE contrato SET status = ?::status_contrato_enum, data_fim = COALESCE(data_fim, CURRENT_DATE) WHERE id = ?"
                : "UPDATE contrato SET status = ?::status_contrato_enum WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

    private String sqlBase() {
        return "SELECT co.id, co.cliente_id, co.numero, co.descricao, co.valor_mensal, "
                + "co.data_inicio, co.data_fim, co.reajuste_percentual, co.status, "
                + "co.observacao, co.data_criacao, "
                + "c.nome AS cliente_nome, c.tipo AS cliente_tipo, c.cpf_cnpj AS cliente_cpf_cnpj "
                + "FROM contrato co "
                + "INNER JOIN cliente c ON c.id = co.cliente_id";
    }

    private void preencherParametrosContrato(PreparedStatement stmt, Contrato contrato) throws SQLException {
        stmt.setLong(1, contrato.getCliente().getId());
        stmt.setString(2, contrato.getNumero());
        stmt.setString(3, contrato.getDescricao());
        stmt.setBigDecimal(4, contrato.getValorMensal());
        stmt.setDate(5, Date.valueOf(contrato.getDataInicio()));
        preencherData(stmt, 6, contrato.getDataFim());
        stmt.setBigDecimal(7, contrato.getReajustePercentual());
        stmt.setString(8, contrato.getStatus().name());
        stmt.setString(9, contrato.getObservacao());
    }

    private void preencherData(PreparedStatement stmt, int indice, java.time.LocalDate data) throws SQLException {
        if (data == null) {
            stmt.setNull(indice, Types.DATE);
            return;
        }

        stmt.setDate(indice, Date.valueOf(data));
    }

    private Contrato mapearContrato(ResultSet rs) throws SQLException {
        Contrato contrato = new Contrato();
        contrato.setId(rs.getLong("id"));
        contrato.setNumero(rs.getString("numero"));
        contrato.setDescricao(rs.getString("descricao"));
        contrato.setValorMensal(rs.getBigDecimal("valor_mensal"));
        contrato.setReajustePercentual(rs.getBigDecimal("reajuste_percentual"));
        contrato.setStatus(StatusContrato.valueOf(rs.getString("status")));
        contrato.setObservacao(rs.getString("observacao"));

        Date dataInicio = rs.getDate("data_inicio");
        if (dataInicio != null) {
            contrato.setDataInicio(dataInicio.toLocalDate());
        }

        Date dataFim = rs.getDate("data_fim");
        if (dataFim != null) {
            contrato.setDataFim(dataFim.toLocalDate());
        }

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            contrato.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("cliente_id"));
        cliente.setNome(rs.getString("cliente_nome"));
        cliente.setTipo(TipoCliente.valueOf(rs.getString("cliente_tipo")));
        cliente.setCpfCnpj(rs.getString("cliente_cpf_cnpj"));
        contrato.setCliente(cliente);

        return contrato;
    }
}
