package br.com.gwfrete.bo;

import br.com.gwfrete.dao.RelatorioDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.RelatorioClienteDTO;
import br.com.gwfrete.model.RelatorioContratoDTO;
import br.com.gwfrete.model.RelatorioFinanceiroDTO;
import br.com.gwfrete.model.RelatorioFreteDTO;
import br.com.gwfrete.model.RelatorioManutencaoDTO;
import br.com.gwfrete.model.RelatorioMotoristaDTO;
import br.com.gwfrete.model.RelatorioOcorrenciaDTO;
import br.com.gwfrete.model.RelatorioVeiculoDTO;
import br.com.gwfrete.model.StatusContrato;
import br.com.gwfrete.model.StatusFrete;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RelatorioBO {
    private final RelatorioDAO relatorioDAO;

    public RelatorioBO() {
        this.relatorioDAO = new RelatorioDAO();
    }

    public List<RelatorioFreteDTO> gerarRelatorioFretes() throws CadastroException {
        return gerarRelatorioFretes(null, null, null, null, null);
    }

    public List<RelatorioFreteDTO> gerarRelatorioFretes(LocalDate dataInicial, LocalDate dataFinal,
            StatusFrete status, String motorista, String veiculo) throws CadastroException {
        try {
            return relatorioDAO.listarFretesOperacionais(dataInicial, dataFinal, status, motorista, veiculo);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível gerar o relatório de fretes.");
        }
    }

    public List<RelatorioContratoDTO> gerarRelatorioContratos(LocalDate dataInicial, LocalDate dataFinal,
            StatusContrato status, String cliente) throws CadastroException {
        try {
            return relatorioDAO.listarContratosGerenciais(dataInicial, dataFinal, status, cliente);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível gerar o relatório de contratos.");
        }
    }

    public List<RelatorioFinanceiroDTO> gerarRelatorioFinanceiro() throws CadastroException {
        try {
            return relatorioDAO.listarFinanceiro();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível gerar o relatório financeiro.");
        }
    }

    public List<RelatorioManutencaoDTO> gerarRelatorioManutencoes() throws CadastroException {
        try {
            return relatorioDAO.listarManutencoes();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível gerar o relatório de manutenções.");
        }
    }

    public List<RelatorioOcorrenciaDTO> gerarRelatorioOcorrencias() throws CadastroException {
        try {
            return relatorioDAO.listarOcorrencias();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível gerar o relatório de ocorrências.");
        }
    }

    public List<RelatorioClienteDTO> gerarRelatorioClientes() throws CadastroException {
        try {
            return relatorioDAO.listarClientes();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível gerar o relatório de clientes.");
        }
    }

    public List<RelatorioMotoristaDTO> gerarRelatorioMotoristas() throws CadastroException {
        try {
            return relatorioDAO.listarMotoristas();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível gerar o relatório de motoristas.");
        }
    }

    public List<RelatorioVeiculoDTO> gerarRelatorioVeiculos() throws CadastroException {
        try {
            return relatorioDAO.listarVeiculos();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível gerar o relatório de veículos.");
        }
    }
}
