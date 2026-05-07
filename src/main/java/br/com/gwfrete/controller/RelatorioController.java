package br.com.gwfrete.controller;

import br.com.gwfrete.bo.RelatorioBO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.RelatorioFreteDTO;
import br.com.gwfrete.model.StatusFrete;
import br.com.gwfrete.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RelatorioController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_RELATORIO_FRETES = "/WEB-INF/views/relatorios/listaRelatorioFretes.jsp";

    private final RelatorioBO relatorioBO = new RelatorioBO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Usuario usuarioLogado = obterUsuarioLogado(request);
        if (!usuarioPodeAcessarRelatorios(usuarioLogado, request, response)) {
            return;
        }

        try {
            List<RelatorioFreteDTO> fretes = relatorioBO.gerarRelatorioFretes();
            request.setAttribute("fretes", fretes);
            prepararIndicadores(request, fretes);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("fretes", Collections.emptyList());
            prepararIndicadores(request, Collections.<RelatorioFreteDTO>emptyList());
        }

        request.getRequestDispatcher(VIEW_RELATORIO_FRETES).forward(request, response);
    }

    private void prepararIndicadores(HttpServletRequest request, List<RelatorioFreteDTO> fretes) {
        int totalFretes = 0;
        int totalEntregues = 0;
        int totalEmTransito = 0;
        int totalCancelados = 0;

        for (RelatorioFreteDTO frete : fretes) {
            totalFretes++;

            if (frete.getStatus() == StatusFrete.ENTREGUE) {
                totalEntregues++;
            } else if (frete.getStatus() == StatusFrete.EM_TRANSITO) {
                totalEmTransito++;
            } else if (frete.getStatus() == StatusFrete.CANCELADO) {
                totalCancelados++;
            }
        }

        request.setAttribute("totalFretes", totalFretes);
        request.setAttribute("totalEntregues", totalEntregues);
        request.setAttribute("totalEmTransito", totalEmTransito);
        request.setAttribute("totalCancelados", totalCancelados);
    }

    private boolean usuarioPodeAcessarRelatorios(Usuario usuarioLogado, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        if (!usuarioLogado.possuiPerfil(PerfilUsuario.ADMIN)
                && !usuarioLogado.possuiPerfil(PerfilUsuario.GESTOR)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return false;
        }

        return true;
    }

    private Usuario obterUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Usuario) session.getAttribute("usuarioLogado");
    }
}
