package br.com.gwfrete.frete;

import br.com.gwfrete.cliente.Cliente;
import br.com.gwfrete.cliente.ClienteDAO;
import br.com.gwfrete.frete.FreteBO;
import br.com.gwfrete.motorista.MotoristaDAO;
import br.com.gwfrete.veiculo.VeiculoDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.frete.Frete;
import br.com.gwfrete.motorista.Motorista;
import br.com.gwfrete.usuario.PerfilUsuario;
import br.com.gwfrete.frete.StatusFrete;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class FreteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_LISTA = "/WEB-INF/views/fretes/listaFretes.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/fretes/formularioFrete.jsp";

    private final FreteBO freteBO = new FreteBO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final MotoristaDAO motoristaDAO = new MotoristaDAO();
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
                listarFretes(request, response);
            }
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            listarFretesComTratamento(request, response);
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
            salvarFrete(request, response);
        } else if ("/atualizar".equals(rota)) {
            atualizarFrete(request, response);
        } else if ("/cancelar".equals(rota)) {
            cancelarFrete(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/fretes");
        }
    }

    private void listarFretes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        consumirMensagemSessao(request);
        String codigoFiltro = obterParametroFiltro(request, "codigo");
        String origemFiltro = obterParametroFiltro(request, "origem");
        String destinoFiltro = obterParametroFiltro(request, "destino");
        String motoristaFiltro = obterParametroFiltro(request, "motorista");
        String veiculoFiltro = obterParametroFiltro(request, "veiculo");
        String statusFiltro = obterParametroFiltro(request, "status");

        request.setAttribute("fretes", freteBO.listarComFiltros(codigoFiltro, origemFiltro, destinoFiltro,
                motoristaFiltro, veiculoFiltro, obterStatus(statusFiltro)));
        request.setAttribute("podeGerenciarFretes", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        prepararFiltrosLista(request, codigoFiltro, origemFiltro, destinoFiltro, motoristaFiltro, veiculoFiltro,
                statusFiltro);
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void listarFretesComTratamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String codigoFiltro = obterParametroFiltro(request, "codigo");
            String origemFiltro = obterParametroFiltro(request, "origem");
            String destinoFiltro = obterParametroFiltro(request, "destino");
            String motoristaFiltro = obterParametroFiltro(request, "motorista");
            String veiculoFiltro = obterParametroFiltro(request, "veiculo");
            String statusFiltro = obterParametroFiltro(request, "status");

            request.setAttribute("fretes", freteBO.listarComFiltros(codigoFiltro, origemFiltro, destinoFiltro,
                    motoristaFiltro, veiculoFiltro, obterStatus(statusFiltro)));
            prepararFiltrosLista(request, codigoFiltro, origemFiltro, destinoFiltro, motoristaFiltro, veiculoFiltro,
                    statusFiltro);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
        }

        request.setAttribute("podeGerenciarFretes", usuarioPodeGerenciar(obterUsuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void prepararFiltrosLista(HttpServletRequest request, String codigoFiltro, String origemFiltro,
            String destinoFiltro, String motoristaFiltro, String veiculoFiltro, String statusFiltro) {
        request.setAttribute("codigoFiltro", codigoFiltro);
        request.setAttribute("origemFiltro", origemFiltro);
        request.setAttribute("destinoFiltro", destinoFiltro);
        request.setAttribute("motoristaFiltro", motoristaFiltro);
        request.setAttribute("veiculoFiltro", veiculoFiltro);
        request.setAttribute("statusFiltro", statusFiltro);
        request.setAttribute("statusFrete", StatusFrete.values());
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        Frete frete = new Frete();
        frete.setCodigo(freteBO.gerarProximoCodigo());
        frete.setStatus(StatusFrete.AGENDADO);
        frete.setRemetente(new Cliente());
        frete.setDestinatario(new Cliente());
        frete.setMotorista(new Motorista());
        frete.setVeiculo(new Veiculo());

        request.setAttribute("frete", frete);
        prepararFormulario(request, "Novo frete", request.getContextPath() + "/fretes/salvar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CadastroException {

        Long id = obterId(request);
        Frete frete = freteBO.buscarPorId(id);

        if (frete == null) {
            throw new CadastroException("Frete não encontrado.");
        }

        request.setAttribute("frete", frete);
        prepararFormulario(request, "Editar frete", request.getContextPath() + "/fretes/atualizar");
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void salvarFrete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Frete frete = montarFreteFormulario(request);

        try {
            freteBO.salvar(frete);
            redirecionarComMensagem(request, response, "Frete cadastrado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("frete", frete);
            prepararFormularioComTratamento(request, "Novo frete", request.getContextPath() + "/fretes/salvar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void atualizarFrete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Frete frete = montarFreteFormulario(request);
        frete.setId(obterId(request));

        try {
            freteBO.atualizar(frete);
            redirecionarComMensagem(request, response, "Frete atualizado com sucesso.");
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("frete", frete);
            prepararFormularioComTratamento(request, "Editar frete", request.getContextPath() + "/fretes/atualizar");
            request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
        }
    }

    private void cancelarFrete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            freteBO.inativar(obterId(request));
            redirecionarComMensagem(request, response, "Frete cancelado com sucesso.");
        } catch (CadastroException e) {
            redirecionarComErro(request, response, e.getMessage());
        }
    }

    private Frete montarFreteFormulario(HttpServletRequest request) {
        Frete frete = new Frete();
        frete.setCodigo(request.getParameter("codigo"));
        frete.setRemetente(montarCliente(request.getParameter("remetenteId")));
        frete.setDestinatario(montarCliente(request.getParameter("destinatarioId")));
        frete.setOrigem(request.getParameter("origem"));
        frete.setDestino(request.getParameter("destino"));
        frete.setDescricaoCarga(request.getParameter("descricaoCarga"));
        frete.setPesoKg(obterBigDecimal(request.getParameter("pesoKg")));
        frete.setValorFrete(obterBigDecimal(request.getParameter("valorFrete")));
        frete.setDataSaida(obterDataHora(request.getParameter("dataSaida")));
        frete.setDataEntrega(obterDataHora(request.getParameter("dataEntrega")));
        frete.setStatus(obterStatus(request.getParameter("status")));
        frete.setMotorista(montarMotorista(request.getParameter("motoristaId")));
        frete.setVeiculo(montarVeiculo(request.getParameter("veiculoId")));
        return frete;
    }

    private Cliente montarCliente(String id) {
        Cliente cliente = new Cliente();
        cliente.setId(obterLong(id));
        return cliente;
    }

    private Motorista montarMotorista(String id) {
        Motorista motorista = new Motorista();
        motorista.setId(obterLong(id));
        return motorista;
    }

    private Veiculo montarVeiculo(String id) {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(obterLong(id));
        return veiculo;
    }

    private void prepararFormulario(HttpServletRequest request, String titulo, String acao) throws CadastroException {
        request.setAttribute("tituloFormulario", titulo);
        request.setAttribute("acaoFormulario", acao);
        request.setAttribute("statusFrete", StatusFrete.values());

        try {
            request.setAttribute("clientes", clienteDAO.listarTodos());
            request.setAttribute("motoristas", motoristaDAO.listarTodos());
            request.setAttribute("veiculos", veiculoDAO.listarTodos());
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível carregar clientes, motoristas e veículos para o formulário.");
        }
    }

    private void prepararFormularioComTratamento(HttpServletRequest request, String titulo, String acao) {
        try {
            prepararFormulario(request, titulo, acao);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
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

        request.getSession().setAttribute("mensagemErro", "Seu perfil permite apenas visualizar fretes.");
        response.sendRedirect(request.getContextPath() + "/fretes");
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
        response.sendRedirect(request.getContextPath() + "/fretes");
    }

    private void redirecionarComErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws IOException {

        request.getSession().setAttribute("mensagemErro", mensagem);
        response.sendRedirect(request.getContextPath() + "/fretes");
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
            if (valor == null || valor.trim().isEmpty()) {
                return null;
            }

            return new BigDecimal(valor.trim().replace(",", "."));
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

    private StatusFrete obterStatus(String valor) {
        try {
            return valor == null || valor.trim().isEmpty() ? null : StatusFrete.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
