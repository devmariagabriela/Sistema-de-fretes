package br.com.gwfrete.dashboard;

import br.com.gwfrete.dashboard.DashboardDTO;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public DashboardDTO buscarIndicadores() throws SQLException {
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM frete) AS total_fretes, "
                + "(SELECT COUNT(*) FROM frete WHERE status = 'EM_TRANSITO') AS fretes_em_transito, "
                + "(SELECT COUNT(*) FROM frete WHERE status = 'ENTREGUE') AS fretes_entregues, "
                + "(SELECT COUNT(*) FROM frete WHERE status = 'CANCELADO') AS fretes_cancelados, "
                + "(SELECT COUNT(*) FROM veiculo) AS total_veiculos, "
                + "(SELECT COUNT(*) FROM veiculo WHERE status = 'DISPONIVEL') AS veiculos_disponiveis, "
                + "(SELECT COUNT(*) FROM veiculo WHERE status = 'MANUTENCAO') AS veiculos_em_manutencao, "
                + "(SELECT COUNT(*) FROM motorista) AS total_motoristas, "
                + "(SELECT COUNT(*) FROM motorista WHERE status = 'ATIVO') AS motoristas_ativos, "
                + "(SELECT COUNT(*) FROM ocorrencia_frete) AS total_ocorrencias";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return mapearDashboard(rs);
            }
        }

        return new DashboardDTO();
    }

    private DashboardDTO mapearDashboard(ResultSet rs) throws SQLException {
        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setTotalFretes(rs.getInt("total_fretes"));
        dashboard.setFretesEmTransito(rs.getInt("fretes_em_transito"));
        dashboard.setFretesEntregues(rs.getInt("fretes_entregues"));
        dashboard.setFretesCancelados(rs.getInt("fretes_cancelados"));
        dashboard.setTotalVeiculos(rs.getInt("total_veiculos"));
        dashboard.setVeiculosDisponiveis(rs.getInt("veiculos_disponiveis"));
        dashboard.setVeiculosEmManutencao(rs.getInt("veiculos_em_manutencao"));
        dashboard.setTotalMotoristas(rs.getInt("total_motoristas"));
        dashboard.setMotoristasAtivos(rs.getInt("motoristas_ativos"));
        dashboard.setTotalOcorrencias(rs.getInt("total_ocorrencias"));
        return dashboard;
    }
}
