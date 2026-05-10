package br.com.gwfrete.controller;

import br.com.gwfrete.bo.RelatorioBO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.RelatorioFreteDTO;
import br.com.gwfrete.model.StatusFrete;
import br.com.gwfrete.model.Usuario;
import br.com.gwfrete.util.RelatorioFretePdfUtil;
import net.sf.jasperreports.engine.JRException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

        FiltroRelatorioFrete filtro = obterFiltro(request);

        if (isRotaPdf(request)) {
            exportarPdf(response, filtro);
            return;
        }

        try {
            consumirMensagemSessao(request);
            prepararFiltro(request, filtro);
            List<RelatorioFreteDTO> fretes = relatorioBO.gerarRelatorioFretes(filtro.dataInicial, filtro.dataFinal,
                    filtro.status, filtro.motorista, filtro.veiculo);
            request.setAttribute("fretes", fretes);
            prepararIndicadores(request, fretes);
        } catch (CadastroException e) {
            prepararFiltro(request, filtro);
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("fretes", Collections.emptyList());
            prepararIndicadores(request, Collections.<RelatorioFreteDTO>emptyList());
        }

        request.getRequestDispatcher(VIEW_RELATORIO_FRETES).forward(request, response);
    }

    private void exportarPdf(HttpServletResponse response, FiltroRelatorioFrete filtro) throws IOException {
        try {
            List<RelatorioFreteDTO> fretes = relatorioBO.gerarRelatorioFretes(filtro.dataInicial, filtro.dataFinal,
                    filtro.status, filtro.motorista, filtro.veiculo);
            byte[] pdf = RelatorioFretePdfUtil.gerarPdf(fretes);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"relatorio-operacional-fretes.pdf\"");
            response.setContentLength(pdf.length);
            response.getOutputStream().write(pdf);
            response.getOutputStream().flush();
        } catch (CadastroException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (JRException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Não foi possível gerar o PDF do relatório de fretes.");
        }
    }

    private void prepararFiltro(HttpServletRequest request, FiltroRelatorioFrete filtro) {
        request.setAttribute("dataInicialFiltro", filtro.dataInicialTexto);
        request.setAttribute("dataFinalFiltro", filtro.dataFinalTexto);
        request.setAttribute("statusFiltro", filtro.statusTexto);
        request.setAttribute("motoristaFiltro", filtro.motorista);
        request.setAttribute("veiculoFiltro", filtro.veiculo);
        request.setAttribute("queryStringFiltros", montarQueryString(filtro));
    }

    private FiltroRelatorioFrete obterFiltro(HttpServletRequest request) {
        FiltroRelatorioFrete filtro = new FiltroRelatorioFrete();
        filtro.dataInicialTexto = normalizar(request.getParameter("dataInicial"));
        filtro.dataFinalTexto = normalizar(request.getParameter("dataFinal"));
        filtro.statusTexto = normalizar(request.getParameter("status"));
        filtro.motorista = normalizar(request.getParameter("motorista"));
        filtro.veiculo = normalizar(request.getParameter("veiculo"));
        filtro.dataInicial = parseData(filtro.dataInicialTexto);
        filtro.dataFinal = parseData(filtro.dataFinalTexto);
        filtro.status = parseStatus(filtro.statusTexto);
        return filtro;
    }

    private LocalDate parseData(String valor) {
        if (valor == null || valor.isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private StatusFrete parseStatus(String valor) {
        if (valor == null || valor.isEmpty()) {
            return null;
        }

        try {
            return StatusFrete.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.trim();
    }

    private String montarQueryString(FiltroRelatorioFrete filtro) {
        StringBuilder queryString = new StringBuilder();
        adicionarParametro(queryString, "dataInicial", filtro.dataInicialTexto);
        adicionarParametro(queryString, "dataFinal", filtro.dataFinalTexto);
        adicionarParametro(queryString, "status", filtro.statusTexto);
        adicionarParametro(queryString, "motorista", filtro.motorista);
        adicionarParametro(queryString, "veiculo", filtro.veiculo);
        return queryString.toString();
    }

    private void adicionarParametro(StringBuilder queryString, String nome, String valor) {
        if (valor == null || valor.isEmpty()) {
            return;
        }

        if (queryString.length() == 0) {
            queryString.append('?');
        } else {
            queryString.append('&');
        }

        try {
            queryString.append(URLEncoder.encode(nome, "UTF-8"))
                    .append('=')
                    .append(URLEncoder.encode(valor, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 não suportado.", e);
        }
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

    private void consumirMensagemSessao(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        Object mensagemErro = session.getAttribute("mensagemErro");
        if (mensagemErro != null) {
            request.setAttribute("mensagemErro", mensagemErro);
            session.removeAttribute("mensagemErro");
        }
    }

    private boolean isRotaPdf(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath != null && servletPath.endsWith("/pdf");
    }

    private Usuario obterUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Usuario) session.getAttribute("usuarioLogado");
    }

    private static class FiltroRelatorioFrete {
        private String dataInicialTexto;
        private String dataFinalTexto;
        private String statusTexto;
        private String motorista;
        private String veiculo;
        private LocalDate dataInicial;
        private LocalDate dataFinal;
        private StatusFrete status;
    }
}
