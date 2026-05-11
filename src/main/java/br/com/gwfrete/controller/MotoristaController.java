package br.com.gwfrete.controller;

import br.com.gwfrete.bo.MotoristaBO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.CategoriaCNH;
import br.com.gwfrete.model.Motorista;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.StatusMotorista;
import br.com.gwfrete.model.TipoVinculoMotorista;
import br.com.gwfrete.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MotoristaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_LISTA = "/WEB-INF/views/motoristas/listaMotoristas.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/motoristas/formularioMotorista.jsp";

    private final MotoristaBO motoristaBO = new MotoristaBO();

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
                listarMotoristas(request, response);
            }
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            listarMotoristasComTratamento(request, response);
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
            salvarMotorista(request, response);
        } else if ("/atualizar".equals(rota)) {
            atualizarMotorista(request, response);
        } else if ("/inativar".equals(rota)) {
            inativarMotorista(request, response);
        } else if ("/ativar".equals(rota)) {
            ativarMotorista(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/motoristas");
        }
    }

    private void listarMotoristas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        String nomeFiltro = obterParametroFiltro(request, "nome");
        String cpfFiltro = obterParametroFiltro(request, "cpf");
        String cnhFiltro = obterParametroFiltro(request, "cnh");
        String categoriaCnhFiltro = obterParametroFiltro(request, "categoriaCnh");
        String statusFiltro = obterParametroFiltro(request, "status");

        request.setAttribute("motoristas", motoristaBO.listarComFiltros(nomeFiltro, cpfFiltro, cnhFiltro,
                obterCategoriaCNH(categoriaCnhFiltro), obterStatus(statusFiltro)));
        request.setAttribute("podeGerenciarMotoristas", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        prepararFiltrosLista(request, nomeFiltro, cpfFiltro, cnhFiltro, categoriaCnhFiltro, statusFiltro);
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarMotoristasComTratamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String nomeFiltro = obterParametroFiltro(request, "nome");
            String cpfFiltro = obterParametroFiltro(request, "cpf");
            String cnhFiltro = obterParametroFiltro(request, "cnh");
            String categoriaCnhFiltro = obterParametroFiltro(request, "categoriaCnh");
            String statusFiltro = obterParametroFiltro(request, "status");

            request.setAttribute("motoristas", motoristaBO.listarComFiltros(nomeFiltro, cpfFiltro, cnhFiltro,
                    obterCategoriaCNH(categoriaCnhFiltro), obterStatus(statusFiltro)));
            prepararFiltrosLista(request, nomeFiltro, cpfFiltro, cnhFiltro, categoriaCnhFiltro, statusFiltro);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }

        request.setAttribute("podeGerenciarMotoristas", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Motorista motorista = new Motorista();
        motorista.setStatus(StatusMotorista.ATIVO);

        request.setAttribute("motorista", motorista);
        prepararFormulario(request, "Novo motorista", request.getContextPath() + "/motoristas/salvar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        Long id = obterId(request);
        Motorista motorista = motoristaBO.buscarPorId(id);

        if (motorista == null) {
            throw new CadastroException("Motorista não encontrado.");
        }

        request.setAttribute("motorista", motorista);
        prepararFormulario(request, "Editar motorista", request.getContextPath() + "/motoristas/atualizar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void salvarMotorista(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Motorista motorista = montarMotoristaFormulario(request);

        try {
            motoristaBO.salvar(motorista);
            redirecionarComMensagem(request, response, "Motorista cadastrado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("motorista", motorista);
            prepararFormulario(request, "Novo motorista", request.getContextPath() + "/motoristas/salvar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void atualizarMotorista(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Motorista motorista = montarMotoristaFormulario(request);
        motorista.setId(obterId(request));

        try {
            motoristaBO.atualizar(motorista);
            redirecionarComMensagem(request, response, "Motorista atualizado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("motorista", motorista);
            prepararFormulario(request, "Editar motorista", request.getContextPath() + "/motoristas/atualizar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void inativarMotorista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            motoristaBO.inativar(obterId(request));
            redirecionarComMensagem(request, response, "Motorista inativado com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private void ativarMotorista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            motoristaBO.ativar(obterId(request));
            redirecionarComMensagem(request, response, "Motorista ativado com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private Motorista montarMotoristaFormulario(HttpServletRequest request) {
        Motorista motorista = new Motorista();
        motorista.setNome(request.getParameter("nome"));
        motorista.setCpf(request.getParameter("cpf"));
        motorista.setDataNascimento(obterData(request.getParameter("dataNascimento")));
        motorista.setTelefone(request.getParameter("telefone"));
        motorista.setCnhNumero(request.getParameter("cnhNumero"));
        motorista.setCnhCategoria(obterCategoriaCNH(request.getParameter("cnhCategoria")));
        motorista.setCnhValidade(obterData(request.getParameter("cnhValidade")));
        motorista.setTipoVinculo(obterTipoVinculo(request.getParameter("tipoVinculo")));
        motorista.setStatus(obterStatus(request.getParameter("status")));
        return motorista;
    }

    private void prepararFormulario(HttpServletRequest request, String titulo, String acao) {
        request.setAttribute("tituloFormulario", titulo);
        request.setAttribute("acaoFormulario", acao);
        request.setAttribute("categoriasCnh", CategoriaCNH.values());
        request.setAttribute("tiposVinculo", TipoVinculoMotorista.values());
        request.setAttribute("statusMotorista", StatusMotorista.values());
    }

    private void prepararFiltrosLista(HttpServletRequest request, String nomeFiltro, String cpfFiltro,
            String cnhFiltro, String categoriaCnhFiltro, String statusFiltro) {
        request.setAttribute("nomeFiltro", nomeFiltro);
        request.setAttribute("cpfFiltro", cpfFiltro);
        request.setAttribute("cnhFiltro", cnhFiltro);
        request.setAttribute("categoriaCnhFiltro", categoriaCnhFiltro);
        request.setAttribute("statusFiltro", statusFiltro);
        request.setAttribute("categoriasCnh", CategoriaCNH.values());
        request.setAttribute("statusMotorista", StatusMotorista.values());
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

        request.getSession().setAttribute("mensagemErro", "Seu perfil permite apenas visualizar motoristas.");
        response.sendRedirect(request.getContextPath() + "/motoristas");
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
        response.sendRedirect(request.getContextPath() + "/motoristas");
    }

    private void redirecionarComErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws IOException {

        request.getSession().setAttribute("mensagemErro", mensagem);
        response.sendRedirect(request.getContextPath() + "/motoristas");
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

    private LocalDate obterData(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private CategoriaCNH obterCategoriaCNH(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : CategoriaCNH.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private TipoVinculoMotorista obterTipoVinculo(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : TipoVinculoMotorista.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private StatusMotorista obterStatus(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : StatusMotorista.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
