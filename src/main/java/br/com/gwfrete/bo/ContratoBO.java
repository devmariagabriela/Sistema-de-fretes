package br.com.gwfrete.bo;

import br.com.gwfrete.dao.ClienteDAO;
import br.com.gwfrete.dao.ContratoDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.Cliente;
import br.com.gwfrete.model.Contrato;
import br.com.gwfrete.model.StatusContrato;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ContratoBO {
    private final ContratoDAO contratoDAO;
    private final ClienteDAO clienteDAO;

    public ContratoBO() {
        this.contratoDAO = new ContratoDAO();
        this.clienteDAO = new ClienteDAO();
    }

    public void salvar(Contrato contrato) throws CadastroException {
        aplicarPadroesCadastro(contrato);
        validarCadastro(contrato);

        try {
            if (contratoDAO.buscarPorNumero(contrato.getNumero()) != null) {
                throw new CadastroException("Já existe contrato cadastrado com este número.");
            }

            carregarCliente(contrato);
            contratoDAO.salvar(contrato);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível salvar o contrato.");
        }
    }

    public List<Contrato> listarTodos() throws CadastroException {
        try {
            return contratoDAO.listarTodos();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os contratos.");
        }
    }

    public Contrato buscarPorId(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Contrato inválido.");
        }

        try {
            return contratoDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar o contrato.");
        }
    }

    public void atualizar(Contrato contrato) throws CadastroException {
        validarIdentificador(contrato);
        aplicarPadroesAtualizacao(contrato);
        validarCamposComuns(contrato);

        try {
            Contrato contratoAtual = contratoDAO.buscarPorId(contrato.getId());

            if (contratoAtual == null) {
                throw new CadastroException("Contrato não encontrado.");
            }

            Contrato contratoComMesmoNumero = contratoDAO.buscarPorNumero(contrato.getNumero());
            if (contratoComMesmoNumero != null && !contratoComMesmoNumero.getId().equals(contrato.getId())) {
                throw new CadastroException("Já existe contrato cadastrado com este número.");
            }

            validarTransicaoStatus(contratoAtual, contrato);
            carregarCliente(contrato);
            contratoDAO.atualizar(contrato);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível atualizar o contrato.");
        }
    }

    private void validarCadastro(Contrato contrato) throws CadastroException {
        validarCamposComuns(contrato);
    }

    private void validarIdentificador(Contrato contrato) throws CadastroException {
        if (contrato == null || contrato.getId() == null || contrato.getId() <= 0) {
            throw new CadastroException("Contrato inválido.");
        }
    }

    private void validarCamposComuns(Contrato contrato) throws CadastroException {
        if (contrato == null) {
            throw new CadastroException("Contrato inválido.");
        }

        if (contrato.getCliente() == null
                || contrato.getCliente().getId() == null
                || contrato.getCliente().getId() <= 0) {
            throw new CadastroException("Cliente é obrigatório.");
        }

        if (contrato.getNumero() == null || contrato.getNumero().trim().isEmpty()) {
            throw new CadastroException("Número do contrato é obrigatório.");
        }

        if (contrato.getValorMensal() == null) {
            throw new CadastroException("Valor mensal é obrigatório.");
        }

        if (contrato.getValorMensal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CadastroException("Valor mensal deve ser maior que zero.");
        }

        if (contrato.getDataInicio() == null) {
            throw new CadastroException("Data de início é obrigatória.");
        }

        if (contrato.getStatus() == null) {
            throw new CadastroException("Status do contrato é obrigatório.");
        }

        if (contrato.getStatus() == StatusContrato.ENCERRADO && contrato.getDataFim() == null) {
            throw new CadastroException("Contrato encerrado deve possuir data de fim.");
        }
    }

    private void validarTransicaoStatus(Contrato contratoAtual, Contrato contratoAtualizado) throws CadastroException {
        if (contratoAtual.getStatus() == StatusContrato.CANCELADO
                && contratoAtualizado.getStatus() == StatusContrato.ATIVO) {
            throw new CadastroException("Contrato cancelado não pode voltar para ativo.");
        }
    }

    private void carregarCliente(Contrato contrato) throws SQLException, CadastroException {
        Cliente cliente = clienteDAO.buscarPorId(contrato.getCliente().getId());

        if (cliente == null) {
            throw new CadastroException("Cliente não encontrado.");
        }

        contrato.setCliente(cliente);
    }

    private void aplicarPadroesCadastro(Contrato contrato) {
        if (contrato == null) {
            return;
        }

        prepararDadosContrato(contrato);

        if (contrato.getStatus() == null) {
            contrato.setStatus(StatusContrato.ATIVO);
        }
    }

    private void aplicarPadroesAtualizacao(Contrato contrato) {
        if (contrato == null) {
            return;
        }

        prepararDadosContrato(contrato);

        if (contrato.getStatus() == null) {
            contrato.setStatus(StatusContrato.ATIVO);
        }
    }

    private void prepararDadosContrato(Contrato contrato) {
        if (contrato.getNumero() != null) {
            contrato.setNumero(contrato.getNumero().trim().toUpperCase());
        }

        contrato.setDescricao(normalizarTexto(contrato.getDescricao()));
        contrato.setObservacao(normalizarTexto(contrato.getObservacao()));
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }
}
