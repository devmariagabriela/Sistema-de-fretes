package br.com.gwfrete.bo;

import br.com.gwfrete.dao.RelatorioDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.RelatorioFreteDTO;
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
}
