package br.com.gwfrete.controller;

import br.com.gwfrete.bo.ClienteBO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.Cliente;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.TipoCliente;
import br.com.gwfrete.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ClienteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_LISTA = "/WEB-INF/views/clientes/listaClientes.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/clientes/formularioCliente.jsp";

    private final ClienteBO clienteBO = new ClienteBO();

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
                listarClientes(request, response);
            }
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            listarClientesComTratamento(request, response);
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
            salvarCliente(request, response);
        } else if ("/atualizar".equals(rota)) {
            atualizarCliente(request, response);
        } else if ("/inativar".equals(rota)) {
            inativarCliente(request, response);
        } else if ("/ativar".equals(rota)) {
            ativarCliente(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/clientes");
        }
    }

    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        String nomeFiltro = obterParametroFiltro(request, "nome");
        String cpfCnpjFiltro = obterParametroFiltro(request, "cpfCnpj");
        String tipoFiltro = obterParametroFiltro(request, "tipo");
        String cidadeFiltro = obterParametroFiltro(request, "cidade");
        String statusFiltro = obterParametroFiltro(request, "status");

        request.setAttribute("clientes", clienteBO.listarComFiltros(nomeFiltro, cpfCnpjFiltro,
                obterTipoCliente(tipoFiltro), cidadeFiltro, obterStatusFiltro(statusFiltro)));
        request.setAttribute("podeGerenciarClientes", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        prepararFiltrosLista(request, nomeFiltro, cpfCnpjFiltro, tipoFiltro, cidadeFiltro, statusFiltro);
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarClientesComTratamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String nomeFiltro = obterParametroFiltro(request, "nome");
            String cpfCnpjFiltro = obterParametroFiltro(request, "cpfCnpj");
            String tipoFiltro = obterParametroFiltro(request, "tipo");
            String cidadeFiltro = obterParametroFiltro(request, "cidade");
            String statusFiltro = obterParametroFiltro(request, "status");

            request.setAttribute("clientes", clienteBO.listarComFiltros(nomeFiltro, cpfCnpjFiltro,
                    obterTipoCliente(tipoFiltro), cidadeFiltro, obterStatusFiltro(statusFiltro)));
            prepararFiltrosLista(request, nomeFiltro, cpfCnpjFiltro, tipoFiltro, cidadeFiltro, statusFiltro);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }

        request.setAttribute("podeGerenciarClientes", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Cliente cliente = new Cliente();
        cliente.setStatus(Boolean.TRUE);

        request.setAttribute("cliente", cliente);
        prepararFormulario(request, "Novo cliente", request.getContextPath() + "/clientes/salvar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        Long id = obterId(request);
        Cliente cliente = clienteBO.buscarPorId(id);

        if (cliente == null) {
            throw new CadastroException("Cliente não encontrado.");
        }

        request.setAttribute("cliente", cliente);
        prepararFormulario(request, "Editar cliente", request.getContextPath() + "/clientes/atualizar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void salvarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cliente cliente = montarClienteFormulario(request);

        try {
            clienteBO.salvar(cliente);
            redirecionarComMensagem(request, response, "Cliente cadastrado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("cliente", cliente);
            prepararFormulario(request, "Novo cliente", request.getContextPath() + "/clientes/salvar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void atualizarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Cliente cliente = montarClienteFormulario(request);
        cliente.setId(obterId(request));

        try {
            clienteBO.atualizar(cliente);
            redirecionarComMensagem(request, response, "Cliente atualizado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("cliente", cliente);
            prepararFormulario(request, "Editar cliente", request.getContextPath() + "/clientes/atualizar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void inativarCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            clienteBO.inativar(obterId(request));
            redirecionarComMensagem(request, response, "Cliente inativado com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private void ativarCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            clienteBO.ativar(obterId(request));
            redirecionarComMensagem(request, response, "Cliente ativado com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private Cliente montarClienteFormulario(HttpServletRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.getParameter("nome"));
        cliente.setTipo(obterTipoCliente(request.getParameter("tipo")));
        cliente.setCpfCnpj(request.getParameter("cpfCnpj"));
        cliente.setEmail(request.getParameter("email"));
        cliente.setTelefone(request.getParameter("telefone"));
        cliente.setContato(request.getParameter("contato"));
        cliente.setEndereco(request.getParameter("endereco"));
        cliente.setCidade(request.getParameter("cidade"));
        cliente.setEstado(request.getParameter("estado"));
        cliente.setCep(request.getParameter("cep"));
        cliente.setStatus(obterStatus(request.getParameter("status")));
        return cliente;
    }

    private void prepararFormulario(HttpServletRequest request, String titulo, String acao) {
        request.setAttribute("tituloFormulario", titulo);
        request.setAttribute("acaoFormulario", acao);
        request.setAttribute("tiposCliente", TipoCliente.values());
    }

    private void prepararFiltrosLista(HttpServletRequest request, String nomeFiltro, String cpfCnpjFiltro,
            String tipoFiltro, String cidadeFiltro, String statusFiltro) {
        request.setAttribute("nomeFiltro", nomeFiltro);
        request.setAttribute("cpfCnpjFiltro", cpfCnpjFiltro);
        request.setAttribute("tipoFiltro", tipoFiltro);
        request.setAttribute("cidadeFiltro", cidadeFiltro);
        request.setAttribute("statusFiltro", statusFiltro);
        request.setAttribute("tiposCliente", TipoCliente.values());
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

        request.getSession().setAttribute("mensagemErro", "Seu perfil permite apenas visualizar clientes.");
        response.sendRedirect(request.getContextPath() + "/clientes");
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
        response.sendRedirect(request.getContextPath() + "/clientes");
    }

    private void redirecionarComErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws IOException {

        request.getSession().setAttribute("mensagemErro", mensagem);
        response.sendRedirect(request.getContextPath() + "/clientes");
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
        try {
            String id = request.getParameter("id");
            return id == null || id.trim().isEmpty() ? null : Long.valueOf(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private TipoCliente obterTipoCliente(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : TipoCliente.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Boolean obterStatus(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return Boolean.TRUE;
        }

        return Boolean.valueOf(valor);
    }

    private Boolean obterStatusFiltro(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }

        return Boolean.valueOf(valor);
    }
}
