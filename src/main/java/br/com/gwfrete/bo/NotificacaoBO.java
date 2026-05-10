package br.com.gwfrete.bo;

import br.com.gwfrete.dao.NotificacaoDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.Notificacao;
import br.com.gwfrete.model.StatusNotificacao;

import java.sql.SQLException;
import java.util.List;

public class NotificacaoBO {
    private final NotificacaoDAO notificacaoDAO;

    public NotificacaoBO() {
        this.notificacaoDAO = new NotificacaoDAO();
    }

    public void salvar(Notificacao notificacao) throws CadastroException {
        aplicarPadroesCadastro(notificacao);
        validarCamposObrigatorios(notificacao);

        try {
            notificacaoDAO.salvar(notificacao);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível salvar a notificação.");
        }
    }

    public List<Notificacao> listarTodas() throws CadastroException {
        try {
            return notificacaoDAO.listarTodas();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar as notificações.");
        }
    }

    public List<Notificacao> listarNaoLidas() throws CadastroException {
        try {
            return notificacaoDAO.listarNaoLidas();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar as notificações não lidas.");
        }
    }

    public Notificacao buscarPorId(Long id) throws CadastroException {
        validarId(id);

        try {
            return notificacaoDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar a notificação.");
        }
    }

    public void marcarComoLida(Long id) throws CadastroException {
        validarId(id);

        try {
            Notificacao notificacao = notificacaoDAO.buscarPorId(id);

            if (notificacao == null) {
                throw new CadastroException("Notificação não encontrada.");
            }

            notificacaoDAO.marcarComoLida(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível marcar a notificação como lida.");
        }
    }

    public void arquivar(Long id) throws CadastroException {
        validarId(id);

        try {
            Notificacao notificacao = notificacaoDAO.buscarPorId(id);

            if (notificacao == null) {
                throw new CadastroException("Notificação não encontrada.");
            }

            notificacaoDAO.arquivar(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível arquivar a notificação.");
        }
    }

    private void validarCamposObrigatorios(Notificacao notificacao) throws CadastroException {
        if (notificacao == null) {
            throw new CadastroException("Notificação inválida.");
        }

        if (notificacao.getTipo() == null) {
            throw new CadastroException("Tipo da notificação é obrigatório.");
        }

        if (notificacao.getTitulo() == null || notificacao.getTitulo().trim().isEmpty()) {
            throw new CadastroException("Título da notificação é obrigatório.");
        }

        if (notificacao.getMensagem() == null || notificacao.getMensagem().trim().isEmpty()) {
            throw new CadastroException("Mensagem da notificação é obrigatória.");
        }

        if (notificacao.getStatus() == null) {
            throw new CadastroException("Status da notificação é obrigatório.");
        }
    }

    private void validarId(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Notificação inválida.");
        }
    }

    private void aplicarPadroesCadastro(Notificacao notificacao) {
        if (notificacao == null) {
            return;
        }

        prepararDadosNotificacao(notificacao);

        if (notificacao.getStatus() == null) {
            notificacao.setStatus(StatusNotificacao.NAO_LIDA);
        }
    }

    private void prepararDadosNotificacao(Notificacao notificacao) {
        notificacao.setTitulo(normalizarTexto(notificacao.getTitulo()));
        notificacao.setMensagem(normalizarTexto(notificacao.getMensagem()));
        notificacao.setReferenciaTipo(normalizarTexto(notificacao.getReferenciaTipo()));
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }
}
