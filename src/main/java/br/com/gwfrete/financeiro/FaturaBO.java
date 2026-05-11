package br.com.gwfrete.financeiro;

import br.com.gwfrete.notificacao.NotificacaoBO;

import br.com.gwfrete.financeiro.FaturaDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.financeiro.Fatura;
import br.com.gwfrete.financeiro.StatusFatura;
import br.com.gwfrete.notificacao.TipoNotificacao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FaturaBO {
    private final FaturaDAO faturaDAO;
    private final NotificacaoBO notificacaoBO;

    public FaturaBO() {
        this.faturaDAO = new FaturaDAO();
        this.notificacaoBO = new NotificacaoBO();
    }

    public void salvar(Fatura fatura) throws CadastroException {
        aplicarPadroesCadastro(fatura);

        try {
            gerarNumeroSeNecessario(fatura);
            validarCadastro(fatura);

            if (faturaDAO.buscarPorNumero(fatura.getNumero()) != null) {
                throw new CadastroException("Já existe fatura cadastrada com este número.");
            }

            faturaDAO.salvar(fatura);
            registrarNotificacaoFatura(fatura);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível salvar a fatura.");
        }
    }

    public List<Fatura> listarTodos() throws CadastroException {
        try {
            return faturaDAO.listarTodos();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar as faturas.");
        }
    }

    public List<Fatura> listarComFiltros(StatusFatura status) throws CadastroException {
        try {
            return faturaDAO.listarComFiltros(status);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar as faturas.");
        }
    }

    public Fatura buscarPorId(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Fatura inválida.");
        }

        try {
            return faturaDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar a fatura.");
        }
    }

    public void atualizar(Fatura fatura) throws CadastroException {
        validarIdentificador(fatura);
        aplicarPadroesAtualizacao(fatura);
        validarCamposComuns(fatura);

        try {
            Fatura faturaAtual = faturaDAO.buscarPorId(fatura.getId());

            if (faturaAtual == null) {
                throw new CadastroException("Fatura não encontrada.");
            }

            Fatura faturaComMesmoNumero = faturaDAO.buscarPorNumero(fatura.getNumero());
            if (faturaComMesmoNumero != null && !faturaComMesmoNumero.getId().equals(fatura.getId())) {
                throw new CadastroException("Já existe fatura cadastrada com este número.");
            }

            validarTransicaoPagamento(faturaAtual, fatura);
            faturaDAO.atualizar(fatura);
            registrarNotificacaoFatura(fatura);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível atualizar a fatura.");
        }
    }

    public void inativar(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Fatura inválida.");
        }

        try {
            if (faturaDAO.buscarPorId(id) == null) {
                throw new CadastroException("Fatura não encontrada.");
            }

            faturaDAO.inativar(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível cancelar a fatura.");
        }
    }

    public void marcarComoPago(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Fatura inválida.");
        }

        try {
            Fatura fatura = faturaDAO.buscarPorId(id);

            if (fatura == null) {
                throw new CadastroException("Fatura não encontrada.");
            }

            if (fatura.getStatus() == StatusFatura.CANCELADO) {
                throw new CadastroException("Fatura cancelada não pode ser marcada como paga.");
            }

            faturaDAO.marcarComoPago(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível marcar a fatura como paga.");
        }
    }

    private void validarCadastro(Fatura fatura) throws CadastroException {
        validarCamposComuns(fatura);
    }

    private void validarIdentificador(Fatura fatura) throws CadastroException {
        if (fatura == null || fatura.getId() == null || fatura.getId() <= 0) {
            throw new CadastroException("Fatura inválida.");
        }
    }

    private void validarCamposComuns(Fatura fatura) throws CadastroException {
        if (fatura == null) {
            throw new CadastroException("Fatura inválida.");
        }

        if (fatura.getFrete() == null || fatura.getFrete().getId() == null || fatura.getFrete().getId() <= 0) {
            throw new CadastroException("Frete é obrigatório.");
        }

        if (fatura.getCliente() == null || fatura.getCliente().getId() == null || fatura.getCliente().getId() <= 0) {
            throw new CadastroException("Cliente é obrigatório.");
        }

        if (fatura.getNumero() == null || fatura.getNumero().trim().isEmpty()) {
            throw new CadastroException("Número da fatura é obrigatório.");
        }

        if (fatura.getValor() == null) {
            throw new CadastroException("Valor da fatura é obrigatório.");
        }

        if (fatura.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CadastroException("Valor da fatura deve ser maior que zero.");
        }

        if (fatura.getDataEmissao() == null) {
            throw new CadastroException("Data de emissão é obrigatória.");
        }

        if (fatura.getDataVencimento() == null) {
            throw new CadastroException("Data de vencimento é obrigatória.");
        }

        if (fatura.getDataEmissao().isAfter(LocalDate.now())) {
            throw new CadastroException("Data de emissão não pode ser futura.");
        }

        if (fatura.getDataVencimento().isBefore(fatura.getDataEmissao())) {
            throw new CadastroException("Data de vencimento não pode ser anterior à data de emissão.");
        }

        if (fatura.getDataPagamento() != null && fatura.getDataPagamento().isBefore(fatura.getDataEmissao())) {
            throw new CadastroException("Data de pagamento não pode ser anterior à data de emissão.");
        }

        if (fatura.getDataPagamento() != null && fatura.getDataPagamento().isAfter(LocalDate.now())) {
            throw new CadastroException("Data de pagamento não pode ser futura.");
        }

        if (fatura.getStatus() == null) {
            throw new CadastroException("Status da fatura é obrigatório.");
        }

        if (fatura.getStatus() == StatusFatura.PAGO && fatura.getDataPagamento() == null) {
            throw new CadastroException("Fatura paga deve possuir data de pagamento.");
        }
    }

    private void validarTransicaoPagamento(Fatura faturaAtual, Fatura faturaAtualizada) throws CadastroException {
        if (faturaAtual.getStatus() == StatusFatura.CANCELADO
                && faturaAtualizada.getStatus() == StatusFatura.PAGO) {
            throw new CadastroException("Fatura cancelada não pode ser marcada como paga.");
        }
    }

    private void aplicarPadroesCadastro(Fatura fatura) {
        if (fatura == null) {
            return;
        }

        prepararDadosFatura(fatura);

        if (fatura.getStatus() == null) {
            fatura.setStatus(StatusFatura.PENDENTE);
        }
    }

    private void aplicarPadroesAtualizacao(Fatura fatura) {
        if (fatura == null) {
            return;
        }

        prepararDadosFatura(fatura);

        if (fatura.getStatus() == null) {
            fatura.setStatus(StatusFatura.PENDENTE);
        }
    }

    private void prepararDadosFatura(Fatura fatura) {
        if (fatura.getNumero() != null) {
            fatura.setNumero(fatura.getNumero().trim().toUpperCase());
        }

        if (fatura.getObservacao() != null) {
            String observacao = fatura.getObservacao().trim();
            fatura.setObservacao(observacao.isEmpty() ? null : observacao);
        }
    }

    private void gerarNumeroSeNecessario(Fatura fatura) throws SQLException, CadastroException {
        if (fatura == null || fatura.getNumero() != null && !fatura.getNumero().trim().isEmpty()) {
            return;
        }

        for (int tentativa = 0; tentativa < 5; tentativa++) {
            String numero = faturaDAO.gerarNumeroAutomatico();
            if (faturaDAO.buscarPorNumero(numero) == null) {
                fatura.setNumero(numero);
                return;
            }
        }

        throw new CadastroException("Não foi possível gerar um número único para a fatura.");
    }

    private void registrarNotificacaoFatura(Fatura fatura) {
        try {
            if (fatura.getStatus() == StatusFatura.PAGO) {
                notificacaoBO.registrarEvento(TipoNotificacao.OUTROS, "Fatura paga",
                        "Fatura " + fatura.getNumero() + " foi marcada como paga.", fatura.getId(), "FATURA");
            } else if (fatura.getStatus() == StatusFatura.VENCIDO) {
                notificacaoBO.registrarEvento(TipoNotificacao.FATURA_VENCIDA, "Fatura vencida",
                        "Fatura " + fatura.getNumero() + " está vencida.", fatura.getId(), "FATURA");
            }

            notificacaoBO.gerarNotificacoesAutomaticas();
        } catch (CadastroException e) {
            // Notificações não devem impedir o fluxo financeiro principal.
        }
    }
}
