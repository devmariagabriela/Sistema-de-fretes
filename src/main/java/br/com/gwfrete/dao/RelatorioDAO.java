package br.com.gwfrete.dao;

import br.com.gwfrete.model.RelatorioFreteDTO;
import br.com.gwfrete.model.StatusFrete;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {

    public List<RelatorioFreteDTO> listarFretesOperacionais() throws SQLException {
        return listarFretesOperacionais(null, null, null, null, null);
    }

    public List<RelatorioFreteDTO> listarFretesOperacionais(LocalDate dataInicial, LocalDate dataFinal,
            StatusFrete status, String motorista, String veiculo) throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT f.codigo, f.origem, f.destino, m.nome AS motorista, "
                + "v.placa AS veiculo_placa, v.modelo AS veiculo_modelo, f.status, "
                + "f.data_saida, f.data_entrega, f.valor_frete "
                + "FROM frete f "
                + "INNER JOIN motorista m ON m.id = f.motorista_id "
                + "INNER JOIN veiculo v ON v.id = f.veiculo_id "
                + "WHERE 1 = 1 ");

        List<Object> parametros = new ArrayList<>();

        if (dataInicial != null) {
            sql.append("AND f.data_saida >= ? ");
            parametros.add(Timestamp.valueOf(dataInicial.atStartOfDay()));
        }

        if (dataFinal != null) {
            sql.append("AND f.data_saida < ? ");
            parametros.add(Timestamp.valueOf(dataFinal.plusDays(1).atStartOfDay()));
        }

        if (status != null) {
            sql.append("AND f.status = ?::status_frete_enum ");
            parametros.add(status.name());
        }

        if (motorista != null && !motorista.trim().isEmpty()) {
            sql.append("AND LOWER(m.nome) LIKE LOWER(?) ");
            parametros.add("%" + motorista.trim() + "%");
        }

        if (veiculo != null && !veiculo.trim().isEmpty()) {
            sql.append("AND (LOWER(v.placa) LIKE LOWER(?) OR LOWER(v.modelo) LIKE LOWER(?)) ");
            String filtroVeiculo = "%" + veiculo.trim() + "%";
            parametros.add(filtroVeiculo);
            parametros.add(filtroVeiculo);
        }

        sql.append("ORDER BY f.data_criacao DESC");

        List<RelatorioFreteDTO> fretes = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    fretes.add(mapearRelatorioFrete(rs));
                }
            }
        }

        return fretes;
    }

    private RelatorioFreteDTO mapearRelatorioFrete(ResultSet rs) throws SQLException {
        RelatorioFreteDTO frete = new RelatorioFreteDTO();
        frete.setCodigo(rs.getString("codigo"));
        frete.setOrigem(rs.getString("origem"));
        frete.setDestino(rs.getString("destino"));
        frete.setMotorista(rs.getString("motorista"));
        frete.setVeiculo(rs.getString("veiculo_placa") + " - " + rs.getString("veiculo_modelo"));
        frete.setStatus(StatusFrete.valueOf(rs.getString("status")));
        frete.setValorFrete(rs.getBigDecimal("valor_frete"));

        Timestamp dataSaida = rs.getTimestamp("data_saida");
        if (dataSaida != null) {
            frete.setDataSaida(dataSaida.toLocalDateTime());
        }

        Timestamp dataEntrega = rs.getTimestamp("data_entrega");
        if (dataEntrega != null) {
            frete.setDataEntrega(dataEntrega.toLocalDateTime());
        }

        return frete;
    }
}
