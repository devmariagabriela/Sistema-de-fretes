package br.com.gwfrete.bo;

import br.com.gwfrete.dao.MotoristaDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.Motorista;
import br.com.gwfrete.model.StatusMotorista;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class MotoristaBO {
    private static final Pattern SOMENTE_DIGITOS = Pattern.compile("\\D");

    private final MotoristaDAO motoristaDAO;

    public MotoristaBO() {
        this.motoristaDAO = new MotoristaDAO();
    }

    public void salvar(Motorista motorista) throws CadastroException {
        aplicarPadroesCadastro(motorista);
        validarCadastro(motorista);

        try {
            if (motoristaDAO.buscarPorCpf(motorista.getCpf()) != null) {
                throw new CadastroException("Já existe motorista cadastrado com este CPF.");
            }

            if (motoristaDAO.buscarPorCnhNumero(motorista.getCnhNumero()) != null) {
                throw new CadastroException("Já existe motorista cadastrado com esta CNH.");
            }

            motoristaDAO.salvar(motorista);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível salvar o motorista.");
        }
    }

    public List<Motorista> listarTodos() throws CadastroException {
        try {
            return motoristaDAO.listarTodos();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os motoristas.");
        }
    }

    public Motorista buscarPorId(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Motorista inválido.");
        }

        try {
            return motoristaDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar o motorista.");
        }
    }

    public void atualizar(Motorista motorista) throws CadastroException {
        validarIdentificador(motorista);
        aplicarPadroesAtualizacao(motorista);
        validarCamposComuns(motorista);

        try {
            Motorista motoristaAtual = motoristaDAO.buscarPorId(motorista.getId());

            if (motoristaAtual == null) {
                throw new CadastroException("Motorista não encontrado.");
            }

            Motorista motoristaComMesmoCpf = motoristaDAO.buscarPorCpf(motorista.getCpf());
            if (motoristaComMesmoCpf != null && !motoristaComMesmoCpf.getId().equals(motorista.getId())) {
                throw new CadastroException("Já existe motorista cadastrado com este CPF.");
            }

            Motorista motoristaComMesmaCnh = motoristaDAO.buscarPorCnhNumero(motorista.getCnhNumero());
            if (motoristaComMesmaCnh != null && !motoristaComMesmaCnh.getId().equals(motorista.getId())) {
                throw new CadastroException("Já existe motorista cadastrado com esta CNH.");
            }

            motoristaDAO.atualizar(motorista);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível atualizar o motorista.");
        }
    }

    public boolean motoristaDisponivelParaOperacao(Motorista motorista) {
        return motorista != null && motorista.isDisponivelOperacionalmente();
    }

    private void validarCadastro(Motorista motorista) throws CadastroException {
        validarCamposComuns(motorista);
    }

    private void validarIdentificador(Motorista motorista) throws CadastroException {
        if (motorista == null || motorista.getId() == null || motorista.getId() <= 0) {
            throw new CadastroException("Motorista inválido.");
        }
    }

    private void validarCamposComuns(Motorista motorista) throws CadastroException {
        if (motorista == null) {
            throw new CadastroException("Motorista inválido.");
        }

        if (motorista.getNome() == null || motorista.getNome().trim().isEmpty()) {
            throw new CadastroException("Nome é obrigatório.");
        }

        if (motorista.getCpf() == null || motorista.getCpf().trim().isEmpty()) {
            throw new CadastroException("CPF é obrigatório.");
        }

        if (motorista.getCpf().length() != 11) {
            throw new CadastroException("CPF deve conter 11 dígitos.");
        }

        if (motorista.getDataNascimento() == null) {
            throw new CadastroException("Data de nascimento é obrigatória.");
        }

        if (motorista.getCnhNumero() == null || motorista.getCnhNumero().trim().isEmpty()) {
            throw new CadastroException("Número da CNH é obrigatório.");
        }

        if (motorista.getCnhCategoria() == null) {
            throw new CadastroException("Categoria da CNH é obrigatória.");
        }

        if (motorista.getCnhValidade() == null) {
            throw new CadastroException("Validade da CNH é obrigatória.");
        }

        if (motorista.getTipoVinculo() == null) {
            throw new CadastroException("Tipo de vínculo é obrigatório.");
        }

        if (motorista.getStatus() == null) {
            throw new CadastroException("Status do motorista é obrigatório.");
        }
    }

    private void aplicarPadroesCadastro(Motorista motorista) {
        if (motorista == null) {
            return;
        }

        prepararDadosMotorista(motorista);

        if (motorista.getStatus() == null) {
            motorista.setStatus(StatusMotorista.ATIVO);
        }
    }

    private void aplicarPadroesAtualizacao(Motorista motorista) {
        if (motorista == null) {
            return;
        }

        prepararDadosMotorista(motorista);
    }

    private void prepararDadosMotorista(Motorista motorista) {
        if (motorista.getNome() != null) {
            motorista.setNome(motorista.getNome().trim());
        }

        if (motorista.getCpf() != null) {
            motorista.setCpf(removerCaracteresNaoNumericos(motorista.getCpf()));
        }

        if (motorista.getTelefone() != null) {
            String telefone = motorista.getTelefone().trim();
            motorista.setTelefone(telefone.isEmpty() ? null : telefone);
        }

        if (motorista.getCnhNumero() != null) {
            motorista.setCnhNumero(removerCaracteresNaoNumericos(motorista.getCnhNumero()));
        }
    }

    private String removerCaracteresNaoNumericos(String valor) {
        return SOMENTE_DIGITOS.matcher(valor.trim()).replaceAll("");
    }
}
