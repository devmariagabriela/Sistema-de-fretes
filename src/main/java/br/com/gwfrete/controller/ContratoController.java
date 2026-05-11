package br.com.gwfrete.controller;

import br.com.gwfrete.bo.ContratoBO;
import br.com.gwfrete.dao.ClienteDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.Cliente;
import br.com.gwfrete.model.Contrato;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.StatusContrato;
import br.com.gwfrete.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;

public class ContratoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_LISTA = "/WEB-INF/views/contratos/listaContratos.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/contratos/formularioContrato.jsp";

    private final ContratoBO contratoBO = new ContratoBO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

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
            } else if ("/editar".equals(rota)) {
                if (!usuarioPodeGerenciar(usuarioLogado, request, response)) {
                    return;
                }
                abrirFormularioEdicao(request, response);
            } else {
                listarContratos(request, response);
            }
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            listarContratosComTratamento(request, response);
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
            salvarContrato(request, response);
        } else if ("/atualizar".equals(rota)) {
            atualizarContrato(request, response);
        } else if ("/encerrar".equals(rota) || "/inativar".equals(rota)) {
            inativarContrato(request, response);
        } else if ("/suspender".equals(rota)) {
            suspenderContrato(request, response);
        } else if ("/cancelar".equals(rota)) {
            cancelarContrato(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/contratos");
        }
    }

    private void listarContratos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        String statusFiltro = obterParametroFiltro(request, "status");
        request.setAttribute("contratos", contratoBO.listarComFiltros(obterStatus(statusFiltro)));
        request.setAttribute("statusFiltro", statusFiltro);
        request.setAttribute("statusContratos", StatusContrato.values());
        request.setAttribute("podeGerenciarContratos", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarContratosComTratamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String statusFiltro = obterParametroFiltro(request, "status");
            request.setAttribute("contratos", contratoBO.listarComFiltros(obterStatus(statusFiltro)));
            request.setAttribute("statusFiltro", statusFiltro);
            request.setAttribute("statusContratos", StatusContrato.values());
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }

        request.setAttribute("podeGerenciarContratos", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Contrato contrato = new Contrato();
        contrato.setStatus(StatusContrato.ATIVO);
        contrato.setDataInicio(LocalDate.now());

        request.setAttribute("contrato", contrato);
        prepararFormulario(request, "Novo contrato", request.getContextPath() + "/contratos/salvar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        Long id = obterId(request);
        Contrato contrato = contratoBO.buscarPorId(id);

        if (contrato == null) {
            throw new CadastroException("Contrato não encontrado.");
        }

        request.setAttribute("contrato", contrato);
        prepararFormulario(request, "Editar contrato", request.getContextPath() + "/contratos/atualizar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void salvarContrato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Contrato contrato = montarContratoFormulario(request);

        try {
            contratoBO.salvar(contrato);
            redirecionarComMensagem(request, response, "Contrato cadastrado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("contrato", contrato);
            prepararFormulario(request, "Novo contrato", request.getContextPath() + "/contratos/salvar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void atualizarContrato(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Contrato contrato = montarContratoFormulario(request);
        contrato.setId(obterId(request));

        try {
            contratoBO.atualizar(contrato);
            redirecionarComMensagem(request, response, "Contrato atualizado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("contrato", contrato);
            prepararFormulario(request, "Editar contrato", request.getContextPath() + "/contratos/atualizar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void inativarContrato(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            contratoBO.inativar(obterId(request));
            redirecionarComMensagem(request, response, "Contrato encerrado com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private void suspenderContrato(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            contratoBO.suspender(obterId(request));
            redirecionarComMensagem(request, response, "Contrato suspenso com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private void cancelarContrato(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            contratoBO.cancelar(obterId(request));
            redirecionarComMensagem(request, response, "Contrato cancelado com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private Contrato montarContratoFormulario(HttpServletRequest request) {
        Contrato contrato = new Contrato();
        contrato.setCliente(criarClienteReferencia(obterLong(request.getParameter("clienteId"))));
        contrato.setNumero(request.getParameter("numero"));
        contrato.setDescricao(request.getParameter("descricao"));
        contrato.setValorMensal(obterBigDecimal(request.getParameter("valorMensal")));
        contrato.setDataInicio(obterData(request.getParameter("dataInicio")));
        contrato.setDataFim(obterData(request.getParameter("dataFim")));
        contrato.setReajustePercentual(obterBigDecimal(request.getParameter("reajustePercentual")));
        contrato.setStatus(obterStatus(request.getParameter("status")));
        contrato.setObservacao(request.getParameter("observacao"));
        return contrato;
    }

    private void prepararFormulario(HttpServletRequest request, String titulo, String acao) {
        request.setAttribute("tituloFormulario", titulo);
        request.setAttribute("acaoFormulario", acao);
        request.setAttribute("statusContrato", StatusContrato.values());

        try {
            request.setAttribute("clientes", clienteDAO.listarTodos());
        } catch (SQLException e) {
            request.setAttribute("clientes", Collections.emptyList());
            request.setAttribute("mensagemErro", "Não foi possível carregar os clientes para o formulário.");
        }
    }

    private Cliente criarClienteReferencia(Long id) {
        if (id == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setId(id);
        return cliente;
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

        request.getSession().setAttribute("mensagemErro", "Seu perfil permite apenas visualizar contratos.");
        response.sendRedirect(request.getContextPath() + "/contratos");
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

    private void redirecionarComMensagem(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws IOException {

        request.getSession().setAttribute("mensagemSucesso", mensagem);
        response.sendRedirect(request.getContextPath() + "/contratos");
    }

    private void redirecionarComErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws IOException {

        request.getSession().setAttribute("mensagemErro", mensagem);
        response.sendRedirect(request.getContextPath() + "/contratos");
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

    private String obterParametroFiltro(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }

    private Long obterId(HttpServletRequest request) {
        return obterLong(request.getParameter("id"));
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

    private LocalDate obterData(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private StatusContrato obterStatus(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : StatusContrato.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
