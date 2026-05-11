package br.com.gwfrete.manutencao;

import br.com.gwfrete.notificacao.NotificacaoBO;

import br.com.gwfrete.manutencao.ManutencaoVeiculoDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.manutencao.ManutencaoVeiculo;
import br.com.gwfrete.manutencao.StatusManutencao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ManutencaoVeiculoBO {
    private final ManutencaoVeiculoDAO manutencaoVeiculoDAO;
    private final NotificacaoBO notificacaoBO;

    public ManutencaoVeiculoBO() {
        this.manutencaoVeiculoDAO = new ManutencaoVeiculoDAO();
        this.notificacaoBO = new NotificacaoBO();
    }

    public void salvar(ManutencaoVeiculo manutencao) throws CadastroException {
        aplicarPadroesCadastro(manutencao);
        validarCadastro(manutencao);

        try {
            manutencaoVeiculoDAO.salvar(manutencao);
            gerarNotificacoesAutomaticasSemBloquear();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível salvar a manutenção do veículo.");
        }
    }

    public List<ManutencaoVeiculo> listarTodos() throws CadastroException {
        try {
            return manutencaoVeiculoDAO.listarTodos();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar as manutenções de veículos.");
        }
    }

    public List<ManutencaoVeiculo> listarComFiltros(StatusManutencao status) throws CadastroException {
        try {
            return manutencaoVeiculoDAO.listarComFiltros(status);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar as manutenções de veículos.");
        }
    }

    public ManutencaoVeiculo buscarPorId(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Manutenção inválida.");
        }

        try {
            return manutencaoVeiculoDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar a manutenção do veículo.");
        }
    }

    public void atualizar(ManutencaoVeiculo manutencao) throws CadastroException {
        validarIdentificador(manutencao);
        aplicarPadroesAtualizacao(manutencao);
        validarCamposComuns(manutencao);

        try {
            ManutencaoVeiculo manutencaoAtual = manutencaoVeiculoDAO.buscarPorId(manutencao.getId());

            if (manutencaoAtual == null) {
                throw new CadastroException("Manutenção não encontrada.");
            }

            validarTransicaoStatus(manutencaoAtual, manutencao);
            manutencaoVeiculoDAO.atualizar(manutencao);
            gerarNotificacoesAutomaticasSemBloquear();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível atualizar a manutenção do veículo.");
        }
    }

    public void inativar(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Manutenção inválida.");
        }

        try {
            if (manutencaoVeiculoDAO.buscarPorId(id) == null) {
                throw new CadastroException("Manutenção não encontrada.");
            }

            manutencaoVeiculoDAO.inativar(id);
            gerarNotificacoesAutomaticasSemBloquear();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível cancelar a manutenção.");
        }
    }

    public List<ManutencaoVeiculo> listarPorVeiculo(Long veiculoId) throws CadastroException {
        if (veiculoId == null || veiculoId <= 0) {
            throw new CadastroException("Veículo inválido.");
        }

        try {
            return manutencaoVeiculoDAO.listarPorVeiculo(veiculoId);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar as manutenções do veículo.");
        }
    }

    private void validarCadastro(ManutencaoVeiculo manutencao) throws CadastroException {
        validarCamposComuns(manutencao);
    }

    private void validarIdentificador(ManutencaoVeiculo manutencao) throws CadastroException {
        if (manutencao == null || manutencao.getId() == null || manutencao.getId() <= 0) {
            throw new CadastroException("Manutenção inválida.");
        }
    }

    private void validarCamposComuns(ManutencaoVeiculo manutencao) throws CadastroException {
        if (manutencao == null) {
            throw new CadastroException("Manutenção inválida.");
        }

        if (manutencao.getVeiculo() == null
                || manutencao.getVeiculo().getId() == null
                || manutencao.getVeiculo().getId() <= 0) {
            throw new CadastroException("Veículo é obrigatório.");
        }

        if (manutencao.getTipo() == null) {
            throw new CadastroException("Tipo de manutenção é obrigatório.");
        }

        if (manutencao.getStatus() == null) {
            throw new CadastroException("Status da manutenção é obrigatório.");
        }

        if (manutencao.getDescricao() == null || manutencao.getDescricao().trim().isEmpty()) {
            throw new CadastroException("Descrição da manutenção é obrigatória.");
        }

        if (manutencao.getDataAgendada() == null) {
            throw new CadastroException("Data agendada é obrigatória.");
        }

        if (manutencao.getCusto() != null && manutencao.getCusto().compareTo(BigDecimal.ZERO) < 0) {
            throw new CadastroException("Custo não pode ser negativo.");
        }

        if (manutencao.getQuilometragem() != null && manutencao.getQuilometragem() < 0) {
            throw new CadastroException("Quilometragem não pode ser negativa.");
        }

        if (manutencao.getStatus() == StatusManutencao.CONCLUIDA && manutencao.getDataConclusao() == null) {
            throw new CadastroException("Manutenção concluída deve possuir data de conclusão.");
        }

        if (manutencao.getDataInicio() != null
                && manutencao.getDataInicio().isBefore(manutencao.getDataAgendada())) {
            throw new CadastroException("Data de início não pode ser anterior à data agendada.");
        }

        if (manutencao.getDataConclusao() != null && manutencao.getDataInicio() != null
                && manutencao.getDataConclusao().isBefore(manutencao.getDataInicio())) {
            throw new CadastroException("Data de conclusão não pode ser anterior à data de início.");
        }

        if (manutencao.getDataConclusao() != null && manutencao.getDataInicio() == null
                && manutencao.getDataConclusao().isBefore(manutencao.getDataAgendada())) {
            throw new CadastroException("Data de conclusão não pode ser anterior à data agendada.");
        }
    }

    private void validarTransicaoStatus(ManutencaoVeiculo manutencaoAtual, ManutencaoVeiculo manutencaoAtualizada)
            throws CadastroException {

        if (manutencaoAtual.getStatus() == StatusManutencao.CANCELADA
                && manutencaoAtualizada.getStatus() == StatusManutencao.EM_ANDAMENTO) {
            throw new CadastroException("Manutenção cancelada não pode voltar para em andamento.");
        }
    }

    private void aplicarPadroesCadastro(ManutencaoVeiculo manutencao) {
        if (manutencao == null) {
            return;
        }

        prepararDadosManutencao(manutencao);

        if (manutencao.getStatus() == null) {
            manutencao.setStatus(StatusManutencao.AGENDADA);
        }
    }

    private void aplicarPadroesAtualizacao(ManutencaoVeiculo manutencao) {
        if (manutencao == null) {
            return;
        }

        prepararDadosManutencao(manutencao);

        if (manutencao.getStatus() == null) {
            manutencao.setStatus(StatusManutencao.AGENDADA);
        }
    }

    private void prepararDadosManutencao(ManutencaoVeiculo manutencao) {
        manutencao.setDescricao(normalizarTexto(manutencao.getDescricao()));
        manutencao.setOficina(normalizarTexto(manutencao.getOficina()));
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }

    private void gerarNotificacoesAutomaticasSemBloquear() {
        try {
            notificacaoBO.gerarNotificacoesAutomaticas();
        } catch (CadastroException e) {
            // Notificações não devem impedir o fluxo de manutenção.
        }
    }
}
