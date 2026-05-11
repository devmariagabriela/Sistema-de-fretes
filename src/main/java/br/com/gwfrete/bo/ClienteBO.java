package br.com.gwfrete.bo;

import br.com.gwfrete.dao.ClienteDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.Cliente;
import br.com.gwfrete.model.TipoCliente;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteBO {
    private static final Pattern SOMENTE_DIGITOS = Pattern.compile("\\D");

    private final ClienteDAO clienteDAO;

    public ClienteBO() {
        this.clienteDAO = new ClienteDAO();
    }

    public void salvar(Cliente cliente) throws CadastroException {
        aplicarPadroesCadastro(cliente);
        validarCadastro(cliente);

        try {
            if (clienteDAO.buscarPorCpfCnpj(cliente.getCpfCnpj()) != null) {
                throw new CadastroException("Já existe cliente cadastrado com este CPF/CNPJ.");
            }

            clienteDAO.salvar(cliente);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível salvar o cliente.");
        }
    }

    public List<Cliente> listarTodos() throws CadastroException {
        try {
            return clienteDAO.listarTodos();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os clientes.");
        }
    }

    public List<Cliente> listarComFiltros(String nome, String cpfCnpj, TipoCliente tipo, String cidade, Boolean status)
            throws CadastroException {
        try {
            return clienteDAO.listarComFiltros(normalizarTexto(nome), normalizarDocumentoFiltro(cpfCnpj), tipo,
                    normalizarTexto(cidade), status);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os clientes.");
        }
    }

    public Cliente buscarPorId(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Cliente inválido.");
        }

        try {
            return clienteDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar o cliente.");
        }
    }

    public void atualizar(Cliente cliente) throws CadastroException {
        validarIdentificador(cliente);
        aplicarPadroesAtualizacao(cliente);
        validarCamposComuns(cliente);

        try {
            Cliente clienteAtual = clienteDAO.buscarPorId(cliente.getId());

            if (clienteAtual == null) {
                throw new CadastroException("Cliente não encontrado.");
            }

            Cliente clienteComMesmoDocumento = clienteDAO.buscarPorCpfCnpj(cliente.getCpfCnpj());
            if (clienteComMesmoDocumento != null && !clienteComMesmoDocumento.getId().equals(cliente.getId())) {
                throw new CadastroException("Já existe cliente cadastrado com este CPF/CNPJ.");
            }

            clienteDAO.atualizar(cliente);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível atualizar o cliente.");
        }
    }

    public void inativar(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Cliente inválido.");
        }

        try {
            if (clienteDAO.buscarPorId(id) == null) {
                throw new CadastroException("Cliente não encontrado.");
            }

            clienteDAO.inativar(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível inativar o cliente.");
        }
    }

    public void ativar(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Cliente inválido.");
        }

        try {
            if (clienteDAO.buscarPorId(id) == null) {
                throw new CadastroException("Cliente não encontrado.");
            }

            clienteDAO.ativar(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível ativar o cliente.");
        }
    }

    private void validarCadastro(Cliente cliente) throws CadastroException {
        validarCamposComuns(cliente);
    }

    private void validarIdentificador(Cliente cliente) throws CadastroException {
        if (cliente == null || cliente.getId() == null || cliente.getId() <= 0) {
            throw new CadastroException("Cliente inválido.");
        }
    }

    private void validarCamposComuns(Cliente cliente) throws CadastroException {
        if (cliente == null) {
            throw new CadastroException("Cliente inválido.");
        }

        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new CadastroException("Nome é obrigatório.");
        }

        if (cliente.getTipo() == null) {
            throw new CadastroException("Tipo de cliente é obrigatório.");
        }

        if (cliente.getCpfCnpj() == null || cliente.getCpfCnpj().trim().isEmpty()) {
            throw new CadastroException("CPF/CNPJ é obrigatório.");
        }
    }

    private void aplicarPadroesCadastro(Cliente cliente) {
        if (cliente == null) {
            return;
        }

        prepararDadosCliente(cliente);

        if (cliente.getStatus() == null) {
            cliente.setStatus(Boolean.TRUE);
        }
    }

    private void aplicarPadroesAtualizacao(Cliente cliente) {
        if (cliente == null) {
            return;
        }

        prepararDadosCliente(cliente);

        if (cliente.getStatus() == null) {
            cliente.setStatus(Boolean.TRUE);
        }
    }

    private void prepararDadosCliente(Cliente cliente) {
        cliente.setNome(normalizarTexto(cliente.getNome()));
        cliente.setEmail(normalizarTexto(cliente.getEmail()));
        cliente.setTelefone(normalizarTexto(cliente.getTelefone()));
        cliente.setContato(normalizarTexto(cliente.getContato()));
        cliente.setEndereco(normalizarTexto(cliente.getEndereco()));
        cliente.setCidade(normalizarTexto(cliente.getCidade()));
        cliente.setEstado(normalizarEstado(cliente.getEstado()));
        cliente.setCep(normalizarTexto(cliente.getCep()));

        if (cliente.getCpfCnpj() != null) {
            cliente.setCpfCnpj(removerCaracteresNaoNumericos(cliente.getCpfCnpj()));
        }
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }

    private String normalizarEstado(String estado) {
        String estadoNormalizado = normalizarTexto(estado);
        return estadoNormalizado == null ? null : estadoNormalizado.toUpperCase();
    }

    private String removerCaracteresNaoNumericos(String valor) {
        return SOMENTE_DIGITOS.matcher(valor.trim()).replaceAll("");
    }

    private String normalizarDocumentoFiltro(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = SOMENTE_DIGITOS.matcher(valor.trim()).replaceAll("");
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }
}
