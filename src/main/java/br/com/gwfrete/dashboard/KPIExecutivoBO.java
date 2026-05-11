package br.com.gwfrete.dashboard;

import br.com.gwfrete.dashboard.KPIExecutivoDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.dashboard.KPIExecutivoDTO;

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
