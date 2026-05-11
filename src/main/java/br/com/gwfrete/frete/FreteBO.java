package br.com.gwfrete.frete;

import br.com.gwfrete.cliente.Cliente;
import br.com.gwfrete.cliente.ClienteDAO;
import br.com.gwfrete.notificacao.NotificacaoBO;

import br.com.gwfrete.frete.FreteDAO;
import br.com.gwfrete.motorista.MotoristaDAO;
import br.com.gwfrete.veiculo.VeiculoDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.frete.Frete;
import br.com.gwfrete.motorista.Motorista;
import br.com.gwfrete.frete.StatusFrete;
import br.com.gwfrete.motorista.StatusMotorista;
import br.com.gwfrete.veiculo.StatusVeiculo;
import br.com.gwfrete.notificacao.TipoNotificacao;
import br.com.gwfrete.veiculo.Veiculo;

import java.sql.SQLException;
import java.util.List;

public class FreteBO {
    private final FreteDAO freteDAO;
    private final ClienteDAO clienteDAO;
    private final MotoristaDAO motoristaDAO;
    private final VeiculoDAO veiculoDAO;
    private final NotificacaoBO notificacaoBO;

    public FreteBO() {
        this.freteDAO = new FreteDAO();
        this.clienteDAO = new ClienteDAO();
        this.motoristaDAO = new MotoristaDAO();
        this.veiculoDAO = new VeiculoDAO();
        this.notificacaoBO = new NotificacaoBO();
    }

    public void salvar(Frete frete) throws CadastroException {
        aplicarPadroesCadastro(frete);

        try {
            if (frete.getCodigo() == null || frete.getCodigo().trim().isEmpty()) {
                frete.setCodigo(gerarProximoCodigoComTratamento());
            }

            validarCadastro(frete);

            if (freteDAO.buscarPorCodigo(frete.getCodigo()) != null) {
                throw new CadastroException("Já existe frete cadastrado com este código.");
            }

            validarRecursosOperacionais(frete);
            freteDAO.salvar(frete);
            registrarNotificacaoFrete(frete);
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

    public String gerarProximoCodigo() throws CadastroException {
        try {
            return gerarProximoCodigoComTratamento();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível gerar o próximo código do frete.");
        }
    }

    public void atualizar(Frete frete) throws CadastroException {
        validarIdentificador(frete);
        aplicarPadroesAtualizacao(frete);

        try {
            Frete freteAtual = freteDAO.buscarPorId(frete.getId());

            if (freteAtual == null) {
                throw new CadastroException("Frete não encontrado.");
            }

            frete.setCodigo(freteAtual.getCodigo());
            validarCamposComuns(frete);

            validarAlteracaoStatus(freteAtual, frete);

            Frete freteComMesmoCodigo = freteDAO.buscarPorCodigo(frete.getCodigo());
            if (freteComMesmoCodigo != null && !freteComMesmoCodigo.getId().equals(frete.getId())) {
                throw new CadastroException("Já existe frete cadastrado com este código.");
            }

            validarRecursosOperacionais(frete);
            freteDAO.atualizar(frete);
            registrarNotificacaoFrete(frete);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível atualizar o frete.");
        }
    }

    public void inativar(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Frete inválido.");
        }

        try {
            Frete frete = freteDAO.buscarPorId(id);

            if (frete == null) {
                throw new CadastroException("Frete não encontrado.");
            }

            if (frete.getStatus() == StatusFrete.CANCELADO) {
                throw new CadastroException("Frete já está cancelado.");
            }

            if (frete.getStatus() == StatusFrete.ENTREGUE) {
                throw new CadastroException("Frete entregue não pode ser cancelado.");
            }

            freteDAO.inativar(id);
            frete.setStatus(StatusFrete.CANCELADO);
            registrarNotificacaoFrete(frete);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível cancelar o frete.");
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

        if (frete.getRemetente() == null
                || frete.getRemetente().getId() == null
                || frete.getRemetente().getId() <= 0) {
            throw new CadastroException("Remetente é obrigatório.");
        }

        if (frete.getDestinatario() == null
                || frete.getDestinatario().getId() == null
                || frete.getDestinatario().getId() <= 0) {
            throw new CadastroException("Destinatário é obrigatório.");
        }

        if (frete.getRemetente().getId().equals(frete.getDestinatario().getId())) {
            throw new CadastroException("Remetente e destinatário devem ser diferentes.");
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

        if (frete.getDataSaida() != null && frete.getDataEntrega() != null
                && frete.getDataEntrega().isBefore(frete.getDataSaida())) {
            throw new CadastroException("Data de entrega não pode ser anterior à data de saída.");
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

    private String gerarProximoCodigoComTratamento() throws SQLException {
        String ultimoCodigo = freteDAO.buscarUltimoCodigoSequencial();
        int proximoNumero = 1;

        if (ultimoCodigo != null && ultimoCodigo.startsWith("FRT-")) {
            try {
                proximoNumero = Integer.parseInt(ultimoCodigo.substring(4)) + 1;
            } catch (NumberFormatException e) {
                proximoNumero = 1;
            }
        }

        return String.format("FRT-%03d", proximoNumero);
    }

    private void validarRecursosOperacionais(Frete frete) throws SQLException, CadastroException {
        Cliente remetente = clienteDAO.buscarPorId(frete.getRemetente().getId());
        if (remetente == null) {
            throw new CadastroException("Remetente não encontrado.");
        }

        if (!remetente.isAtivo()) {
            throw new CadastroException("Remetente inativo não pode ser utilizado em fretes.");
        }

        Cliente destinatario = clienteDAO.buscarPorId(frete.getDestinatario().getId());
        if (destinatario == null) {
            throw new CadastroException("Destinatário não encontrado.");
        }

        if (!destinatario.isAtivo()) {
            throw new CadastroException("Destinatário inativo não pode ser utilizado em fretes.");
        }

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

        frete.setRemetente(remetente);
        frete.setDestinatario(destinatario);
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

    private void registrarNotificacaoFrete(Frete frete) {
        try {
            if (frete.getStatus() == StatusFrete.CANCELADO) {
                notificacaoBO.registrarEvento(TipoNotificacao.FRETE_CANCELADO, "Frete cancelado",
                        "Frete " + frete.getCodigo() + " foi cancelado.", frete.getId(), "FRETE");
            } else if (frete.getStatus() == StatusFrete.ENTREGUE) {
                notificacaoBO.registrarEvento(TipoNotificacao.OUTROS, "Frete entregue",
                        "Frete " + frete.getCodigo() + " foi entregue.", frete.getId(), "FRETE");
            }

            notificacaoBO.gerarNotificacoesAutomaticas();
        } catch (CadastroException e) {
            // Notificações não devem impedir o fluxo operacional principal.
        }
    }
}
