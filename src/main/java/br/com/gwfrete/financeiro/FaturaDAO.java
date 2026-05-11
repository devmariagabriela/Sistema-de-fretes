package br.com.gwfrete.financeiro;

import br.com.gwfrete.cliente.Cliente;
import br.com.gwfrete.financeiro.Fatura;
import br.com.gwfrete.frete.Frete;
import br.com.gwfrete.financeiro.StatusFatura;
import br.com.gwfrete.frete.StatusFrete;
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

public class FaturaDAO {

    public String gerarNumeroAutomatico() throws SQLException {
        String sql = "SELECT 'FAT-' || LPAD(nextval('fatura_numero_seq')::text, 6, '0') AS numero";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("numero");
            }
        }

        throw new SQLException("Não foi possível gerar o número da fatura.");
    }

    public void salvar(Fatura fatura) throws SQLException {
        String sql = "INSERT INTO fatura (frete_id, cliente_id, numero, valor, data_emissao, data_vencimento, "
                + "data_pagamento, status, observacao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?::status_fatura_enum, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosFatura(stmt, fatura);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    fatura.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Fatura> listarTodos() throws SQLException {
        String sql = sqlBase() + " WHERE fa.status <> 'CANCELADO'::status_fatura_enum ORDER BY fa.data_criacao DESC";

        List<Fatura> faturas = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                faturas.add(mapearFatura(rs));
            }
        }

        return faturas;
    }

    public List<Fatura> listarComFiltros(StatusFatura status) throws SQLException {
        StringBuilder sql = new StringBuilder(sqlBase());
        List<Object> parametros = new ArrayList<>();

        if (status != null) {
            sql.append(" WHERE fa.status = ?::status_fatura_enum");
            parametros.add(status.name());
        } else {
            sql.append(" WHERE fa.status <> 'CANCELADO'::status_fatura_enum");
        }

        sql.append(" ORDER BY fa.data_criacao DESC");

        List<Fatura> faturas = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    faturas.add(mapearFatura(rs));
                }
            }
        }

        return faturas;
    }

    public Fatura buscarPorId(Long id) throws SQLException {
        String sql = sqlBase() + " WHERE fa.id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFatura(rs);
                }
            }
        }

        return null;
    }

    public Fatura buscarPorNumero(String numero) throws SQLException {
        String sql = sqlBase() + " WHERE UPPER(fa.numero) = UPPER(?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numero);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFatura(rs);
                }
            }
        }

        return null;
    }

    public void atualizar(Fatura fatura) throws SQLException {
        String sql = "UPDATE fatura "
                + "SET frete_id = ?, cliente_id = ?, numero = ?, valor = ?, data_emissao = ?, "
                + "data_vencimento = ?, data_pagamento = ?, status = ?::status_fatura_enum, observacao = ? "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametrosFatura(stmt, fatura);
            stmt.setLong(10, fatura.getId());
            stmt.executeUpdate();
        }
    }

    public void inativar(Long id) throws SQLException {
        String sql = "UPDATE fatura SET status = 'CANCELADO'::status_fatura_enum WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void marcarComoPago(Long id) throws SQLException {
        String sql = "UPDATE fatura "
                + "SET status = 'PAGO'::status_fatura_enum, data_pagamento = COALESCE(data_pagamento, CURRENT_DATE) "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private String sqlBase() {
        return "SELECT fa.id, fa.frete_id, fa.cliente_id, fa.numero, fa.valor, fa.data_emissao, "
                + "fa.data_vencimento, fa.data_pagamento, fa.status, fa.observacao, fa.data_criacao, "
                + "f.codigo AS frete_codigo, f.status AS frete_status, "
                + "c.nome AS cliente_nome, c.tipo AS cliente_tipo, c.cpf_cnpj AS cliente_cpf_cnpj "
                + "FROM fatura fa "
                + "INNER JOIN frete f ON f.id = fa.frete_id "
                + "INNER JOIN cliente c ON c.id = fa.cliente_id";
    }

    private void preencherParametrosFatura(PreparedStatement stmt, Fatura fatura) throws SQLException {
        stmt.setLong(1, fatura.getFrete().getId());
        stmt.setLong(2, fatura.getCliente().getId());
        stmt.setString(3, fatura.getNumero());
        stmt.setBigDecimal(4, fatura.getValor());
        stmt.setDate(5, Date.valueOf(fatura.getDataEmissao()));
        stmt.setDate(6, Date.valueOf(fatura.getDataVencimento()));
        preencherData(stmt, 7, fatura.getDataPagamento());
        stmt.setString(8, fatura.getStatus().name());
        stmt.setString(9, fatura.getObservacao());
    }

    private void preencherData(PreparedStatement stmt, int indice, java.time.LocalDate data) throws SQLException {
        if (data == null) {
            stmt.setNull(indice, Types.DATE);
            return;
        }

        stmt.setDate(indice, Date.valueOf(data));
    }

    private Fatura mapearFatura(ResultSet rs) throws SQLException {
        Fatura fatura = new Fatura();
        fatura.setId(rs.getLong("id"));
        fatura.setNumero(rs.getString("numero"));
        fatura.setValor(rs.getBigDecimal("valor"));
        fatura.setStatus(StatusFatura.valueOf(rs.getString("status")));
        fatura.setObservacao(rs.getString("observacao"));

        Date dataEmissao = rs.getDate("data_emissao");
        if (dataEmissao != null) {
            fatura.setDataEmissao(dataEmissao.toLocalDate());
        }

        Date dataVencimento = rs.getDate("data_vencimento");
        if (dataVencimento != null) {
            fatura.setDataVencimento(dataVencimento.toLocalDate());
        }

        Date dataPagamento = rs.getDate("data_pagamento");
        if (dataPagamento != null) {
            fatura.setDataPagamento(dataPagamento.toLocalDate());
        }

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            fatura.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        Frete frete = new Frete();
        frete.setId(rs.getLong("frete_id"));
        frete.setCodigo(rs.getString("frete_codigo"));
        frete.setStatus(StatusFrete.valueOf(rs.getString("frete_status")));
        fatura.setFrete(frete);

        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("cliente_id"));
        cliente.setNome(rs.getString("cliente_nome"));
        cliente.setTipo(TipoCliente.valueOf(rs.getString("cliente_tipo")));
        cliente.setCpfCnpj(rs.getString("cliente_cpf_cnpj"));
        fatura.setCliente(cliente);

        return fatura;
    }
}
