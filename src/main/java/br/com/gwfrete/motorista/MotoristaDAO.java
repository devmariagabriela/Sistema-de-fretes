package br.com.gwfrete.motorista;

import br.com.gwfrete.motorista.CategoriaCNH;
import br.com.gwfrete.motorista.Motorista;
import br.com.gwfrete.motorista.StatusMotorista;
import br.com.gwfrete.motorista.TipoVinculoMotorista;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MotoristaDAO {

    public void salvar(Motorista motorista) throws SQLException {
        String sql = "INSERT INTO motorista (nome, cpf, data_nascimento, telefone, cnh_numero, "
                + "cnh_categoria, cnh_validade, tipo_vinculo, status) "
                + "VALUES (?, ?, ?, ?, ?, ?::categoria_cnh_enum, ?, ?::tipo_vinculo_motorista_enum, "
                + "?::status_motorista_enum)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosMotorista(stmt, motorista);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    motorista.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Motorista> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, cnh_numero, cnh_categoria, "
                + "cnh_validade, tipo_vinculo, status, data_criacao "
                + "FROM motorista WHERE status <> 'INATIVO'::status_motorista_enum ORDER BY nome";

        List<Motorista> motoristas = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                motoristas.add(mapearMotorista(rs));
            }
        }

        return motoristas;
    }

    public List<Motorista> listarComFiltros(String nome, String cpf, String cnhNumero, CategoriaCNH categoriaCnh,
            StatusMotorista status) throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT id, nome, cpf, data_nascimento, telefone, cnh_numero, "
                + "cnh_categoria, cnh_validade, tipo_vinculo, status, data_criacao "
                + "FROM motorista WHERE 1 = 1");
        List<Object> parametros = new ArrayList<>();

        if (textoPreenchido(nome)) {
            sql.append(" AND LOWER(nome) LIKE LOWER(?)");
            parametros.add("%" + nome.trim() + "%");
        }

        if (textoPreenchido(cpf)) {
            sql.append(" AND cpf LIKE ?");
            parametros.add("%" + cpf.trim() + "%");
        }

        if (textoPreenchido(cnhNumero)) {
            sql.append(" AND cnh_numero LIKE ?");
            parametros.add("%" + cnhNumero.trim() + "%");
        }

        if (categoriaCnh != null) {
            sql.append(" AND cnh_categoria = ?::categoria_cnh_enum");
            parametros.add(categoriaCnh.name());
        }

        if (status != null) {
            sql.append(" AND status = ?::status_motorista_enum");
            parametros.add(status.name());
        } else {
            sql.append(" AND status <> 'INATIVO'::status_motorista_enum");
        }

        sql.append(" ORDER BY nome");
        return executarConsultaMotoristas(sql.toString(), parametros);
    }

    public Motorista buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, cnh_numero, cnh_categoria, "
                + "cnh_validade, tipo_vinculo, status, data_criacao "
                + "FROM motorista WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMotorista(rs);
                }
            }
        }

        return null;
    }

    public Motorista buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, cnh_numero, cnh_categoria, "
                + "cnh_validade, tipo_vinculo, status, data_criacao "
                + "FROM motorista WHERE cpf = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMotorista(rs);
                }
            }
        }

        return null;
    }

    public Motorista buscarPorCnhNumero(String cnhNumero) throws SQLException {
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, cnh_numero, cnh_categoria, "
                + "cnh_validade, tipo_vinculo, status, data_criacao "
                + "FROM motorista WHERE cnh_numero = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnhNumero);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMotorista(rs);
                }
            }
        }

        return null;
    }

    public void atualizar(Motorista motorista) throws SQLException {
        String sql = "UPDATE motorista "
                + "SET nome = ?, cpf = ?, data_nascimento = ?, telefone = ?, cnh_numero = ?, "
                + "cnh_categoria = ?::categoria_cnh_enum, cnh_validade = ?, "
                + "tipo_vinculo = ?::tipo_vinculo_motorista_enum, status = ?::status_motorista_enum "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametrosMotorista(stmt, motorista);
            stmt.setLong(10, motorista.getId());
            stmt.executeUpdate();
        }
    }

    public void inativar(Long id) throws SQLException {
        String sql = "UPDATE motorista SET status = 'INATIVO'::status_motorista_enum WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void ativar(Long id) throws SQLException {
        String sql = "UPDATE motorista SET status = 'ATIVO'::status_motorista_enum WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private void preencherParametrosMotorista(PreparedStatement stmt, Motorista motorista) throws SQLException {
        stmt.setString(1, motorista.getNome());
        stmt.setString(2, motorista.getCpf());
        stmt.setDate(3, Date.valueOf(motorista.getDataNascimento()));
        stmt.setString(4, motorista.getTelefone());
        stmt.setString(5, motorista.getCnhNumero());
        stmt.setString(6, motorista.getCnhCategoria().name());
        stmt.setDate(7, Date.valueOf(motorista.getCnhValidade()));
        stmt.setString(8, motorista.getTipoVinculo().name());
        stmt.setString(9, motorista.getStatus().name());
    }

    private List<Motorista> executarConsultaMotoristas(String sql, List<Object> parametros) throws SQLException {
        List<Motorista> motoristas = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    motoristas.add(mapearMotorista(rs));
                }
            }
        }

        return motoristas;
    }

    private boolean textoPreenchido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    private Motorista mapearMotorista(ResultSet rs) throws SQLException {
        Motorista motorista = new Motorista();
        motorista.setId(rs.getLong("id"));
        motorista.setNome(rs.getString("nome"));
        motorista.setCpf(rs.getString("cpf"));
        motorista.setTelefone(rs.getString("telefone"));
        motorista.setCnhNumero(rs.getString("cnh_numero"));
        motorista.setCnhCategoria(CategoriaCNH.valueOf(rs.getString("cnh_categoria")));
        motorista.setTipoVinculo(TipoVinculoMotorista.valueOf(rs.getString("tipo_vinculo")));
        motorista.setStatus(StatusMotorista.valueOf(rs.getString("status")));

        Date dataNascimento = rs.getDate("data_nascimento");
        if (dataNascimento != null) {
            motorista.setDataNascimento(dataNascimento.toLocalDate());
        }

        Date cnhValidade = rs.getDate("cnh_validade");
        if (cnhValidade != null) {
            motorista.setCnhValidade(cnhValidade.toLocalDate());
        }

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            motorista.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        return motorista;
    }
}
