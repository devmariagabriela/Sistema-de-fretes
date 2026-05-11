package br.com.gwfrete.notificacao;

import br.com.gwfrete.notificacao.Notificacao;
import br.com.gwfrete.notificacao.StatusNotificacao;
import br.com.gwfrete.notificacao.TipoNotificacao;
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

    public void salvarSeNaoExistir(Notificacao notificacao) throws SQLException {
        if (existeNotificacao(notificacao)) {
            return;
        }

        salvar(notificacao);
    }

    public boolean existeNotificacao(Notificacao notificacao) throws SQLException {
        String sql = "SELECT 1 FROM notificacao "
                + "WHERE tipo = ?::tipo_notificacao_enum "
                + "AND titulo = ? "
                + "AND referencia_tipo IS NOT DISTINCT FROM ? "
                + "AND referencia_id IS NOT DISTINCT FROM ? "
                + "LIMIT 1";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, notificacao.getTipo().name());
            stmt.setString(2, notificacao.getTitulo());
            stmt.setString(3, notificacao.getReferenciaTipo());
            preencherLong(stmt, 4, notificacao.getReferenciaId());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void gerarNotificacoesAutomaticasPorDatas() throws SQLException {
        gerarNotificacoesContratos();
        gerarNotificacoesFaturas();
        gerarNotificacoesManutencoes();
        gerarNotificacoesFretes();
        gerarNotificacoesMotoristas();
    }

    public List<Notificacao> listarTodas() throws SQLException {
        String sql = sqlBase() + " WHERE status <> 'ARQUIVADA' ORDER BY data_criacao DESC";
        return listarPorSql(sql);
    }

    public List<Notificacao> listarTodasIncluindoArquivadas() throws SQLException {
        String sql = sqlBase() + " ORDER BY data_criacao DESC";
        return listarPorSql(sql);
    }

    public List<Notificacao> listarNaoLidas() throws SQLException {
        String sql = sqlBase() + " WHERE status = 'NAO_LIDA' ORDER BY data_criacao DESC";
        return listarPorSql(sql);
    }

    public List<Notificacao> listarPorStatus(StatusNotificacao status) throws SQLException {
        String sql = sqlBase() + " WHERE status = ?::status_notificacao_enum ORDER BY data_criacao DESC";
        List<Notificacao> notificacoes = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notificacoes.add(mapearNotificacao(rs));
                }
            }
        }

        return notificacoes;
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

    public void inativar(Long id) throws SQLException {
        arquivar(id);
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

    private void gerarNotificacoesContratos() throws SQLException {
        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'OUTROS'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'Contrato vencendo', "
                + "'Contrato ' || co.numero || ' vence em ' || TO_CHAR(co.data_fim, 'DD/MM/YYYY') || '.', "
                + "co.id, 'CONTRATO' "
                + "FROM contrato co "
                + "WHERE co.data_fim BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '30 days' "
                + "AND co.status = 'ATIVO' "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'OUTROS' AND n.titulo = 'Contrato vencendo' "
                + "AND n.referencia_tipo = 'CONTRATO' AND n.referencia_id = co.id)");

        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'OUTROS'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'Contrato vencido', "
                + "'Contrato ' || co.numero || ' venceu em ' || TO_CHAR(co.data_fim, 'DD/MM/YYYY') || '.', "
                + "co.id, 'CONTRATO' "
                + "FROM contrato co "
                + "WHERE co.data_fim < CURRENT_DATE "
                + "AND co.status = 'ATIVO' "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'OUTROS' AND n.titulo = 'Contrato vencido' "
                + "AND n.referencia_tipo = 'CONTRATO' AND n.referencia_id = co.id)");
    }

    private void gerarNotificacoesFaturas() throws SQLException {
        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'OUTROS'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'Fatura vencendo', "
                + "'Fatura ' || fa.numero || ' vence em ' || TO_CHAR(fa.data_vencimento, 'DD/MM/YYYY') || '.', "
                + "fa.id, 'FATURA' "
                + "FROM fatura fa "
                + "WHERE fa.data_vencimento BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '3 days' "
                + "AND fa.status = 'PENDENTE' "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'OUTROS' AND n.titulo = 'Fatura vencendo' "
                + "AND n.referencia_tipo = 'FATURA' AND n.referencia_id = fa.id)");

        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'FATURA_VENCIDA'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'Fatura vencida', "
                + "'Fatura ' || fa.numero || ' venceu em ' || TO_CHAR(fa.data_vencimento, 'DD/MM/YYYY') || '.', "
                + "fa.id, 'FATURA' "
                + "FROM fatura fa "
                + "WHERE ((fa.data_vencimento < CURRENT_DATE AND fa.status = 'PENDENTE') OR fa.status = 'VENCIDO') "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'FATURA_VENCIDA' AND n.titulo = 'Fatura vencida' "
                + "AND n.referencia_tipo = 'FATURA' AND n.referencia_id = fa.id)");

        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'OUTROS'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'Fatura paga', "
                + "'Fatura ' || fa.numero || ' foi marcada como paga.', "
                + "fa.id, 'FATURA' "
                + "FROM fatura fa "
                + "WHERE fa.status = 'PAGO' "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'OUTROS' AND n.titulo = 'Fatura paga' "
                + "AND n.referencia_tipo = 'FATURA' AND n.referencia_id = fa.id)");
    }

    private void gerarNotificacoesManutencoes() throws SQLException {
        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'VEICULO_MANUTENCAO'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'Manutenção agendada hoje', "
                + "'Manutenção do veículo ' || v.placa || ' está agendada para hoje.', "
                + "mv.id, 'MANUTENCAO' "
                + "FROM manutencao_veiculo mv INNER JOIN veiculo v ON v.id = mv.veiculo_id "
                + "WHERE mv.data_agendada = CURRENT_DATE "
                + "AND mv.status IN ('AGENDADA', 'EM_ANDAMENTO') "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'VEICULO_MANUTENCAO' "
                + "AND n.titulo = 'Manutenção agendada hoje' AND n.referencia_tipo = 'MANUTENCAO' AND n.referencia_id = mv.id)");

        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'VEICULO_MANUTENCAO'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'Manutenção em atraso', "
                + "'Manutenção do veículo ' || v.placa || ' está em atraso desde ' || TO_CHAR(mv.data_agendada, 'DD/MM/YYYY') || '.', "
                + "mv.id, 'MANUTENCAO' "
                + "FROM manutencao_veiculo mv INNER JOIN veiculo v ON v.id = mv.veiculo_id "
                + "WHERE mv.data_agendada < CURRENT_DATE "
                + "AND mv.status IN ('AGENDADA', 'EM_ANDAMENTO') "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'VEICULO_MANUTENCAO' "
                + "AND n.titulo = 'Manutenção em atraso' AND n.referencia_tipo = 'MANUTENCAO' AND n.referencia_id = mv.id)");
    }

    private void gerarNotificacoesFretes() throws SQLException {
        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'OUTROS'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'Frete atrasado', "
                + "'Frete ' || f.codigo || ' está atrasado. Entrega prevista: ' || TO_CHAR(f.data_entrega, 'DD/MM/YYYY HH24:MI') || '.', "
                + "f.id, 'FRETE' "
                + "FROM frete f "
                + "WHERE f.data_entrega < CURRENT_TIMESTAMP "
                + "AND f.status NOT IN ('ENTREGUE', 'CANCELADO') "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'OUTROS' AND n.titulo = 'Frete atrasado' "
                + "AND n.referencia_tipo = 'FRETE' AND n.referencia_id = f.id)");

        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'FRETE_CANCELADO'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'Frete cancelado', 'Frete ' || f.codigo || ' foi cancelado.', f.id, 'FRETE' "
                + "FROM frete f WHERE f.status = 'CANCELADO' "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'FRETE_CANCELADO' AND n.titulo = 'Frete cancelado' "
                + "AND n.referencia_tipo = 'FRETE' AND n.referencia_id = f.id)");

        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'OUTROS'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'Frete entregue', 'Frete ' || f.codigo || ' foi entregue.', f.id, 'FRETE' "
                + "FROM frete f WHERE f.status = 'ENTREGUE' "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'OUTROS' AND n.titulo = 'Frete entregue' "
                + "AND n.referencia_tipo = 'FRETE' AND n.referencia_id = f.id)");
    }

    private void gerarNotificacoesMotoristas() throws SQLException {
        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'CNH_VENCENDO'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'CNH vencendo', "
                + "'CNH do motorista ' || nome || ' vence em ' || TO_CHAR(cnh_validade, 'DD/MM/YYYY') || '.', "
                + "id, 'MOTORISTA' "
                + "FROM motorista "
                + "WHERE cnh_validade BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '30 days' "
                + "AND status = 'ATIVO' "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'CNH_VENCENDO' AND n.titulo = 'CNH vencendo' "
                + "AND n.referencia_tipo = 'MOTORISTA' AND n.referencia_id = motorista.id)");

        executarInsercaoAutomatica("INSERT INTO notificacao (tipo, status, titulo, mensagem, referencia_id, referencia_tipo) "
                + "SELECT 'CNH_VENCENDO'::tipo_notificacao_enum, 'NAO_LIDA'::status_notificacao_enum, "
                + "'CNH vencida', "
                + "'CNH do motorista ' || nome || ' venceu em ' || TO_CHAR(cnh_validade, 'DD/MM/YYYY') || '.', "
                + "id, 'MOTORISTA' "
                + "FROM motorista "
                + "WHERE cnh_validade < CURRENT_DATE "
                + "AND status = 'ATIVO' "
                + "AND NOT EXISTS (SELECT 1 FROM notificacao n WHERE n.tipo = 'CNH_VENCENDO' AND n.titulo = 'CNH vencida' "
                + "AND n.referencia_tipo = 'MOTORISTA' AND n.referencia_id = motorista.id)");
    }

    private void executarInsercaoAutomatica(String sql) throws SQLException {
        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
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
