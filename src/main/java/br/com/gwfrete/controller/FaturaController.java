package br.com.gwfrete.controller;

import br.com.gwfrete.bo.FaturaBO;
import br.com.gwfrete.dao.ClienteDAO;
import br.com.gwfrete.dao.FreteDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.Cliente;
import br.com.gwfrete.model.Fatura;
import br.com.gwfrete.model.Frete;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.StatusFatura;
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

public class FaturaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_LISTA = "/WEB-INF/views/financeiro/listaFaturas.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/financeiro/formularioFatura.jsp";

    private final FaturaBO faturaBO = new FaturaBO();
    private final FreteDAO freteDAO = new FreteDAO();
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
            if ("/nova".equals(rota)) {
                if (!usuarioPodeGerenciar(usuarioLogado, request, response)) {
                    return;
                }
                abrirFormularioNova(request, response);
            } else if ("/editar".equals(rota)) {
                if (!usuarioPodeGerenciar(usuarioLogado, request, response)) {
                    return;
                }
                abrirFormularioEdicao(request, response);
            } else {
                listarFaturas(request, response);
            }
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            listarFaturasComTratamento(request, response);
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
            salvarFatura(request, response);
        } else if ("/atualizar".equals(rota)) {
            atualizarFatura(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/financeiro");
        }
    }

    private void listarFaturas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        request.setAttribute("faturas", faturaBO.listarTodos());
        request.setAttribute("podeGerenciarFaturas", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarFaturasComTratamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute("faturas", faturaBO.listarTodos());
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }

        request.setAttribute("podeGerenciarFaturas", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void abrirFormularioNova(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        Fatura fatura = new Fatura();
        fatura.setStatus(StatusFatura.PENDENTE);
        fatura.setDataEmissao(LocalDate.now());

        request.setAttribute("fatura", fatura);
        prepararFormulario(request, "Nova fatura", request.getContextPath() + "/financeiro/salvar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        Long id = obterId(request);
        Fatura fatura = faturaBO.buscarPorId(id);

        if (fatura == null) {
            throw new CadastroException("Fatura não encontrada.");
        }

        request.setAttribute("fatura", fatura);
        prepararFormulario(request, "Editar fatura", request.getContextPath() + "/financeiro/atualizar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void salvarFatura(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Fatura fatura = montarFaturaFormulario(request);

        try {
            faturaBO.salvar(fatura);
            redirecionarComMensagem(request, response, "Fatura cadastrada com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("fatura", fatura);
            prepararFormularioComTratamento(request, "Nova fatura", request.getContextPath() + "/financeiro/salvar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void atualizarFatura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Fatura fatura = montarFaturaFormulario(request);
        fatura.setId(obterId(request));

        try {
            faturaBO.atualizar(fatura);
            redirecionarComMensagem(request, response, "Fatura atualizada com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("fatura", fatura);
            prepararFormularioComTratamento(request, "Editar fatura", request.getContextPath() + "/financeiro/atualizar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private Fatura montarFaturaFormulario(HttpServletRequest request) {
        Fatura fatura = new Fatura();
        fatura.setFrete(criarFreteReferencia(obterLong(request.getParameter("freteId"))));
        fatura.setCliente(criarClienteReferencia(obterLong(request.getParameter("clienteId"))));
        fatura.setNumero(request.getParameter("numero"));
        fatura.setValor(obterBigDecimal(request.getParameter("valor")));
        fatura.setDataEmissao(obterData(request.getParameter("dataEmissao")));
        fatura.setDataVencimento(obterData(request.getParameter("dataVencimento")));
        fatura.setDataPagamento(obterData(request.getParameter("dataPagamento")));
        fatura.setStatus(obterStatus(request.getParameter("status")));
        fatura.setObservacao(request.getParameter("observacao"));
        return fatura;
    }

    private void prepararFormulario(HttpServletRequest request, String titulo, String acao) throws CadastroException {
        try {
            request.setAttribute("tituloFormulario", titulo);
            request.setAttribute("acaoFormulario", acao);
            request.setAttribute("statusFatura", StatusFatura.values());
            request.setAttribute("fretes", freteDAO.listarTodos());
            request.setAttribute("clientes", clienteDAO.listarTodos());
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível carregar os dados do formulário financeiro.");
        }
    }

    private void prepararFormularioComTratamento(HttpServletRequest request, String titulo, String acao) {
        try {
            prepararFormulario(request, titulo, acao);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }
    }

    private Frete criarFreteReferencia(Long id) {
        if (id == null) {
            return null;
        }

        Frete frete = new Frete();
        frete.setId(id);
        return frete;
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

        request.getSession().setAttribute("mensagemErro", "Seu perfil permite apenas visualizar faturas.");
        response.sendRedirect(request.getContextPath() + "/financeiro");
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
        response.sendRedirect(request.getContextPath() + "/financeiro");
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
            if (valor == null || valor.trim().isEmpty()) {
                return null;
            }

            return new BigDecimal(valor.trim().replace(",", "."));
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

    private StatusFatura obterStatus(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : StatusFatura.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
