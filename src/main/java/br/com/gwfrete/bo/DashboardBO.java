package br.com.gwfrete.bo;

import br.com.gwfrete.dao.DashboardDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.DashboardDTO;

import java.sql.SQLException;

public class DashboardBO {
    private final DashboardDAO dashboardDAO;

    public DashboardBO() {
        this.dashboardDAO = new DashboardDAO();
    }

    public DashboardDTO buscarIndicadores() throws CadastroException {
        try {
            return dashboardDAO.buscarIndicadores();
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível carregar os indicadores do dashboard.");
        }
    }
}
