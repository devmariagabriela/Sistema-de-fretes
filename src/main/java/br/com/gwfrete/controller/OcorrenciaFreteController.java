package br.com.gwfrete.controller;

import br.com.gwfrete.bo.OcorrenciaFreteBO;
import br.com.gwfrete.dao.FreteDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.Frete;
import br.com.gwfrete.model.OcorrenciaFrete;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.TipoOcorrenciaFrete;
import br.com.gwfrete.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;

public class OcorrenciaFreteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_LISTA = "/WEB-INF/views/ocorrencias-frete/listaOcorrenciasFrete.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/ocorrencias-frete/formularioOcorrenciaFrete.jsp";

    private final OcorrenciaFreteBO ocorrenciaFreteBO = new OcorrenciaFreteBO();
    private final FreteDAO freteDAO = new FreteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Usuario usuarioLogado = obterUsuarioLogado(request);
        if (!usuarioPodeVisualizar(usuarioLogado, request, response)) {
            return;
        }

        String rota = obterRota(request);

        try {
            if ("/nova".equals(rota)) {
                if (!usuarioPodeGerenciar(usuarioLogado, request, response)) {
                    return;
                }
                abrirFormularioNova(request, response);
            } else if ("/frete".equals(rota)) {
                listarOcorrenciasPorFrete(request, response);
            } else {
                listarOcorrencias(request, response);
            }
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            listarOcorrenciasComTratamento(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Usuario usuarioLogado = obterUsuarioLogado(request);
        if (!usuarioPodeVisualizar(usuarioLogado, request, response)) {
            return;
        }

        if (!usuarioPodeGerenciar(usuarioLogado, request, response)) {
            return;
        }

        String rota = obterRota(request);

        if ("/salvar".equals(rota)) {
            salvarOcorrencia(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/ocorrencias-frete");
        }
    }

    private void listarOcorrencias(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        Long freteId = obterFreteId(request);
        prepararListagem(request, freteId);
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarOcorrenciasPorFrete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        Long freteId = obterFreteId(request);
        prepararListagem(request, freteId);
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarOcorrenciasComTratamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            prepararListagem(request, obterFreteId(request));
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("ocorrencias", Collections.emptyList());
            prepararFretesComTratamento(request);
        }

        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void abrirFormularioNova(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        OcorrenciaFrete ocorrencia = new OcorrenciaFrete();
        ocorrencia.setFrete(montarFrete(obterFreteId(request)));
        ocorrencia.setDataHora(LocalDateTime.now());

        request.setAttribute("ocorrencia", ocorrencia);
        prepararFormulario(request, "Nova ocorrência", request.getContextPath() + "/ocorrencias-frete/salvar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void salvarOcorrencia(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OcorrenciaFrete ocorrencia = montarOcorrenciaFormulario(request);

        try {
            ocorrenciaFreteBO.salvar(ocorrencia);
            request.getSession().setAttribute("mensagemSucesso", "Ocorrência registrada com sucesso.");
            response.sendRedirect(request.getContextPath() + "/ocorrencias-frete/frete?freteId="
                    + ocorrencia.getFrete().getId());
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("ocorrencia", ocorrencia);
            prepararFormularioComTratamento(request, "Nova ocorrência",
                    request.getContextPath() + "/ocorrencias-frete/salvar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void prepararListagem(HttpServletRequest request, Long freteId) throws CadastroException {
        prepararFretes(request);
        request.setAttribute("podeGerenciarOcorrencias", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.setAttribute("freteSelecionadoId", freteId);

        if (freteId == null || freteId <= 0) {
            request.setAttribute("ocorrencias", Collections.emptyList());
            return;
        }

        request.setAttribute("ocorrencias", ocorrenciaFreteBO.listarPorFrete(freteId));
    }

    private void prepararFormulario(HttpServletRequest request, String titulo, String acao) throws CadastroException {
        request.setAttribute("tituloFormulario", titulo);
        request.setAttribute("acaoFormulario", acao);
        request.setAttribute("tiposOcorrencia", TipoOcorrenciaFrete.values());
        prepararFretes(request);
    }

    private void prepararFormularioComTratamento(HttpServletRequest request, String titulo, String acao) {
        try {
            prepararFormulario(request, titulo, acao);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }
    }

    private void prepararFretes(HttpServletRequest request) throws CadastroException {
        try {
            request.setAttribute("fretes", freteDAO.listarTodos());
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível carregar os fretes.");
        }
    }

    private void prepararFretesComTratamento(HttpServletRequest request) {
        try {
            prepararFretes(request);
        } catch (CadastroException e) {
            request.setAttribute("fretes", Collections.emptyList());
        }
    }

    private OcorrenciaFrete montarOcorrenciaFormulario(HttpServletRequest request) {
        OcorrenciaFrete ocorrencia = new OcorrenciaFrete();
        ocorrencia.setFrete(montarFrete(obterFreteId(request)));
        ocorrencia.setTipo(obterTipo(request.getParameter("tipo")));
        ocorrencia.setDataHora(obterDataHora(request.getParameter("dataHora")));
        ocorrencia.setLocalizacao(request.getParameter("localizacao"));
        ocorrencia.setDescricao(request.getParameter("descricao"));
        ocorrencia.setNomeRecebedor(request.getParameter("nomeRecebedor"));
        ocorrencia.setDocumentoRecebedor(request.getParameter("documentoRecebedor"));
        return ocorrencia;
    }

    private Frete montarFrete(Long id) {
        Frete frete = new Frete();
        frete.setId(id);
        return frete;
    }

    private boolean usuarioPodeVisualizar(Usuario usuarioLogado, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        if (!usuarioLogado.possuiPerfil(PerfilUsuario.ADMIN)
                && !usuarioLogado.possuiPerfil(PerfilUsuario.GESTOR)
                && !usuarioLogado.possuiPerfil(PerfilUsuario.OPERADOR)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return false;
        }

        return true;
    }

    private boolean usuarioPodeGerenciar(Usuario usuarioLogado, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (usuarioPodeGerenciar(usuarioLogado)) {
            return true;
        }

        request.getSession().setAttribute("mensagemErro", "Seu perfil permite apenas visualizar ocorrências.");
        response.sendRedirect(request.getContextPath() + "/ocorrencias-frete");
        return false;
    }

    private boolean usuarioPodeGerenciar(Usuario usuarioLogado) {
        return usuarioLogado != null
                && (usuarioLogado.possuiPerfil(PerfilUsuario.ADMIN)
                || usuarioLogado.possuiPerfil(PerfilUsuario.GESTOR));
    }

    private Usuario obterUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Usuario) session.getAttribute("usuarioLogado");
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

        Object mensagemErro = session.getAttribute("mensagemErro");
        if (mensagemErro != null) {
            request.setAttribute("mensagemErro", mensagemErro);
            session.removeAttribute("mensagemErro");
        }
    }

    private String obterRota(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return pathInfo == null ? "" : pathInfo;
    }

    private Long obterFreteId(HttpServletRequest request) {
        return obterLong(request.getParameter("freteId"));
    }

    private Long obterLong(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : Long.valueOf(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime obterDataHora(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : LocalDateTime.parse(valor);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private TipoOcorrenciaFrete obterTipo(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : TipoOcorrenciaFrete.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
