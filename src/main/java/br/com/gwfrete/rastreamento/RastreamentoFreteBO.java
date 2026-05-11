package br.com.gwfrete.rastreamento;

import br.com.gwfrete.frete.FreteDAO;
import br.com.gwfrete.rastreamento.RastreamentoFreteDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.frete.Frete;
import br.com.gwfrete.rastreamento.RastreamentoFrete;
import br.com.gwfrete.frete.StatusFrete;

import java.sql.SQLException;
import java.util.List;

public class RastreamentoFreteBO {
    private final RastreamentoFreteDAO rastreamentoFreteDAO;
    private final FreteDAO freteDAO;

    public RastreamentoFreteBO() {
        this.rastreamentoFreteDAO = new RastreamentoFreteDAO();
        this.freteDAO = new FreteDAO();
    }

    public void salvar(RastreamentoFrete rastreamento) throws CadastroException {
        prepararDadosRastreamento(rastreamento);
        validarCamposObrigatorios(rastreamento);

        try {
            Frete frete = freteDAO.buscarPorId(rastreamento.getFrete().getId());

            if (frete == null) {
                throw new CadastroException("Frete não encontrado.");
            }

            validarStatusFrete(frete);
            validarOrdemCronologica(rastreamento);
            rastreamento.setFrete(frete);
            rastreamentoFreteDAO.salvar(rastreamento);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível salvar o rastreamento do frete.");
        }
    }

    public List<RastreamentoFrete> listarPorFrete(Long freteId) throws CadastroException {
        validarFreteId(freteId);

        try {
            return rastreamentoFreteDAO.listarPorFrete(freteId);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os rastreamentos do frete.");
        }
    }

    public RastreamentoFrete buscarUltimoPorFrete(Long freteId) throws CadastroException {
        validarFreteId(freteId);

        try {
            return rastreamentoFreteDAO.buscarUltimoPorFrete(freteId);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar o último rastreamento do frete.");
        }
    }

    private void validarCamposObrigatorios(RastreamentoFrete rastreamento) throws CadastroException {
        if (rastreamento == null) {
            throw new CadastroException("Rastreamento inválido.");
        }

        if (rastreamento.getFrete() == null
                || rastreamento.getFrete().getId() == null
                || rastreamento.getFrete().getId() <= 0) {
            throw new CadastroException("Frete é obrigatório.");
        }

        if (rastreamento.getLocalizacao() == null || rastreamento.getLocalizacao().trim().isEmpty()) {
            throw new CadastroException("Localização é obrigatória.");
        }

        if (rastreamento.getDataHora() == null) {
            throw new CadastroException("Data e hora do rastreamento são obrigatórias.");
        }

        if (rastreamento.getLatitude() != null && rastreamento.getLongitude() == null) {
            throw new CadastroException("Longitude é obrigatória quando latitude for informada.");
        }

        if (rastreamento.getLongitude() != null && rastreamento.getLatitude() == null) {
            throw new CadastroException("Latitude é obrigatória quando longitude for informada.");
        }
    }

    private void validarStatusFrete(Frete frete) throws CadastroException {
        if (frete.getStatus() == StatusFrete.CANCELADO) {
            throw new CadastroException("Não é permitido registrar rastreamento em frete cancelado.");
        }

        if (frete.getStatus() == StatusFrete.ENTREGUE) {
            throw new CadastroException("Não é permitido registrar novo rastreamento em frete entregue.");
        }
    }

    private void validarOrdemCronologica(RastreamentoFrete rastreamento) throws SQLException, CadastroException {
        RastreamentoFrete ultimoRastreamento =
                rastreamentoFreteDAO.buscarUltimoPorFrete(rastreamento.getFrete().getId());

        if (ultimoRastreamento != null
                && rastreamento.getDataHora().isBefore(ultimoRastreamento.getDataHora())) {
            throw new CadastroException("Data e hora do rastreamento devem respeitar a ordem cronológica.");
        }
    }

    private void validarFreteId(Long freteId) throws CadastroException {
        if (freteId == null || freteId <= 0) {
            throw new CadastroException("Frete inválido.");
        }
    }

    private void prepararDadosRastreamento(RastreamentoFrete rastreamento) {
        if (rastreamento == null) {
            return;
        }

        rastreamento.setLocalizacao(normalizarTexto(rastreamento.getLocalizacao()));
        rastreamento.setObservacao(normalizarTexto(rastreamento.getObservacao()));
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }
}
