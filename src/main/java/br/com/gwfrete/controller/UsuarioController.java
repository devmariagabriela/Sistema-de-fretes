package br.com.gwfrete.controller;

import br.com.gwfrete.bo.UsuarioBO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.StatusUsuario;
import br.com.gwfrete.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UsuarioController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_LISTA = "/WEB-INF/views/usuarios/listaUsuarios.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/usuarios/formularioUsuario.jsp";

    private final UsuarioBO usuarioBO = new UsuarioBO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (!usuarioAdminLogado(request, response)) {
            return;
        }

        String rota = obterRota(request);

        try {
            if ("/novo".equals(rota)) {
                abrirFormularioNovo(request, response);
            } else if ("/editar".equals(rota)) {
                abrirFormularioEdicao(request, response);
            } else {
                listarUsuarios(request, response);
            }
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            listarUsuariosComTratamento(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (!usuarioAdminLogado(request, response)) {
            return;
        }

        String rota = obterRota(request);

        if ("/salvar".equals(rota)) {
            salvarUsuario(request, response);
        } else if ("/atualizar".equals(rota)) {
            atualizarUsuario(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/usuarios");
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        request.setAttribute("usuarios", usuarioBO.listarTodos());
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarUsuariosComTratamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute("usuarios", usuarioBO.listarTodos());
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }

        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = new Usuario();
        usuario.setStatus(StatusUsuario.ATIVO);
        request.setAttribute("usuario", usuario);
        prepararFormulario(request, "Novo usuário", request.getContextPath() + "/usuarios/salvar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        Long id = obterId(request);
        Usuario usuario = usuarioBO.buscarPorId(id);

        if (usuario == null) {
            throw new CadastroException("Usuário não encontrado.");
        }

        request.setAttribute("usuario", usuario);
        prepararFormulario(request, "Editar usuário", request.getContextPath() + "/usuarios/atualizar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void salvarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = montarUsuarioFormulario(request);

        try {
            usuarioBO.salvar(usuario);
            redirecionarComMensagem(request, response, "Usuário cadastrado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("usuario", usuario);
            prepararFormulario(request, "Novo usuário", request.getContextPath() + "/usuarios/salvar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void atualizarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = montarUsuarioFormulario(request);
        usuario.setId(obterId(request));

        try {
            usuarioBO.atualizar(usuario);
            redirecionarComMensagem(request, response, "Usuário atualizado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("usuario", usuario);
            prepararFormulario(request, "Editar usuário", request.getContextPath() + "/usuarios/atualizar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private Usuario montarUsuarioFormulario(HttpServletRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.getParameter("nome"));
        usuario.setEmail(request.getParameter("email"));
        usuario.setSenha(request.getParameter("senha"));
        usuario.setPerfil(obterPerfil(request.getParameter("perfil")));
        usuario.setStatus(obterStatus(request.getParameter("status")));
        return usuario;
    }

    private void prepararFormulario(HttpServletRequest request, String titulo, String acao) {
        request.setAttribute("tituloFormulario", titulo);
        request.setAttribute("acaoFormulario", acao);
        request.setAttribute("perfis", PerfilUsuario.values());
        request.setAttribute("statusUsuarios", StatusUsuario.values());
    }

    private boolean usuarioAdminLogado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = session == null ? null : (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        if (!usuarioLogado.possuiPerfil(PerfilUsuario.ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return false;
        }

        return true;
    }

    private void redirecionarComMensagem(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws IOException {

        request.getSession().setAttribute("mensagemSucesso", mensagem);
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }

    private void consumirMensagemSessao(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        Object mensagemSucesso = session.getAttribute("mensagemSucesso");

        if (mensagemSucesso != null) {
            request.setAttribute("mensagemSucesso", mensagemSucesso);
            session.removeAttribute("mensagemSucesso");
        }
    }

    private String obterRota(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return pathInfo == null ? "" : pathInfo;
    }

    private Long obterId(HttpServletRequest request) {
        try {
            return Long.valueOf(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private PerfilUsuario obterPerfil(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : PerfilUsuario.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private StatusUsuario obterStatus(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : StatusUsuario.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
