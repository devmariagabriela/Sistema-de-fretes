package br.com.gwfrete.controller;

import br.com.gwfrete.bo.RecuperacaoSenhaBO;
import br.com.gwfrete.exception.CadastroException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RecuperacaoSenhaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_ESQUECI_SENHA = "/WEB-INF/views/esqueciSenha.jsp";
    private static final String VIEW_REDEFINIR_SENHA = "/WEB-INF/views/redefinirSenha.jsp";
    private static final String MENSAGEM_SOLICITACAO = "Se o e-mail estiver cadastrado, enviaremos as instruções de recuperação.";

    private final RecuperacaoSenhaBO recuperacaoSenhaBO = new RecuperacaoSenhaBO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String rota = obterRota(request);

        if ("/redefinir-senha".equals(rota)) {
            abrirRedefinicao(request, response);
            return;
        }

        request.getRequestDispatcher(VIEW_ESQUECI_SENHA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String rota = obterRota(request);

        if ("/recuperar-senha".equals(rota)) {
            solicitarRecuperacao(request, response);
            return;
        }

        if ("/redefinir-senha".equals(rota)) {
            redefinirSenha(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/esqueci-senha");
    }

    private void solicitarRecuperacao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            recuperacaoSenhaBO.solicitarRecuperacao(request.getParameter("email"), montarUrlRedefinicao(request));
        } catch (CadastroException e) {
            // A resposta permanece genérica para não revelar usuários cadastrados.
        }

        request.setAttribute("mensagemSucesso", MENSAGEM_SOLICITACAO);
        request.getRequestDispatcher(VIEW_ESQUECI_SENHA).forward(request, response);
    }

    private void abrirRedefinicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");

        try {
            recuperacaoSenhaBO.validarToken(token);
            request.setAttribute("token", token);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }

        request.getRequestDispatcher(VIEW_REDEFINIR_SENHA).forward(request, response);
    }

    private void redefinirSenha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");

        try {
            recuperacaoSenhaBO.redefinirSenha(token, request.getParameter("novaSenha"),
                    request.getParameter("confirmacaoSenha"));
            request.setAttribute("mensagemSucesso", "Senha redefinida com sucesso. Acesse com sua nova senha.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("token", token);
            request.getRequestDispatcher(VIEW_REDEFINIR_SENHA).forward(request, response);
        }
    }

    private String montarUrlRedefinicao(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName()
                + (portaPadrao(request) ? "" : ":" + request.getServerPort())
                + request.getContextPath() + "/redefinir-senha";
    }

    private boolean portaPadrao(HttpServletRequest request) {
        return ("http".equalsIgnoreCase(request.getScheme()) && request.getServerPort() == 80)
                || ("https".equalsIgnoreCase(request.getScheme()) && request.getServerPort() == 443);
    }

    private String obterRota(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return path == null || path.trim().isEmpty() ? "/esqueci-senha" : path;
    }
}
