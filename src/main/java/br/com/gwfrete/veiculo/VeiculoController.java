package br.com.gwfrete.veiculo;

import br.com.gwfrete.veiculo.VeiculoBO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.usuario.PerfilUsuario;
import br.com.gwfrete.veiculo.StatusVeiculo;
import br.com.gwfrete.veiculo.TipoVeiculo;
import br.com.gwfrete.usuario.Usuario;
import br.com.gwfrete.veiculo.Veiculo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

public class VeiculoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_LISTA = "/WEB-INF/views/veiculos/listaVeiculos.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/veiculos/formularioVeiculo.jsp";

    private final VeiculoBO veiculoBO = new VeiculoBO();

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
                listarVeiculos(request, response);
            }
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            listarVeiculosComTratamento(request, response);
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
            salvarVeiculo(request, response);
        } else if ("/atualizar".equals(rota)) {
            atualizarVeiculo(request, response);
        } else if ("/inativar".equals(rota)) {
            inativarVeiculo(request, response);
        } else if ("/ativar".equals(rota)) {
            ativarVeiculo(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/veiculos");
        }
    }

    private void listarVeiculos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        String placaFiltro = obterParametroFiltro(request, "placa");
        String tipoFiltro = obterParametroFiltro(request, "tipo");
        String statusFiltro = obterParametroFiltro(request, "status");
        String modeloFiltro = obterParametroFiltro(request, "modelo");

        request.setAttribute("veiculos", veiculoBO.listarComFiltros(placaFiltro, obterTipo(tipoFiltro),
                obterStatus(statusFiltro), modeloFiltro));
        request.setAttribute("podeGerenciarVeiculos", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        prepararFiltrosLista(request, placaFiltro, tipoFiltro, statusFiltro, modeloFiltro);
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarVeiculosComTratamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String placaFiltro = obterParametroFiltro(request, "placa");
            String tipoFiltro = obterParametroFiltro(request, "tipo");
            String statusFiltro = obterParametroFiltro(request, "status");
            String modeloFiltro = obterParametroFiltro(request, "modelo");

            request.setAttribute("veiculos", veiculoBO.listarComFiltros(placaFiltro, obterTipo(tipoFiltro),
                    obterStatus(statusFiltro), modeloFiltro));
            prepararFiltrosLista(request, placaFiltro, tipoFiltro, statusFiltro, modeloFiltro);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }

        request.setAttribute("podeGerenciarVeiculos", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Veiculo veiculo = new Veiculo();
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        veiculo.setQuilometragem(0L);

        request.setAttribute("veiculo", veiculo);
        prepararFormulario(request, "Novo veículo", request.getContextPath() + "/veiculos/salvar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        Long id = obterId(request);
        Veiculo veiculo = veiculoBO.buscarPorId(id);

        if (veiculo == null) {
            throw new CadastroException("Veículo não encontrado.");
        }

        request.setAttribute("veiculo", veiculo);
        prepararFormulario(request, "Editar veículo", request.getContextPath() + "/veiculos/atualizar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void salvarVeiculo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Veiculo veiculo = montarVeiculoFormulario(request);

        try {
            veiculoBO.salvar(veiculo);
            redirecionarComMensagem(request, response, "Veículo cadastrado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("veiculo", veiculo);
            prepararFormulario(request, "Novo veículo", request.getContextPath() + "/veiculos/salvar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void atualizarVeiculo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Veiculo veiculo = montarVeiculoFormulario(request);
        veiculo.setId(obterId(request));

        try {
            veiculoBO.atualizar(veiculo);
            redirecionarComMensagem(request, response, "Veículo atualizado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("veiculo", veiculo);
            prepararFormulario(request, "Editar veículo", request.getContextPath() + "/veiculos/atualizar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void inativarVeiculo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            veiculoBO.inativar(obterId(request));
            redirecionarComMensagem(request, response, "Veículo inativado com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private void ativarVeiculo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            veiculoBO.ativar(obterId(request));
            redirecionarComMensagem(request, response, "Veículo ativado como disponível com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private Veiculo montarVeiculoFormulario(HttpServletRequest request) {
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(request.getParameter("placa"));
        veiculo.setModelo(request.getParameter("modelo"));
        veiculo.setMarca(request.getParameter("marca"));
        veiculo.setAno(obterInteger(request.getParameter("ano")));
        veiculo.setCapacidadeKg(obterBigDecimal(request.getParameter("capacidadeKg")));
        veiculo.setTipo(obterTipo(request.getParameter("tipo")));
        veiculo.setStatus(obterStatus(request.getParameter("status")));
        veiculo.setQuilometragem(obterLong(request.getParameter("quilometragem")));
        return veiculo;
    }

    private void prepararFormulario(HttpServletRequest request, String titulo, String acao) {
        request.setAttribute("tituloFormulario", titulo);
        request.setAttribute("acaoFormulario", acao);
        request.setAttribute("tiposVeiculo", TipoVeiculo.values());
        request.setAttribute("statusVeiculo", StatusVeiculo.values());
    }

    private void prepararFiltrosLista(HttpServletRequest request, String placaFiltro, String tipoFiltro,
            String statusFiltro, String modeloFiltro) {
        request.setAttribute("placaFiltro", placaFiltro);
        request.setAttribute("tipoFiltro", tipoFiltro);
        request.setAttribute("statusFiltro", statusFiltro);
        request.setAttribute("modeloFiltro", modeloFiltro);
        request.setAttribute("tiposVeiculo", TipoVeiculo.values());
        request.setAttribute("statusVeiculo", StatusVeiculo.values());
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

        request.getSession().setAttribute("mensagemErro", "Seu perfil permite apenas visualizar veículos.");
        response.sendRedirect(request.getContextPath() + "/veiculos");
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
        response.sendRedirect(request.getContextPath() + "/veiculos");
    }

    private void redirecionarComErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws IOException {

        request.getSession().setAttribute("mensagemErro", mensagem);
        response.sendRedirect(request.getContextPath() + "/veiculos");
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

    private Integer obterInteger(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : Integer.valueOf(valor);
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

    private TipoVeiculo obterTipo(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : TipoVeiculo.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private StatusVeiculo obterStatus(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : StatusVeiculo.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
