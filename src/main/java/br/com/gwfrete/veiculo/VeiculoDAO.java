package br.com.gwfrete.veiculo;

import br.com.gwfrete.veiculo.StatusVeiculo;
import br.com.gwfrete.veiculo.TipoVeiculo;
import br.com.gwfrete.veiculo.Veiculo;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public void salvar(Veiculo veiculo) throws SQLException {
        String sql = "INSERT INTO veiculo (placa, modelo, marca, ano, capacidade_kg, tipo, status, quilometragem) "
                + "VALUES (?, ?, ?, ?, ?, ?::tipo_veiculo_enum, ?::status_veiculo_enum, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosVeiculo(stmt, veiculo);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    veiculo.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Veiculo> listarTodos() throws SQLException {
        String sql = "SELECT id, placa, modelo, marca, ano, capacidade_kg, tipo, status, quilometragem, data_criacao "
                + "FROM veiculo WHERE status <> 'INATIVO'::status_veiculo_enum ORDER BY placa";

        List<Veiculo> veiculos = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                veiculos.add(mapearVeiculo(rs));
            }
        }

        return veiculos;
    }

    public List<Veiculo> listarComFiltros(String placa, TipoVeiculo tipo, StatusVeiculo status, String modelo)
            throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT id, placa, modelo, marca, ano, capacidade_kg, tipo, status, "
                + "quilometragem, data_criacao FROM veiculo WHERE 1 = 1");
        List<Object> parametros = new ArrayList<>();

        if (textoPreenchido(placa)) {
            sql.append(" AND UPPER(placa) LIKE UPPER(?)");
            parametros.add("%" + placa.trim() + "%");
        }

        if (tipo != null) {
            sql.append(" AND tipo = ?::tipo_veiculo_enum");
            parametros.add(tipo.name());
        }

        if (status != null) {
            sql.append(" AND status = ?::status_veiculo_enum");
            parametros.add(status.name());
        } else {
            sql.append(" AND status <> 'INATIVO'::status_veiculo_enum");
        }

        if (textoPreenchido(modelo)) {
            sql.append(" AND LOWER(modelo) LIKE LOWER(?)");
            parametros.add("%" + modelo.trim() + "%");
        }

        sql.append(" ORDER BY placa");
        return executarConsultaVeiculos(sql.toString(), parametros);
    }

    public Veiculo buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, placa, modelo, marca, ano, capacidade_kg, tipo, status, quilometragem, data_criacao "
                + "FROM veiculo WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearVeiculo(rs);
                }
            }
        }

        return null;
    }

    public Veiculo buscarPorPlaca(String placa) throws SQLException {
        String sql = "SELECT id, placa, modelo, marca, ano, capacidade_kg, tipo, status, quilometragem, data_criacao "
                + "FROM veiculo WHERE UPPER(placa) = UPPER(?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearVeiculo(rs);
                }
            }
        }

        return null;
    }

    public void atualizar(Veiculo veiculo) throws SQLException {
        String sql = "UPDATE veiculo "
                + "SET placa = ?, modelo = ?, marca = ?, ano = ?, capacidade_kg = ?, "
                + "tipo = ?::tipo_veiculo_enum, status = ?::status_veiculo_enum, quilometragem = ? "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametrosVeiculo(stmt, veiculo);
            stmt.setLong(9, veiculo.getId());
            stmt.executeUpdate();
        }
    }

    public void inativar(Long id) throws SQLException {
        String sql = "UPDATE veiculo SET status = 'INATIVO'::status_veiculo_enum WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void ativar(Long id) throws SQLException {
        String sql = "UPDATE veiculo SET status = 'DISPONIVEL'::status_veiculo_enum WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private void preencherParametrosVeiculo(PreparedStatement stmt, Veiculo veiculo) throws SQLException {
        stmt.setString(1, veiculo.getPlaca());
        stmt.setString(2, veiculo.getModelo());
        stmt.setString(3, veiculo.getMarca());
        stmt.setInt(4, veiculo.getAno());
        stmt.setBigDecimal(5, veiculo.getCapacidadeKg());
        stmt.setString(6, veiculo.getTipo().name());
        stmt.setString(7, veiculo.getStatus().name());
        stmt.setLong(8, veiculo.getQuilometragem());
    }

    private List<Veiculo> executarConsultaVeiculos(String sql, List<Object> parametros) throws SQLException {
        List<Veiculo> veiculos = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    veiculos.add(mapearVeiculo(rs));
                }
            }
        }

        return veiculos;
    }

    private boolean textoPreenchido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    private Veiculo mapearVeiculo(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(rs.getLong("id"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setModelo(rs.getString("modelo"));
        veiculo.setMarca(rs.getString("marca"));
        veiculo.setAno(rs.getInt("ano"));
        veiculo.setCapacidadeKg(rs.getBigDecimal("capacidade_kg"));
        veiculo.setTipo(TipoVeiculo.valueOf(rs.getString("tipo")));
        veiculo.setStatus(StatusVeiculo.valueOf(rs.getString("status")));
        veiculo.setQuilometragem(rs.getLong("quilometragem"));

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            veiculo.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        return veiculo;
    }
}
