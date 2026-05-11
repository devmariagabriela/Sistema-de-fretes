package br.com.gwfrete.dashboard;

import br.com.gwfrete.dashboard.DashboardBO;
import br.com.gwfrete.dashboard.KPIExecutivoBO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.dashboard.DashboardDTO;
import br.com.gwfrete.dashboard.KPIExecutivoDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final DashboardBO dashboardBO = new DashboardBO();
    private final KPIExecutivoBO kpiExecutivoBO = new KPIExecutivoBO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("dashboardDTO", dashboardBO.buscarIndicadores());
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("dashboardDTO", new DashboardDTO());
        }

        try {
            request.setAttribute("kpiExecutivoDTO", kpiExecutivoBO.buscarIndicadores());
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("kpiExecutivoDTO", new KPIExecutivoDTO());
        }

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
