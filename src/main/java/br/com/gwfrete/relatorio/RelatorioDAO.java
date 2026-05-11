package br.com.gwfrete.relatorio;

import br.com.gwfrete.relatorio.RelatorioContratoDTO;
import br.com.gwfrete.relatorio.RelatorioClienteDTO;
import br.com.gwfrete.relatorio.RelatorioFinanceiroDTO;
import br.com.gwfrete.relatorio.RelatorioFreteDTO;
import br.com.gwfrete.relatorio.RelatorioManutencaoDTO;
import br.com.gwfrete.relatorio.RelatorioMotoristaDTO;
import br.com.gwfrete.relatorio.RelatorioOcorrenciaDTO;
import br.com.gwfrete.relatorio.RelatorioVeiculoDTO;
import br.com.gwfrete.contrato.StatusContrato;
import br.com.gwfrete.financeiro.StatusFatura;
import br.com.gwfrete.frete.StatusFrete;
import br.com.gwfrete.manutencao.StatusManutencao;
import br.com.gwfrete.motorista.StatusMotorista;
import br.com.gwfrete.veiculo.StatusVeiculo;
import br.com.gwfrete.cliente.TipoCliente;
import br.com.gwfrete.manutencao.TipoManutencao;
import br.com.gwfrete.ocorrencia.TipoOcorrenciaFrete;
import br.com.gwfrete.veiculo.TipoVeiculo;
import br.com.gwfrete.motorista.TipoVinculoMotorista;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.Date;
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

    public List<RelatorioContratoDTO> listarContratosGerenciais(LocalDate dataInicial, LocalDate dataFinal,
            StatusContrato status, String cliente) throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT co.numero, c.nome AS cliente, co.valor_mensal, "
                + "co.data_inicio, co.data_fim, co.status "
                + "FROM contrato co "
                + "INNER JOIN cliente c ON c.id = co.cliente_id "
                + "WHERE 1 = 1 ");

        List<Object> parametros = new ArrayList<>();

        if (cliente != null && !cliente.trim().isEmpty()) {
            sql.append("AND LOWER(c.nome) LIKE LOWER(?) ");
            parametros.add("%" + cliente.trim() + "%");
        }

        if (status != null) {
            sql.append("AND co.status = ?::status_contrato_enum ");
            parametros.add(status.name());
        }

        if (dataInicial != null) {
            sql.append("AND co.data_inicio >= ? ");
            parametros.add(Date.valueOf(dataInicial));
        }

        if (dataFinal != null) {
            sql.append("AND co.data_inicio <= ? ");
            parametros.add(Date.valueOf(dataFinal));
        }

        sql.append("ORDER BY co.data_criacao DESC");

        List<RelatorioContratoDTO> contratos = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contratos.add(mapearRelatorioContrato(rs));
                }
            }
        }

        return contratos;
    }

    public List<RelatorioFinanceiroDTO> listarFinanceiro() throws SQLException {
        String sql = "SELECT fa.numero, c.nome AS cliente, f.codigo AS frete, fa.valor, "
                + "fa.data_emissao, fa.data_vencimento, fa.data_pagamento, fa.status "
                + "FROM fatura fa "
                + "INNER JOIN cliente c ON c.id = fa.cliente_id "
                + "INNER JOIN frete f ON f.id = fa.frete_id "
                + "ORDER BY fa.data_emissao DESC, fa.id DESC";
        List<RelatorioFinanceiroDTO> itens = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                itens.add(mapearRelatorioFinanceiro(rs));
            }
        }

        return itens;
    }

    public List<RelatorioManutencaoDTO> listarManutencoes() throws SQLException {
        String sql = "SELECT v.placa, v.modelo, mv.tipo, mv.status, mv.descricao, mv.oficina, "
                + "mv.custo, mv.data_agendada, mv.data_conclusao "
                + "FROM manutencao_veiculo mv "
                + "INNER JOIN veiculo v ON v.id = mv.veiculo_id "
                + "ORDER BY mv.data_agendada DESC, mv.id DESC";
        List<RelatorioManutencaoDTO> itens = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                itens.add(mapearRelatorioManutencao(rs));
            }
        }

        return itens;
    }

    public List<RelatorioOcorrenciaDTO> listarOcorrencias() throws SQLException {
        String sql = "SELECT f.codigo AS frete, ofr.tipo, ofr.data_hora, ofr.localizacao, "
                + "ofr.descricao, ofr.nome_recebedor, ofr.documento_recebedor "
                + "FROM ocorrencia_frete ofr "
                + "INNER JOIN frete f ON f.id = ofr.frete_id "
                + "ORDER BY ofr.data_hora DESC, ofr.id DESC";
        List<RelatorioOcorrenciaDTO> itens = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                itens.add(mapearRelatorioOcorrencia(rs));
            }
        }

        return itens;
    }

    public List<RelatorioClienteDTO> listarClientes() throws SQLException {
        String sql = "SELECT nome, tipo, cpf_cnpj, email, telefone, cidade, estado, status "
                + "FROM cliente ORDER BY data_criacao DESC, id DESC";
        List<RelatorioClienteDTO> itens = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                itens.add(mapearRelatorioCliente(rs));
            }
        }

        return itens;
    }

    public List<RelatorioMotoristaDTO> listarMotoristas() throws SQLException {
        String sql = "SELECT nome, cpf, telefone, cnh_numero, cnh_categoria, cnh_validade, "
                + "tipo_vinculo, status FROM motorista ORDER BY data_criacao DESC, id DESC";
        List<RelatorioMotoristaDTO> itens = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                itens.add(mapearRelatorioMotorista(rs));
            }
        }

        return itens;
    }

    public List<RelatorioVeiculoDTO> listarVeiculos() throws SQLException {
        String sql = "SELECT placa, modelo, tipo, status, capacidade_kg, quilometragem, data_criacao "
                + "FROM veiculo ORDER BY data_criacao DESC, id DESC";
        List<RelatorioVeiculoDTO> itens = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                itens.add(mapearRelatorioVeiculo(rs));
            }
        }

        return itens;
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

    private RelatorioContratoDTO mapearRelatorioContrato(ResultSet rs) throws SQLException {
        RelatorioContratoDTO contrato = new RelatorioContratoDTO();
        contrato.setNumero(rs.getString("numero"));
        contrato.setCliente(rs.getString("cliente"));
        contrato.setValorMensal(rs.getBigDecimal("valor_mensal"));
        contrato.setStatus(StatusContrato.valueOf(rs.getString("status")));

        Date dataInicio = rs.getDate("data_inicio");
        if (dataInicio != null) {
            contrato.setDataInicio(dataInicio.toLocalDate());
        }

        Date dataFim = rs.getDate("data_fim");
        if (dataFim != null) {
            contrato.setDataFim(dataFim.toLocalDate());
        }

        return contrato;
    }

    private RelatorioFinanceiroDTO mapearRelatorioFinanceiro(ResultSet rs) throws SQLException {
        RelatorioFinanceiroDTO item = new RelatorioFinanceiroDTO();
        item.setNumero(rs.getString("numero"));
        item.setCliente(rs.getString("cliente"));
        item.setFrete(rs.getString("frete"));
        item.setValor(rs.getBigDecimal("valor"));
        item.setStatus(StatusFatura.valueOf(rs.getString("status")));
        Date dataEmissao = rs.getDate("data_emissao");
        Date dataVencimento = rs.getDate("data_vencimento");
        Date dataPagamento = rs.getDate("data_pagamento");
        if (dataEmissao != null) item.setDataEmissao(dataEmissao.toLocalDate());
        if (dataVencimento != null) item.setDataVencimento(dataVencimento.toLocalDate());
        if (dataPagamento != null) item.setDataPagamento(dataPagamento.toLocalDate());
        return item;
    }

    private RelatorioManutencaoDTO mapearRelatorioManutencao(ResultSet rs) throws SQLException {
        RelatorioManutencaoDTO item = new RelatorioManutencaoDTO();
        item.setVeiculo(rs.getString("placa") + " - " + rs.getString("modelo"));
        item.setTipo(TipoManutencao.valueOf(rs.getString("tipo")));
        item.setStatus(StatusManutencao.valueOf(rs.getString("status")));
        item.setDescricao(rs.getString("descricao"));
        item.setOficina(rs.getString("oficina"));
        item.setCusto(rs.getBigDecimal("custo"));
        Date dataAgendada = rs.getDate("data_agendada");
        Date dataConclusao = rs.getDate("data_conclusao");
        if (dataAgendada != null) item.setDataAgendada(dataAgendada.toLocalDate());
        if (dataConclusao != null) item.setDataConclusao(dataConclusao.toLocalDate());
        return item;
    }

    private RelatorioOcorrenciaDTO mapearRelatorioOcorrencia(ResultSet rs) throws SQLException {
        RelatorioOcorrenciaDTO item = new RelatorioOcorrenciaDTO();
        item.setFrete(rs.getString("frete"));
        item.setTipo(TipoOcorrenciaFrete.valueOf(rs.getString("tipo")));
        Timestamp dataHora = rs.getTimestamp("data_hora");
        if (dataHora != null) item.setDataHora(dataHora.toLocalDateTime());
        item.setLocalizacao(rs.getString("localizacao"));
        item.setDescricao(rs.getString("descricao"));
        String recebedor = rs.getString("nome_recebedor");
        String documento = rs.getString("documento_recebedor");
        item.setRecebedor(recebedor == null ? null : (documento == null ? recebedor : recebedor + " - " + documento));
        return item;
    }

    private RelatorioClienteDTO mapearRelatorioCliente(ResultSet rs) throws SQLException {
        RelatorioClienteDTO item = new RelatorioClienteDTO();
        item.setNome(rs.getString("nome"));
        item.setTipo(TipoCliente.valueOf(rs.getString("tipo")));
        item.setCpfCnpj(rs.getString("cpf_cnpj"));
        item.setEmail(rs.getString("email"));
        item.setTelefone(rs.getString("telefone"));
        item.setCidade(rs.getString("cidade"));
        item.setEstado(rs.getString("estado"));
        item.setStatus(rs.getBoolean("status"));
        return item;
    }

    private RelatorioMotoristaDTO mapearRelatorioMotorista(ResultSet rs) throws SQLException {
        RelatorioMotoristaDTO item = new RelatorioMotoristaDTO();
        item.setNome(rs.getString("nome"));
        item.setCpf(rs.getString("cpf"));
        item.setTelefone(rs.getString("telefone"));
        item.setCnh(rs.getString("cnh_numero"));
        item.setCategoriaCnh(rs.getString("cnh_categoria"));
        Date validade = rs.getDate("cnh_validade");
        if (validade != null) item.setValidadeCnh(validade.toLocalDate());
        item.setVinculo(TipoVinculoMotorista.valueOf(rs.getString("tipo_vinculo")));
        item.setStatus(StatusMotorista.valueOf(rs.getString("status")));
        return item;
    }

    private RelatorioVeiculoDTO mapearRelatorioVeiculo(ResultSet rs) throws SQLException {
        RelatorioVeiculoDTO item = new RelatorioVeiculoDTO();
        item.setPlaca(rs.getString("placa"));
        item.setModelo(rs.getString("modelo"));
        item.setTipo(TipoVeiculo.valueOf(rs.getString("tipo")));
        item.setStatus(StatusVeiculo.valueOf(rs.getString("status")));
        item.setCapacidade(rs.getBigDecimal("capacidade_kg"));
        item.setQuilometragem(rs.getLong("quilometragem"));
        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) item.setDataCriacao(dataCriacao.toLocalDateTime());
        return item;
    }
}
