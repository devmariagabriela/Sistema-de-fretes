package br.com.gwfrete.bo;

import br.com.gwfrete.dao.KPIExecutivoDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.KPIExecutivoDTO;

import java.sql.SQLException;

public class KPIExecutivoBO {
    private final KPIExecutivoDAO kpiExecutivoDAO;

    public KPIExecutivoBO() {
        this.kpiExecutivoDAO = new KPIExecutivoDAO();
    }

    public KPIExecutivoDTO buscarIndicadores() throws CadastroException {
        try {
            return kpiExecutivoDAO.buscarIndicadores();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível carregar os KPIs executivos.");
        }
    }
}
