package br.com.gwfrete.bo;

import br.com.gwfrete.dao.FreteDAO;
import br.com.gwfrete.dao.MotoristaDAO;
import br.com.gwfrete.dao.VeiculoDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.Frete;
import br.com.gwfrete.model.Motorista;
import br.com.gwfrete.model.StatusFrete;
import br.com.gwfrete.model.StatusMotorista;
import br.com.gwfrete.model.StatusVeiculo;
import br.com.gwfrete.model.Veiculo;

import java.sql.SQLException;
import java.util.List;

public class FreteBO {
    private final FreteDAO freteDAO;
    private final MotoristaDAO motoristaDAO;
    private final VeiculoDAO veiculoDAO;

    public FreteBO() {
        this.freteDAO = new FreteDAO();
        this.motoristaDAO = new MotoristaDAO();
        this.veiculoDAO = new VeiculoDAO();
    }

    public void salvar(Frete frete) throws CadastroException {
        aplicarPadroesCadastro(frete);
        validarCadastro(frete);

        try {
            if (freteDAO.buscarPorCodigo(frete.getCodigo()) != null) {
                throw new CadastroException("Já existe frete cadastrado com este código.");
            }

            validarRecursosOperacionais(frete);
            freteDAO.salvar(frete);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível salvar o frete.");
        }
    }

    public List<Frete> listarTodos() throws CadastroException {
        try {
            return freteDAO.listarTodos();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os fretes.");
        }
    }

    public List<Frete> listarComFiltros(String codigo, String origem, String destino, String motorista,
            String veiculo, StatusFrete status) throws CadastroException {
        try {
            return freteDAO.listarComFiltros(normalizarFiltro(codigo), normalizarFiltro(origem),
                    normalizarFiltro(destino), normalizarFiltro(motorista), normalizarFiltro(veiculo), status);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os fretes.");
        }
    }

    public Frete buscarPorId(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Frete inválido.");
        }

        try {
            return freteDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar o frete.");
        }
    }

    public void atualizar(Frete frete) throws CadastroException {
        validarIdentificador(frete);
        aplicarPadroesAtualizacao(frete);
        validarCamposComuns(frete);

        try {
            Frete freteAtual = freteDAO.buscarPorId(frete.getId());

            if (freteAtual == null) {
                throw new CadastroException("Frete não encontrado.");
            }

            validarAlteracaoStatus(freteAtual, frete);

            Frete freteComMesmoCodigo = freteDAO.buscarPorCodigo(frete.getCodigo());
            if (freteComMesmoCodigo != null && !freteComMesmoCodigo.getId().equals(frete.getId())) {
                throw new CadastroException("Já existe frete cadastrado com este código.");
            }

            validarRecursosOperacionais(frete);
            freteDAO.atualizar(frete);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível atualizar o frete.");
        }
    }

    private void validarCadastro(Frete frete) throws CadastroException {
        validarCamposComuns(frete);
    }

    private void validarIdentificador(Frete frete) throws CadastroException {
        if (frete == null || frete.getId() == null || frete.getId() <= 0) {
            throw new CadastroException("Frete inválido.");
        }
    }

    private void validarCamposComuns(Frete frete) throws CadastroException {
        if (frete == null) {
            throw new CadastroException("Frete inválido.");
        }

        if (frete.getCodigo() == null || frete.getCodigo().trim().isEmpty()) {
            throw new CadastroException("Código do frete é obrigatório.");
        }

        if (frete.getOrigem() == null || frete.getOrigem().trim().isEmpty()) {
            throw new CadastroException("Origem é obrigatória.");
        }

        if (frete.getDestino() == null || frete.getDestino().trim().isEmpty()) {
            throw new CadastroException("Destino é obrigatório.");
        }

        if (frete.getMotorista() == null || frete.getMotorista().getId() == null || frete.getMotorista().getId() <= 0) {
            throw new CadastroException("Motorista é obrigatório.");
        }

        if (frete.getVeiculo() == null || frete.getVeiculo().getId() == null || frete.getVeiculo().getId() <= 0) {
            throw new CadastroException("Veículo é obrigatório.");
        }

        if (frete.getStatus() == null) {
            throw new CadastroException("Status do frete é obrigatório.");
        }
    }

    private void aplicarPadroesCadastro(Frete frete) {
        if (frete == null) {
            return;
        }

        prepararDadosFrete(frete);

        if (frete.getStatus() == null) {
            frete.setStatus(StatusFrete.AGENDADO);
        }
    }

    private void aplicarPadroesAtualizacao(Frete frete) {
        if (frete == null) {
            return;
        }

        prepararDadosFrete(frete);
    }

    private void prepararDadosFrete(Frete frete) {
        if (frete.getCodigo() != null) {
            frete.setCodigo(frete.getCodigo().trim().toUpperCase());
        }

        if (frete.getOrigem() != null) {
            frete.setOrigem(frete.getOrigem().trim());
        }

        if (frete.getDestino() != null) {
            frete.setDestino(frete.getDestino().trim());
        }

        if (frete.getDescricaoCarga() != null) {
            String descricaoCarga = frete.getDescricaoCarga().trim();
            frete.setDescricaoCarga(descricaoCarga.isEmpty() ? null : descricaoCarga);
        }
    }

    private String normalizarFiltro(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }

    private void validarRecursosOperacionais(Frete frete) throws SQLException, CadastroException {
        Motorista motorista = motoristaDAO.buscarPorId(frete.getMotorista().getId());
        if (motorista == null) {
            throw new CadastroException("Motorista não encontrado.");
        }

        if (motorista.getStatus() == StatusMotorista.INATIVO || motorista.getStatus() == StatusMotorista.SUSPENSO) {
            throw new CadastroException("Motorista inativo ou suspenso não pode ser utilizado em fretes.");
        }

        Veiculo veiculo = veiculoDAO.buscarPorId(frete.getVeiculo().getId());
        if (veiculo == null) {
            throw new CadastroException("Veículo não encontrado.");
        }

        if (veiculo.getStatus() == StatusVeiculo.INATIVO || veiculo.getStatus() == StatusVeiculo.MANUTENCAO) {
            throw new CadastroException("Veículo inativo ou em manutenção não pode ser utilizado em fretes.");
        }

        frete.setMotorista(motorista);
        frete.setVeiculo(veiculo);
    }

    private void validarAlteracaoStatus(Frete freteAtual, Frete freteAtualizado) throws CadastroException {
        if (freteAtual.getStatus() == StatusFrete.CANCELADO) {
            throw new CadastroException("Frete cancelado não pode ser alterado.");
        }

        if (freteAtual.getStatus() == StatusFrete.ENTREGUE
                && freteAtualizado.getStatus() == StatusFrete.EM_TRANSITO) {
            throw new CadastroException("Frete entregue não pode voltar para em trânsito.");
        }
    }
}
