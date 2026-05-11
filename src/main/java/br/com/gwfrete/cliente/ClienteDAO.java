package br.com.gwfrete.cliente;

import br.com.gwfrete.cliente.Cliente;
import br.com.gwfrete.cliente.TipoCliente;
import br.com.gwfrete.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void salvar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, tipo, cpf_cnpj, email, telefone, contato, endereco, cidade, "
                + "estado, cep, status) "
                + "VALUES (?, ?::tipo_cliente_enum, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            preencherParametrosCliente(stmt, cliente);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, tipo, cpf_cnpj, email, telefone, contato, endereco, cidade, estado, cep, "
                + "status, data_criacao "
                + "FROM cliente WHERE status = TRUE ORDER BY nome";

        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }

        return clientes;
    }

    public List<Cliente> listarComFiltros(String nome, String cpfCnpj, TipoCliente tipo, String cidade, Boolean status)
            throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT id, nome, tipo, cpf_cnpj, email, telefone, contato, endereco, "
                + "cidade, estado, cep, status, data_criacao FROM cliente WHERE 1 = 1");
        List<Object> parametros = new ArrayList<>();

        if (textoPreenchido(nome)) {
            sql.append(" AND LOWER(nome) LIKE LOWER(?)");
            parametros.add("%" + nome.trim() + "%");
        }

        if (textoPreenchido(cpfCnpj)) {
            sql.append(" AND cpf_cnpj LIKE ?");
            parametros.add("%" + cpfCnpj.trim() + "%");
        }

        if (tipo != null) {
            sql.append(" AND tipo = ?::tipo_cliente_enum");
            parametros.add(tipo.name());
        }

        if (textoPreenchido(cidade)) {
            sql.append(" AND LOWER(cidade) LIKE LOWER(?)");
            parametros.add("%" + cidade.trim() + "%");
        }

        if (status != null) {
            sql.append(" AND status = ?");
            parametros.add(status);
        } else {
            sql.append(" AND status = TRUE");
        }

        sql.append(" ORDER BY nome");
        return executarConsultaClientes(sql.toString(), parametros);
    }

    public Cliente buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nome, tipo, cpf_cnpj, email, telefone, contato, endereco, cidade, estado, cep, "
                + "status, data_criacao "
                + "FROM cliente WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }

        return null;
    }

    public Cliente buscarPorCpfCnpj(String cpfCnpj) throws SQLException {
        String sql = "SELECT id, nome, tipo, cpf_cnpj, email, telefone, contato, endereco, cidade, estado, cep, "
                + "status, data_criacao "
                + "FROM cliente WHERE cpf_cnpj = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfCnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }

        return null;
    }

    public void atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente "
                + "SET nome = ?, tipo = ?::tipo_cliente_enum, cpf_cnpj = ?, email = ?, telefone = ?, contato = ?, "
                + "endereco = ?, cidade = ?, estado = ?, cep = ?, status = ? "
                + "WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametrosCliente(stmt, cliente);
            stmt.setLong(12, cliente.getId());
            stmt.executeUpdate();
        }
    }

    public void inativar(Long id) throws SQLException {
        String sql = "UPDATE cliente SET status = FALSE WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void ativar(Long id) throws SQLException {
        String sql = "UPDATE cliente SET status = TRUE WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private void preencherParametrosCliente(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getTipo().name());
        stmt.setString(3, cliente.getCpfCnpj());
        stmt.setString(4, cliente.getEmail());
        stmt.setString(5, cliente.getTelefone());
        stmt.setString(6, cliente.getContato());
        stmt.setString(7, cliente.getEndereco());
        stmt.setString(8, cliente.getCidade());
        stmt.setString(9, cliente.getEstado());
        stmt.setString(10, cliente.getCep());
        stmt.setBoolean(11, cliente.getStatus());
    }

    private List<Cliente> executarConsultaClientes(String sql, List<Object> parametros) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearCliente(rs));
                }
            }
        }

        return clientes;
    }

    private boolean textoPreenchido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setTipo(TipoCliente.valueOf(rs.getString("tipo")));
        cliente.setCpfCnpj(rs.getString("cpf_cnpj"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setContato(rs.getString("contato"));
        cliente.setEndereco(rs.getString("endereco"));
        cliente.setCidade(rs.getString("cidade"));
        cliente.setEstado(rs.getString("estado"));
        cliente.setCep(rs.getString("cep"));
        cliente.setStatus(rs.getBoolean("status"));

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            cliente.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        return cliente;
    }
}
