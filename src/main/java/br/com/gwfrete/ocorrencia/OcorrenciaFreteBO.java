package br.com.gwfrete.ocorrencia;

import br.com.gwfrete.notificacao.NotificacaoBO;

import br.com.gwfrete.frete.FreteDAO;
import br.com.gwfrete.ocorrencia.OcorrenciaFreteDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.frete.Frete;
import br.com.gwfrete.ocorrencia.OcorrenciaFrete;
import br.com.gwfrete.frete.StatusFrete;
import br.com.gwfrete.notificacao.TipoNotificacao;
import br.com.gwfrete.ocorrencia.TipoOcorrenciaFrete;

import java.sql.SQLException;
import java.util.List;

public class OcorrenciaFreteBO {
    private final OcorrenciaFreteDAO ocorrenciaFreteDAO;
    private final FreteDAO freteDAO;
    private final NotificacaoBO notificacaoBO;

    public OcorrenciaFreteBO() {
        this.ocorrenciaFreteDAO = new OcorrenciaFreteDAO();
        this.freteDAO = new FreteDAO();
        this.notificacaoBO = new NotificacaoBO();
    }

    public void salvar(OcorrenciaFrete ocorrencia) throws CadastroException {
        prepararDadosOcorrencia(ocorrencia);
        validarCamposObrigatorios(ocorrencia);
        validarCamposPorTipo(ocorrencia);

        try {
            Frete frete = freteDAO.buscarPorId(ocorrencia.getFrete().getId());

            if (frete == null) {
                throw new CadastroException("Frete não encontrado.");
            }

            validarSituacaoFrete(frete);
            ocorrencia.setFrete(frete);

            ocorrenciaFreteDAO.salvar(ocorrencia);
            atualizarStatusFretePorOcorrencia(frete, ocorrencia.getTipo());
            registrarNotificacaoOcorrencia(ocorrencia);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível registrar a ocorrência do frete.");
        }
    }

    public List<OcorrenciaFrete> listarPorFrete(Long freteId) throws CadastroException {
        if (freteId == null || freteId <= 0) {
            throw new CadastroException("Frete inválido.");
        }

        try {
            return ocorrenciaFreteDAO.listarPorFrete(freteId);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar as ocorrências do frete.");
        }
    }

    public OcorrenciaFrete buscarPorId(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Ocorrência inválida.");
        }

        try {
            return ocorrenciaFreteDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar a ocorrência.");
        }
    }

    private void validarCamposObrigatorios(OcorrenciaFrete ocorrencia) throws CadastroException {
        if (ocorrencia == null) {
            throw new CadastroException("Ocorrência inválida.");
        }

        if (ocorrencia.getFrete() == null || ocorrencia.getFrete().getId() == null
                || ocorrencia.getFrete().getId() <= 0) {
            throw new CadastroException("Frete é obrigatório.");
        }

        if (ocorrencia.getTipo() == null) {
            throw new CadastroException("Tipo da ocorrência é obrigatório.");
        }

        if (ocorrencia.getDataHora() == null) {
            throw new CadastroException("Data e hora da ocorrência são obrigatórias.");
        }

        if (ocorrencia.getLocalizacao() == null || ocorrencia.getLocalizacao().trim().isEmpty()) {
            throw new CadastroException("Localização é obrigatória.");
        }
    }

    private void validarCamposPorTipo(OcorrenciaFrete ocorrencia) throws CadastroException {
        if (ocorrencia.getTipo() == TipoOcorrenciaFrete.ENTREGA_REALIZADA) {
            if (ocorrencia.getNomeRecebedor() == null || ocorrencia.getNomeRecebedor().trim().isEmpty()) {
                throw new CadastroException("Nome do recebedor é obrigatório para entrega realizada.");
            }

            if (ocorrencia.getDocumentoRecebedor() == null
                    || ocorrencia.getDocumentoRecebedor().trim().isEmpty()) {
                throw new CadastroException("Documento do recebedor é obrigatório para entrega realizada.");
            }
        }

        if ((ocorrencia.getTipo() == TipoOcorrenciaFrete.AVARIA
                || ocorrencia.getTipo() == TipoOcorrenciaFrete.EXTRAVIO
                || ocorrencia.getTipo() == TipoOcorrenciaFrete.OUTROS)
                && (ocorrencia.getDescricao() == null || ocorrencia.getDescricao().trim().isEmpty())) {
            throw new CadastroException("Descrição é obrigatória para este tipo de ocorrência.");
        }
    }

    private void validarSituacaoFrete(Frete frete) throws CadastroException {
        if (frete.getStatus() == StatusFrete.CANCELADO) {
            throw new CadastroException("Não é permitido registrar ocorrência em frete cancelado.");
        }

        if (frete.getStatus() == StatusFrete.ENTREGUE) {
            throw new CadastroException("Não é permitido registrar ocorrência em frete entregue.");
        }
    }

    private void atualizarStatusFretePorOcorrencia(Frete frete, TipoOcorrenciaFrete tipo) throws SQLException {
        if (tipo == TipoOcorrenciaFrete.ENTREGA_REALIZADA) {
            frete.setStatus(StatusFrete.ENTREGUE);
            freteDAO.atualizar(frete);
            return;
        }

        if (tipo == TipoOcorrenciaFrete.EM_ROTA && frete.getStatus() != StatusFrete.EM_TRANSITO) {
            frete.setStatus(StatusFrete.EM_TRANSITO);
            freteDAO.atualizar(frete);
        }
    }

    private void prepararDadosOcorrencia(OcorrenciaFrete ocorrencia) {
        if (ocorrencia == null) {
            return;
        }

        if (ocorrencia.getLocalizacao() != null) {
            ocorrencia.setLocalizacao(ocorrencia.getLocalizacao().trim());
        }

        if (ocorrencia.getDescricao() != null) {
            String descricao = ocorrencia.getDescricao().trim();
            ocorrencia.setDescricao(descricao.isEmpty() ? null : descricao);
        }

        if (ocorrencia.getNomeRecebedor() != null) {
            String nomeRecebedor = ocorrencia.getNomeRecebedor().trim();
            ocorrencia.setNomeRecebedor(nomeRecebedor.isEmpty() ? null : nomeRecebedor);
        }

        if (ocorrencia.getDocumentoRecebedor() != null) {
            String documentoRecebedor = ocorrencia.getDocumentoRecebedor().trim();
            ocorrencia.setDocumentoRecebedor(documentoRecebedor.isEmpty() ? null : documentoRecebedor);
        }
    }

    private void registrarNotificacaoOcorrencia(OcorrenciaFrete ocorrencia) {
        try {
            notificacaoBO.registrarEvento(TipoNotificacao.OCORRENCIA_CRITICA, "Nova ocorrência cadastrada",
                    "Nova ocorrência " + ocorrencia.getTipo().getDescricao() + " registrada para o frete "
                            + ocorrencia.getFrete().getCodigo() + ".",
                    ocorrencia.getId(), "OCORRENCIA_FRETE");

            if (ocorrencia.getTipo() == TipoOcorrenciaFrete.ENTREGA_REALIZADA) {
                notificacaoBO.registrarEvento(TipoNotificacao.OUTROS, "Frete entregue",
                        "Frete " + ocorrencia.getFrete().getCodigo() + " foi entregue.",
                        ocorrencia.getFrete().getId(), "FRETE");
            }
        } catch (CadastroException e) {
            // Notificações não devem impedir o registro da ocorrência.
        }
    }
}
