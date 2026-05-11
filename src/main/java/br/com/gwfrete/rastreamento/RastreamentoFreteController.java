package br.com.gwfrete.rastreamento;

import br.com.gwfrete.rastreamento.RastreamentoFreteBO;
import br.com.gwfrete.frete.FreteDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.frete.Frete;
import br.com.gwfrete.usuario.PerfilUsuario;
import br.com.gwfrete.rastreamento.RastreamentoFrete;
import br.com.gwfrete.usuario.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;

public class RastreamentoFreteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_LISTA = "/WEB-INF/views/rastreamentos/listaRastreamentos.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/rastreamentos/formularioRastreamento.jsp";

    private final RastreamentoFreteBO rastreamentoFreteBO = new RastreamentoFreteBO();
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
            if ("/novo".equals(rota)) {
                if (!usuarioPodeGerenciar(usuarioLogado, request, response)) {
                    return;
                }
                abrirFormularioNovo(request, response);
            } else {
                listarRastreamentos(request, response);
            }
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            listarRastreamentosComTratamento(request, response);
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
            salvarRastreamento(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/rastreamentos");
        }
    }

    private void listarRastreamentos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        prepararLista(request);
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarRastreamentosComTratamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            prepararLista(request);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("rastreamentos", Collections.emptyList());
            carregarFretesComTratamento(request);
        }

        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RastreamentoFrete rastreamento = new RastreamentoFrete();
        rastreamento.setDataHora(LocalDateTime.now().withSecond(0).withNano(0));
        rastreamento.setFrete(criarFreteReferencia(obterLong(request.getParameter("freteId"))));

        request.setAttribute("rastreamento", rastreamento);
        prepararFormulario(request);
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void salvarRastreamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RastreamentoFrete rastreamento = montarRastreamentoFormulario(request);

        try {
            rastreamentoFreteBO.salvar(rastreamento);
            redirecionarComMensagem(request, response, rastreamento.getFrete().getId(),
                    "Ponto de rastreamento cadastrado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("rastreamento", rastreamento);
            prepararFormulario(request);
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void prepararLista(HttpServletRequest request) throws CadastroException {
        Long freteId = obterLong(request.getParameter("freteId"));

        request.setAttribute("freteIdSelecionado", freteId);
        request.setAttribute("podeGerenciarRastreamentos", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        carregarFretes(request);

        if (freteId == null) {
            request.setAttribute("rastreamentos", Collections.emptyList());
            return;
        }

        request.setAttribute("rastreamentos", rastreamentoFreteBO.listarPorFrete(freteId));
    }

    private void prepararFormulario(HttpServletRequest request) {
        request.setAttribute("acaoFormulario", request.getContextPath() + "/rastreamentos/salvar");
        carregarFretesComTratamento(request);
    }

    private void carregarFretes(HttpServletRequest request) throws CadastroException {
        try {
            request.setAttribute("fretes", freteDAO.listarTodos());
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível carregar os fretes.");
        }
    }

    private void carregarFretesComTratamento(HttpServletRequest request) {
        try {
            carregarFretes(request);
        } catch (CadastroException e) {
            request.setAttribute("fretes", Collections.emptyList());
            request.setAttribute("mensagemErro", e.getMessage());
        }
    }

    private RastreamentoFrete montarRastreamentoFormulario(HttpServletRequest request) {
        RastreamentoFrete rastreamento = new RastreamentoFrete();
        rastreamento.setFrete(criarFreteReferencia(obterLong(request.getParameter("freteId"))));
        rastreamento.setLatitude(obterBigDecimal(request.getParameter("latitude")));
        rastreamento.setLongitude(obterBigDecimal(request.getParameter("longitude")));
        rastreamento.setLocalizacao(request.getParameter("localizacao"));
        rastreamento.setObservacao(request.getParameter("observacao"));
        rastreamento.setDataHora(obterDataHora(request.getParameter("dataHora")));
        return rastreamento;
    }

    private Frete criarFreteReferencia(Long id) {
        if (id == null) {
            return null;
        }

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

        request.getSession().setAttribute("mensagemErro", "Seu perfil permite apenas visualizar rastreamentos.");
        response.sendRedirect(request.getContextPath() + "/rastreamentos");
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

    private void redirecionarComMensagem(HttpServletRequest request, HttpServletResponse response,
                                         Long freteId, String mensagem) throws IOException {

        request.getSession().setAttribute("mensagemSucesso", mensagem);
        response.sendRedirect(request.getContextPath() + "/rastreamentos/frete?freteId=" + freteId);
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

    private Long obterLong(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : Long.valueOf(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal obterBigDecimal(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : new BigDecimal(valor.trim().replace(",", "."));
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
}
