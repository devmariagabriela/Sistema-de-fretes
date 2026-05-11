package br.com.gwfrete.dashboard;

import br.com.gwfrete.dashboard.KPIExecutivoDTO;
import br.com.gwfrete.util.ConexaoFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KPIExecutivoDAO {

    public KPIExecutivoDTO buscarIndicadores() throws SQLException {
        String sql = "SELECT total_fretes, fretes_entregues, fretes_cancelados, fretes_em_transito, "
                + "total_clientes, total_veiculos, total_motoristas, total_faturas, faturamento_total, "
                + "faturas_pendentes, faturas_vencidas, total_contratos_ativos, total_ocorrencias, "
                + "total_notificacoes_nao_lidas, total_manutencoes_em_andamento "
                + "FROM vw_kpis_executivos";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return mapearIndicadores(rs);
            }
        }

        return new KPIExecutivoDTO();
    }

    private KPIExecutivoDTO mapearIndicadores(ResultSet rs) throws SQLException {
        KPIExecutivoDTO indicadores = new KPIExecutivoDTO();
        indicadores.setTotalFretes(rs.getInt("total_fretes"));
        indicadores.setFretesEntregues(rs.getInt("fretes_entregues"));
        indicadores.setFretesCancelados(rs.getInt("fretes_cancelados"));
        indicadores.setFretesEmTransito(rs.getInt("fretes_em_transito"));
        indicadores.setTotalClientes(rs.getInt("total_clientes"));
        indicadores.setTotalVeiculos(rs.getInt("total_veiculos"));
        indicadores.setTotalMotoristas(rs.getInt("total_motoristas"));
        indicadores.setTotalFaturas(rs.getInt("total_faturas"));
        indicadores.setFaturamentoTotal(obterBigDecimal(rs, "faturamento_total"));
        indicadores.setFaturasPendentes(rs.getInt("faturas_pendentes"));
        indicadores.setFaturasVencidas(rs.getInt("faturas_vencidas"));
        indicadores.setTotalContratosAtivos(rs.getInt("total_contratos_ativos"));
        indicadores.setTotalOcorrencias(rs.getInt("total_ocorrencias"));
        indicadores.setTotalNotificacoesNaoLidas(rs.getInt("total_notificacoes_nao_lidas"));
        indicadores.setTotalManutencoesEmAndamento(rs.getInt("total_manutencoes_em_andamento"));
        return indicadores;
    }

    private BigDecimal obterBigDecimal(ResultSet rs, String coluna) throws SQLException {
        BigDecimal valor = rs.getBigDecimal(coluna);
        return valor == null ? BigDecimal.ZERO : valor;
    }
}
