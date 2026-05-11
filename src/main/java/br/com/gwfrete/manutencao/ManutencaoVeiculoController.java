package br.com.gwfrete.manutencao;

import br.com.gwfrete.manutencao.ManutencaoVeiculoBO;
import br.com.gwfrete.veiculo.VeiculoDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.manutencao.ManutencaoVeiculo;
import br.com.gwfrete.usuario.PerfilUsuario;
import br.com.gwfrete.manutencao.StatusManutencao;
import br.com.gwfrete.manutencao.TipoManutencao;
import br.com.gwfrete.usuario.Usuario;
import br.com.gwfrete.veiculo.Veiculo;

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

public class ManutencaoVeiculoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_LISTA = "/WEB-INF/views/manutencoes/listaManutencoes.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/manutencoes/formularioManutencao.jsp";

    private final ManutencaoVeiculoBO manutencaoVeiculoBO = new ManutencaoVeiculoBO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

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
                listarManutencoes(request, response);
            }
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            listarManutencoesComTratamento(request, response);
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
            salvarManutencao(request, response);
        } else if ("/atualizar".equals(rota)) {
            atualizarManutencao(request, response);
        } else if ("/cancelar".equals(rota) || "/inativar".equals(rota)) {
            inativarManutencao(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/manutencoes");
        }
    }

    private void listarManutencoes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        String statusFiltro = obterParametroFiltro(request, "status");
        request.setAttribute("manutencoes", manutencaoVeiculoBO.listarComFiltros(obterStatusManutencao(statusFiltro)));
        request.setAttribute("statusFiltro", statusFiltro);
        request.setAttribute("statusManutencao", StatusManutencao.values());
        request.setAttribute("podeGerenciarManutencoes", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarManutencoesComTratamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String statusFiltro = obterParametroFiltro(request, "status");
            request.setAttribute("manutencoes", manutencaoVeiculoBO.listarComFiltros(obterStatusManutencao(statusFiltro)));
            request.setAttribute("statusFiltro", statusFiltro);
            request.setAttribute("statusManutencao", StatusManutencao.values());
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }

        request.setAttribute("podeGerenciarManutencoes", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void abrirFormularioNova(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ManutencaoVeiculo manutencao = new ManutencaoVeiculo();
        manutencao.setStatus(StatusManutencao.AGENDADA);
        manutencao.setDataAgendada(LocalDate.now());

        request.setAttribute("manutencao", manutencao);
        prepararFormulario(request, "Nova manutenção", request.getContextPath() + "/manutencoes/salvar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        Long id = obterId(request);
        ManutencaoVeiculo manutencao = manutencaoVeiculoBO.buscarPorId(id);

        if (manutencao == null) {
            throw new CadastroException("Manutenção não encontrada.");
        }

        request.setAttribute("manutencao", manutencao);
        prepararFormulario(request, "Editar manutenção", request.getContextPath() + "/manutencoes/atualizar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void salvarManutencao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ManutencaoVeiculo manutencao = montarManutencaoFormulario(request);

        try {
            manutencaoVeiculoBO.salvar(manutencao);
            redirecionarComMensagem(request, response, "Manutenção cadastrada com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("manutencao", manutencao);
            prepararFormulario(request, "Nova manutenção", request.getContextPath() + "/manutencoes/salvar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void atualizarManutencao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ManutencaoVeiculo manutencao = montarManutencaoFormulario(request);
        manutencao.setId(obterId(request));

        try {
            manutencaoVeiculoBO.atualizar(manutencao);
            redirecionarComMensagem(request, response, "Manutenção atualizada com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("manutencao", manutencao);
            prepararFormulario(request, "Editar manutenção", request.getContextPath() + "/manutencoes/atualizar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void inativarManutencao(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            manutencaoVeiculoBO.inativar(obterId(request));
            redirecionarComMensagem(request, response, "Manutenção cancelada com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private ManutencaoVeiculo montarManutencaoFormulario(HttpServletRequest request) {
        ManutencaoVeiculo manutencao = new ManutencaoVeiculo();
        Long veiculoId = obterLong(request.getParameter("veiculoId"));

        if (veiculoId != null) {
            Veiculo veiculo = new Veiculo();
            veiculo.setId(veiculoId);
            manutencao.setVeiculo(veiculo);
        }

        manutencao.setTipo(obterTipoManutencao(request.getParameter("tipo")));
        manutencao.setStatus(obterStatusManutencao(request.getParameter("status")));
        manutencao.setDescricao(request.getParameter("descricao"));
        manutencao.setOficina(request.getParameter("oficina"));
        manutencao.setCusto(obterBigDecimal(request.getParameter("custo")));
        manutencao.setDataAgendada(obterData(request.getParameter("dataAgendada")));
        manutencao.setDataInicio(obterData(request.getParameter("dataInicio")));
        manutencao.setDataConclusao(obterData(request.getParameter("dataConclusao")));
        manutencao.setQuilometragem(obterLong(request.getParameter("quilometragem")));

        return manutencao;
    }

    private void prepararFormulario(HttpServletRequest request, String titulo, String acao) {
        request.setAttribute("tituloFormulario", titulo);
        request.setAttribute("acaoFormulario", acao);
        request.setAttribute("tiposManutencao", TipoManutencao.values());
        request.setAttribute("statusManutencao", StatusManutencao.values());

        try {
            request.setAttribute("veiculos", veiculoDAO.listarTodos());
        } catch (SQLException e) {
            request.setAttribute("veiculos", Collections.emptyList());
            request.setAttribute("mensagemErro", "Não foi possível carregar os veículos disponíveis para seleção.");
        }
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

        request.getSession().setAttribute("mensagemErro", "Seu perfil permite apenas visualizar manutenções.");
        response.sendRedirect(request.getContextPath() + "/manutencoes");
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
        response.sendRedirect(request.getContextPath() + "/manutencoes");
    }

    private void redirecionarComErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws IOException {

        request.getSession().setAttribute("mensagemErro", mensagem);
        response.sendRedirect(request.getContextPath() + "/manutencoes");
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

    private TipoManutencao obterTipoManutencao(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : TipoManutencao.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private StatusManutencao obterStatusManutencao(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : StatusManutencao.valueOf(valor);
        } catch (IllegalArgumentException e) {
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

    private BigDecimal obterBigDecimal(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : new BigDecimal(valor.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long obterLong(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : Long.valueOf(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
