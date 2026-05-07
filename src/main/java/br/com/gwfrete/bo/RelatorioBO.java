package br.com.gwfrete.bo;

import br.com.gwfrete.dao.RelatorioDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.RelatorioFreteDTO;

import java.sql.SQLException;
import java.util.List;

public class RelatorioBO {
    private final RelatorioDAO relatorioDAO;

    public RelatorioBO() {
        this.relatorioDAO = new RelatorioDAO();
    }

    public List<RelatorioFreteDTO> gerarRelatorioFretes() throws CadastroException {
        try {
            return relatorioDAO.listarFretesOperacionais();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível gerar o relatório de fretes.");
        }
    }
}
