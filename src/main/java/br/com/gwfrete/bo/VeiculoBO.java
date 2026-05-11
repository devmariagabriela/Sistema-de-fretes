package br.com.gwfrete.bo;

import br.com.gwfrete.dao.VeiculoDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.StatusVeiculo;
import br.com.gwfrete.model.TipoVeiculo;
import br.com.gwfrete.model.Veiculo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Year;
import java.util.List;
import java.util.regex.Pattern;

public class VeiculoBO {
    private static final Pattern PLACA_VALIDA = Pattern.compile("^[A-Z]{3}-?[0-9][A-Z0-9][0-9]{2}$");
    private static final int ANO_MINIMO_OPERACIONAL = 1980;

    private final VeiculoDAO veiculoDAO;

    public VeiculoBO() {
        this.veiculoDAO = new VeiculoDAO();
    }

    public void salvar(Veiculo veiculo) throws CadastroException {
        aplicarPadroesCadastro(veiculo);
        validarCadastro(veiculo);

        try {
            if (veiculoDAO.buscarPorPlaca(veiculo.getPlaca()) != null) {
                throw new CadastroException("Já existe veículo cadastrado com esta placa.");
            }

            veiculoDAO.salvar(veiculo);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível salvar o veículo.");
        }
    }

    public List<Veiculo> listarTodos() throws CadastroException {
        try {
            return veiculoDAO.listarTodos();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os veículos.");
        }
    }

    public List<Veiculo> listarComFiltros(String placa, TipoVeiculo tipo, StatusVeiculo status, String modelo)
            throws CadastroException {
        try {
            return veiculoDAO.listarComFiltros(normalizarFiltro(placa), tipo, status, normalizarFiltro(modelo));
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os veículos.");
        }
    }

    public Veiculo buscarPorId(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Veículo inválido.");
        }

        try {
            return veiculoDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar o veículo.");
        }
    }

    public void atualizar(Veiculo veiculo) throws CadastroException {
        validarIdentificador(veiculo);
        aplicarPadroesAtualizacao(veiculo);
        validarCamposComuns(veiculo);

        try {
            Veiculo veiculoAtual = veiculoDAO.buscarPorId(veiculo.getId());

            if (veiculoAtual == null) {
                throw new CadastroException("Veículo não encontrado.");
            }

            Veiculo veiculoComMesmaPlaca = veiculoDAO.buscarPorPlaca(veiculo.getPlaca());
            if (veiculoComMesmaPlaca != null && !veiculoComMesmaPlaca.getId().equals(veiculo.getId())) {
                throw new CadastroException("Já existe veículo cadastrado com esta placa.");
            }

            validarTransicaoManutencao(veiculoAtual, veiculo);
            veiculoDAO.atualizar(veiculo);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível atualizar o veículo.");
        }
    }

    private void validarCadastro(Veiculo veiculo) throws CadastroException {
        validarCamposComuns(veiculo);
    }

    private void validarIdentificador(Veiculo veiculo) throws CadastroException {
        if (veiculo == null || veiculo.getId() == null || veiculo.getId() <= 0) {
            throw new CadastroException("Veículo inválido.");
        }
    }

    private void validarCamposComuns(Veiculo veiculo) throws CadastroException {
        if (veiculo == null) {
            throw new CadastroException("Veículo inválido.");
        }

        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new CadastroException("Placa é obrigatória.");
        }

        if (!PLACA_VALIDA.matcher(veiculo.getPlaca()).matches()) {
            throw new CadastroException("Placa inválida.");
        }

        if (veiculo.getModelo() == null || veiculo.getModelo().trim().isEmpty()) {
            throw new CadastroException("Modelo é obrigatório.");
        }

        if (veiculo.getMarca() == null || veiculo.getMarca().trim().isEmpty()) {
            throw new CadastroException("Marca é obrigatória.");
        }

        if (veiculo.getAno() == null) {
            throw new CadastroException("Ano é obrigatório.");
        }

        int anoMaximo = Year.now().getValue() + 1;
        if (veiculo.getAno() < ANO_MINIMO_OPERACIONAL || veiculo.getAno() > anoMaximo) {
            throw new CadastroException("Ano do veículo inválido.");
        }

        if (veiculo.getCapacidadeKg() == null) {
            throw new CadastroException("Capacidade é obrigatória.");
        }

        if (veiculo.getCapacidadeKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CadastroException("Capacidade deve ser maior que zero.");
        }

        if (veiculo.getTipo() == null) {
            throw new CadastroException("Tipo do veículo é obrigatório.");
        }

        if (veiculo.getStatus() == null) {
            throw new CadastroException("Status do veículo é obrigatório.");
        }

        if (veiculo.getQuilometragem() == null) {
            veiculo.setQuilometragem(0L);
        }

        if (veiculo.getQuilometragem() < 0) {
            throw new CadastroException("Quilometragem não pode ser negativa.");
        }
    }

    private void aplicarPadroesCadastro(Veiculo veiculo) {
        if (veiculo == null) {
            return;
        }

        prepararDadosVeiculo(veiculo);

        if (veiculo.getStatus() == null) {
            veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        }

        if (veiculo.getQuilometragem() == null) {
            veiculo.setQuilometragem(0L);
        }
    }

    private void aplicarPadroesAtualizacao(Veiculo veiculo) {
        if (veiculo == null) {
            return;
        }

        prepararDadosVeiculo(veiculo);

        if (veiculo.getQuilometragem() == null) {
            veiculo.setQuilometragem(0L);
        }
    }

    private void prepararDadosVeiculo(Veiculo veiculo) {
        if (veiculo.getPlaca() != null) {
            veiculo.setPlaca(veiculo.getPlaca().trim().toUpperCase());
        }

        if (veiculo.getModelo() != null) {
            veiculo.setModelo(veiculo.getModelo().trim());
        }

        if (veiculo.getMarca() != null) {
            veiculo.setMarca(veiculo.getMarca().trim());
        }
    }

    private String normalizarFiltro(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }

    private void validarTransicaoManutencao(Veiculo veiculoAtual, Veiculo veiculoAtualizado) throws CadastroException {
        if (veiculoAtual.getStatus() == StatusVeiculo.MANUTENCAO
                && veiculoAtualizado.getStatus() == StatusVeiculo.DISPONIVEL
                && veiculoAtualizado.getQuilometragem().equals(veiculoAtual.getQuilometragem())) {
            throw new CadastroException("Atualize a quilometragem antes de disponibilizar veículo em manutenção.");
        }

        if (veiculoAtualizado.getQuilometragem() < veiculoAtual.getQuilometragem()) {
            throw new CadastroException("Quilometragem não pode ser menor que a já registrada.");
        }
    }
}
