package br.com.gwfrete.controller;

import br.com.gwfrete.bo.RelatorioBO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.RelatorioClienteDTO;
import br.com.gwfrete.model.RelatorioContratoDTO;
import br.com.gwfrete.model.RelatorioFinanceiroDTO;
import br.com.gwfrete.model.RelatorioFreteDTO;
import br.com.gwfrete.model.RelatorioManutencaoDTO;
import br.com.gwfrete.model.RelatorioMotoristaDTO;
import br.com.gwfrete.model.RelatorioOcorrenciaDTO;
import br.com.gwfrete.model.RelatorioVeiculoDTO;
import br.com.gwfrete.model.StatusContrato;
import br.com.gwfrete.model.StatusFatura;
import br.com.gwfrete.model.StatusFrete;
import br.com.gwfrete.model.StatusManutencao;
import br.com.gwfrete.model.StatusMotorista;
import br.com.gwfrete.model.StatusVeiculo;
import br.com.gwfrete.model.TipoCliente;
import br.com.gwfrete.model.TipoOcorrenciaFrete;
import br.com.gwfrete.model.Usuario;
import br.com.gwfrete.util.RelatorioContratoPdfUtil;
import br.com.gwfrete.util.RelatorioFretePdfUtil;
import br.com.gwfrete.util.RelatorioPdfUtil;
import net.sf.jasperreports.engine.JRException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

public class RelatorioController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_INDEX_RELATORIOS = "/WEB-INF/views/relatorios/indexRelatorios.jsp";
    private static final String VIEW_RELATORIO_FRETES = "/WEB-INF/views/relatorios/listaRelatorioFretes.jsp";
    private static final String VIEW_RELATORIO_CONTRATOS = "/WEB-INF/views/relatorios/listaRelatorioContratos.jsp";
    private static final String VIEW_RELATORIO_FINANCEIRO = "/WEB-INF/views/relatorios/listaRelatorioFinanceiro.jsp";
    private static final String VIEW_RELATORIO_MANUTENCOES = "/WEB-INF/views/relatorios/listaRelatorioManutencoes.jsp";
    private static final String VIEW_RELATORIO_OCORRENCIAS = "/WEB-INF/views/relatorios/listaRelatorioOcorrencias.jsp";
    private static final String VIEW_RELATORIO_CLIENTES = "/WEB-INF/views/relatorios/listaRelatorioClientes.jsp";
    private static final String VIEW_RELATORIO_MOTORISTAS = "/WEB-INF/views/relatorios/listaRelatorioMotoristas.jsp";
    private static final String VIEW_RELATORIO_VEICULOS = "/WEB-INF/views/relatorios/listaRelatorioVeiculos.jsp";

    private final RelatorioBO relatorioBO = new RelatorioBO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Usuario usuarioLogado = obterUsuarioLogado(request);
        if (!usuarioPodeAcessarRelatorios(usuarioLogado, request, response)) {
            return;
        }

        if (isRotaHub(request)) {
            request.getRequestDispatcher(VIEW_INDEX_RELATORIOS).forward(request, response);
            return;
        }

        if (isRotaContratos(request)) {
            processarRelatorioContratos(request, response);
            return;
        }

        if (isRotaFinanceiro(request)) {
            processarRelatorioFinanceiro(request, response);
            return;
        }

        if (isRotaManutencoes(request)) {
            processarRelatorioManutencoes(request, response);
            return;
        }

        if (isRotaOcorrencias(request)) {
            processarRelatorioOcorrencias(request, response);
            return;
        }

        if (isRotaClientes(request)) {
            processarRelatorioClientes(request, response);
            return;
        }

        if (isRotaMotoristas(request)) {
            processarRelatorioMotoristas(request, response);
            return;
        }

        if (isRotaVeiculos(request)) {
            processarRelatorioVeiculos(request, response);
            return;
        }

        processarRelatorioFretes(request, response);
    }

    private void processarRelatorioFretes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FiltroRelatorioFrete filtro = obterFiltro(request);

        if (isRotaPdf(request)) {
            exportarPdfFretes(response, filtro);
            return;
        }

        try {
            consumirMensagemSessao(request);
            prepararFiltroFretes(request, filtro);
            List<RelatorioFreteDTO> fretes = relatorioBO.gerarRelatorioFretes(filtro.dataInicial, filtro.dataFinal,
                    filtro.status, filtro.motorista, filtro.veiculo);
            request.setAttribute("fretes", fretes);
            prepararIndicadoresFretes(request, fretes);
        } catch (CadastroException e) {
            prepararFiltroFretes(request, filtro);
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("fretes", Collections.emptyList());
            prepararIndicadoresFretes(request, Collections.<RelatorioFreteDTO>emptyList());
        }

        request.getRequestDispatcher(VIEW_RELATORIO_FRETES).forward(request, response);
    }

    private void processarRelatorioContratos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FiltroRelatorioContrato filtro = obterFiltroContrato(request);

        if (isRotaPdf(request)) {
            exportarPdfContratos(response, filtro);
            return;
        }

        try {
            consumirMensagemSessao(request);
            prepararFiltroContratos(request, filtro);
            List<RelatorioContratoDTO> contratos = relatorioBO.gerarRelatorioContratos(filtro.dataInicial,
                    filtro.dataFinal, filtro.status, filtro.cliente);
            request.setAttribute("contratos", contratos);
            prepararIndicadoresContratos(request, contratos);
        } catch (CadastroException e) {
            prepararFiltroContratos(request, filtro);
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("contratos", Collections.emptyList());
            prepararIndicadoresContratos(request, Collections.<RelatorioContratoDTO>emptyList());
        }

        request.getRequestDispatcher(VIEW_RELATORIO_CONTRATOS).forward(request, response);
    }

    private void processarRelatorioFinanceiro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<RelatorioFinanceiroDTO> dados = relatorioBO.gerarRelatorioFinanceiro();
            if (isRotaPdf(request)) {
                exportarPdf(response, RelatorioPdfUtil.gerarPdf("report/relatorio_financeiro.jrxml", dados),
                        "relatorio-financeiro.pdf");
                return;
            }
            request.setAttribute("faturas", dados);
            prepararIndicadoresFinanceiro(request, dados);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("faturas", Collections.emptyList());
            prepararIndicadoresFinanceiro(request, Collections.<RelatorioFinanceiroDTO>emptyList());
        } catch (JRException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Não foi possível gerar o PDF do relatório financeiro.");
            return;
        }
        request.getRequestDispatcher(VIEW_RELATORIO_FINANCEIRO).forward(request, response);
    }

    private void processarRelatorioManutencoes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<RelatorioManutencaoDTO> dados = relatorioBO.gerarRelatorioManutencoes();
            if (isRotaPdf(request)) {
                exportarPdf(response, RelatorioPdfUtil.gerarPdf("report/relatorio_manutencoes.jrxml", dados),
                        "relatorio-manutencoes.pdf");
                return;
            }
            request.setAttribute("manutencoes", dados);
            prepararIndicadoresManutencoes(request, dados);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("manutencoes", Collections.emptyList());
            prepararIndicadoresManutencoes(request, Collections.<RelatorioManutencaoDTO>emptyList());
        } catch (JRException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Não foi possível gerar o PDF do relatório de manutenções.");
            return;
        }
        request.getRequestDispatcher(VIEW_RELATORIO_MANUTENCOES).forward(request, response);
    }

    private void processarRelatorioOcorrencias(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<RelatorioOcorrenciaDTO> dados = relatorioBO.gerarRelatorioOcorrencias();
            if (isRotaPdf(request)) {
                exportarPdf(response, RelatorioPdfUtil.gerarPdf("report/relatorio_ocorrencias.jrxml", dados),
                        "relatorio-ocorrencias.pdf");
                return;
            }
            request.setAttribute("ocorrencias", dados);
            prepararIndicadoresOcorrencias(request, dados);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("ocorrencias", Collections.emptyList());
            prepararIndicadoresOcorrencias(request, Collections.<RelatorioOcorrenciaDTO>emptyList());
        } catch (JRException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Não foi possível gerar o PDF do relatório de ocorrências.");
            return;
        }
        request.getRequestDispatcher(VIEW_RELATORIO_OCORRENCIAS).forward(request, response);
    }

    private void processarRelatorioClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<RelatorioClienteDTO> dados = relatorioBO.gerarRelatorioClientes();
            if (isRotaPdf(request)) {
                exportarPdf(response, RelatorioPdfUtil.gerarPdf("report/relatorio_clientes.jrxml", dados),
                        "relatorio-clientes.pdf");
                return;
            }
            request.setAttribute("clientes", dados);
            prepararIndicadoresClientes(request, dados);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("clientes", Collections.emptyList());
            prepararIndicadoresClientes(request, Collections.<RelatorioClienteDTO>emptyList());
        } catch (JRException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Não foi possível gerar o PDF do relatório de clientes.");
            return;
        }
        request.getRequestDispatcher(VIEW_RELATORIO_CLIENTES).forward(request, response);
    }

    private void processarRelatorioMotoristas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<RelatorioMotoristaDTO> dados = relatorioBO.gerarRelatorioMotoristas();
            if (isRotaPdf(request)) {
                exportarPdf(response, RelatorioPdfUtil.gerarPdf("report/relatorio_motoristas.jrxml", dados),
                        "relatorio-motoristas.pdf");
                return;
            }
            request.setAttribute("motoristas", dados);
            prepararIndicadoresMotoristas(request, dados);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("motoristas", Collections.emptyList());
            prepararIndicadoresMotoristas(request, Collections.<RelatorioMotoristaDTO>emptyList());
        } catch (JRException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Não foi possível gerar o PDF do relatório de motoristas.");
            return;
        }
        request.getRequestDispatcher(VIEW_RELATORIO_MOTORISTAS).forward(request, response);
    }

    private void processarRelatorioVeiculos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<RelatorioVeiculoDTO> dados = relatorioBO.gerarRelatorioVeiculos();
            if (isRotaPdf(request)) {
                exportarPdf(response, RelatorioPdfUtil.gerarPdf("report/relatorio_veiculos.jrxml", dados),
                        "relatorio-veiculos.pdf");
                return;
            }
            request.setAttribute("veiculos", dados);
            prepararIndicadoresVeiculos(request, dados);
        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("veiculos", Collections.emptyList());
            prepararIndicadoresVeiculos(request, Collections.<RelatorioVeiculoDTO>emptyList());
        } catch (JRException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Não foi possível gerar o PDF do relatório de veículos.");
            return;
        }
        request.getRequestDispatcher(VIEW_RELATORIO_VEICULOS).forward(request, response);
    }

    private void exportarPdfFretes(HttpServletResponse response, FiltroRelatorioFrete filtro) throws IOException {
        try {
            List<RelatorioFreteDTO> fretes = relatorioBO.gerarRelatorioFretes(filtro.dataInicial, filtro.dataFinal,
                    filtro.status, filtro.motorista, filtro.veiculo);
            byte[] pdf = RelatorioFretePdfUtil.gerarPdf(fretes);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"relatorio-operacional-fretes.pdf\"");
            response.setContentLength(pdf.length);
            response.getOutputStream().write(pdf);
            response.getOutputStream().flush();
        } catch (CadastroException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (JRException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Não foi possível gerar o PDF do relatório de fretes.");
        }
    }

    private void exportarPdfContratos(HttpServletResponse response, FiltroRelatorioContrato filtro) throws IOException {
        try {
            List<RelatorioContratoDTO> contratos = relatorioBO.gerarRelatorioContratos(filtro.dataInicial,
                    filtro.dataFinal, filtro.status, filtro.cliente);
            byte[] pdf = RelatorioContratoPdfUtil.gerarPdf(contratos);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"relatorio-gerencial-contratos.pdf\"");
            response.setContentLength(pdf.length);
            response.getOutputStream().write(pdf);
            response.getOutputStream().flush();
        } catch (CadastroException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (JRException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Não foi possível gerar o PDF do relatório de contratos.");
        }
    }

    private void exportarPdf(HttpServletResponse response, byte[] pdf, String nomeArquivo) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"" + nomeArquivo + "\"");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }

    private void prepararFiltroFretes(HttpServletRequest request, FiltroRelatorioFrete filtro) {
        request.setAttribute("dataInicialFiltro", filtro.dataInicialTexto);
        request.setAttribute("dataFinalFiltro", filtro.dataFinalTexto);
        request.setAttribute("statusFiltro", filtro.statusTexto);
        request.setAttribute("motoristaFiltro", filtro.motorista);
        request.setAttribute("veiculoFiltro", filtro.veiculo);
        request.setAttribute("queryStringFiltros", montarQueryStringFretes(filtro));
    }

    private void prepararFiltroContratos(HttpServletRequest request, FiltroRelatorioContrato filtro) {
        request.setAttribute("clienteFiltro", filtro.cliente);
        request.setAttribute("statusFiltro", filtro.statusTexto);
        request.setAttribute("dataInicialFiltro", filtro.dataInicialTexto);
        request.setAttribute("dataFinalFiltro", filtro.dataFinalTexto);
        request.setAttribute("queryStringFiltros", montarQueryStringContratos(filtro));
        request.setAttribute("statusContrato", StatusContrato.values());
    }

    private FiltroRelatorioFrete obterFiltro(HttpServletRequest request) {
        FiltroRelatorioFrete filtro = new FiltroRelatorioFrete();
        filtro.dataInicialTexto = normalizar(request.getParameter("dataInicial"));
        filtro.dataFinalTexto = normalizar(request.getParameter("dataFinal"));
        filtro.statusTexto = normalizar(request.getParameter("status"));
        filtro.motorista = normalizar(request.getParameter("motorista"));
        filtro.veiculo = normalizar(request.getParameter("veiculo"));
        filtro.dataInicial = parseData(filtro.dataInicialTexto);
        filtro.dataFinal = parseData(filtro.dataFinalTexto);
        filtro.status = parseStatus(filtro.statusTexto);
        return filtro;
    }

    private FiltroRelatorioContrato obterFiltroContrato(HttpServletRequest request) {
        FiltroRelatorioContrato filtro = new FiltroRelatorioContrato();
        filtro.cliente = normalizar(request.getParameter("cliente"));
        filtro.statusTexto = normalizar(request.getParameter("status"));
        filtro.dataInicialTexto = normalizar(request.getParameter("dataInicial"));
        filtro.dataFinalTexto = normalizar(request.getParameter("dataFinal"));
        filtro.status = parseStatusContrato(filtro.statusTexto);
        filtro.dataInicial = parseData(filtro.dataInicialTexto);
        filtro.dataFinal = parseData(filtro.dataFinalTexto);
        return filtro;
    }

    private LocalDate parseData(String valor) {
        if (valor == null || valor.isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private StatusFrete parseStatus(String valor) {
        if (valor == null || valor.isEmpty()) {
            return null;
        }

        try {
            return StatusFrete.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private StatusContrato parseStatusContrato(String valor) {
        if (valor == null || valor.isEmpty()) {
            return null;
        }

        try {
            return StatusContrato.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.trim();
    }

    private String montarQueryStringFretes(FiltroRelatorioFrete filtro) {
        StringBuilder queryString = new StringBuilder();
        adicionarParametro(queryString, "dataInicial", filtro.dataInicialTexto);
        adicionarParametro(queryString, "dataFinal", filtro.dataFinalTexto);
        adicionarParametro(queryString, "status", filtro.statusTexto);
        adicionarParametro(queryString, "motorista", filtro.motorista);
        adicionarParametro(queryString, "veiculo", filtro.veiculo);
        return queryString.toString();
    }

    private String montarQueryStringContratos(FiltroRelatorioContrato filtro) {
        StringBuilder queryString = new StringBuilder();
        adicionarParametro(queryString, "cliente", filtro.cliente);
        adicionarParametro(queryString, "status", filtro.statusTexto);
        adicionarParametro(queryString, "dataInicial", filtro.dataInicialTexto);
        adicionarParametro(queryString, "dataFinal", filtro.dataFinalTexto);
        return queryString.toString();
    }

    private void adicionarParametro(StringBuilder queryString, String nome, String valor) {
        if (valor == null || valor.isEmpty()) {
            return;
        }

        if (queryString.length() == 0) {
            queryString.append('?');
        } else {
            queryString.append('&');
        }

        try {
            queryString.append(URLEncoder.encode(nome, "UTF-8"))
                    .append('=')
                    .append(URLEncoder.encode(valor, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 não suportado.", e);
        }
    }

    private void prepararIndicadoresFretes(HttpServletRequest request, List<RelatorioFreteDTO> fretes) {
        int totalFretes = 0;
        int totalEntregues = 0;
        int totalEmTransito = 0;
        int totalCancelados = 0;

        for (RelatorioFreteDTO frete : fretes) {
            totalFretes++;

            if (frete.getStatus() == StatusFrete.ENTREGUE) {
                totalEntregues++;
            } else if (frete.getStatus() == StatusFrete.EM_TRANSITO) {
                totalEmTransito++;
            } else if (frete.getStatus() == StatusFrete.CANCELADO) {
                totalCancelados++;
            }
        }

        request.setAttribute("totalFretes", totalFretes);
        request.setAttribute("totalEntregues", totalEntregues);
        request.setAttribute("totalEmTransito", totalEmTransito);
        request.setAttribute("totalCancelados", totalCancelados);
    }

    private void prepararIndicadoresContratos(HttpServletRequest request, List<RelatorioContratoDTO> contratos) {
        int totalContratos = 0;
        int totalAtivos = 0;
        int totalEncerrados = 0;
        BigDecimal valorMensalTotal = BigDecimal.ZERO;

        for (RelatorioContratoDTO contrato : contratos) {
            totalContratos++;

            if (contrato.getStatus() == StatusContrato.ATIVO) {
                totalAtivos++;
            } else if (contrato.getStatus() == StatusContrato.ENCERRADO) {
                totalEncerrados++;
            }

            if (contrato.getValorMensal() != null) {
                valorMensalTotal = valorMensalTotal.add(contrato.getValorMensal());
            }
        }

        request.setAttribute("totalContratos", totalContratos);
        request.setAttribute("totalAtivos", totalAtivos);
        request.setAttribute("totalEncerrados", totalEncerrados);
        request.setAttribute("valorMensalTotal", valorMensalTotal);
    }

    private void prepararIndicadoresFinanceiro(HttpServletRequest request, List<RelatorioFinanceiroDTO> faturas) {
        int total = 0;
        int pagas = 0;
        int pendentes = 0;
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (RelatorioFinanceiroDTO fatura : faturas) {
            total++;
            if (fatura.getStatus() == StatusFatura.PAGO) pagas++;
            if (fatura.getStatus() == StatusFatura.PENDENTE) pendentes++;
            if (fatura.getValor() != null) valorTotal = valorTotal.add(fatura.getValor());
        }
        request.setAttribute("totalFaturas", total);
        request.setAttribute("faturasPagas", pagas);
        request.setAttribute("faturasPendentes", pendentes);
        request.setAttribute("valorTotal", valorTotal);
    }

    private void prepararIndicadoresManutencoes(HttpServletRequest request, List<RelatorioManutencaoDTO> manutencoes) {
        int total = 0;
        int emAndamento = 0;
        int concluidas = 0;
        BigDecimal custoTotal = BigDecimal.ZERO;
        for (RelatorioManutencaoDTO manutencao : manutencoes) {
            total++;
            if (manutencao.getStatus() == StatusManutencao.EM_ANDAMENTO) emAndamento++;
            if (manutencao.getStatus() == StatusManutencao.CONCLUIDA) concluidas++;
            if (manutencao.getCusto() != null) custoTotal = custoTotal.add(manutencao.getCusto());
        }
        request.setAttribute("totalManutencoes", total);
        request.setAttribute("manutencoesEmAndamento", emAndamento);
        request.setAttribute("manutencoesConcluidas", concluidas);
        request.setAttribute("custoTotal", custoTotal);
    }

    private void prepararIndicadoresOcorrencias(HttpServletRequest request, List<RelatorioOcorrenciaDTO> ocorrencias) {
        int total = 0;
        int entregas = 0;
        int avarias = 0;
        int extravios = 0;
        for (RelatorioOcorrenciaDTO ocorrencia : ocorrencias) {
            total++;
            if (ocorrencia.getTipo() == TipoOcorrenciaFrete.ENTREGA_REALIZADA) entregas++;
            if (ocorrencia.getTipo() == TipoOcorrenciaFrete.AVARIA) avarias++;
            if (ocorrencia.getTipo() == TipoOcorrenciaFrete.EXTRAVIO) extravios++;
        }
        request.setAttribute("totalOcorrencias", total);
        request.setAttribute("entregasRealizadas", entregas);
        request.setAttribute("avarias", avarias);
        request.setAttribute("extravios", extravios);
    }

    private void prepararIndicadoresClientes(HttpServletRequest request, List<RelatorioClienteDTO> clientes) {
        int total = 0;
        int pessoasFisicas = 0;
        int pessoasJuridicas = 0;
        int ativos = 0;
        for (RelatorioClienteDTO cliente : clientes) {
            total++;
            if (cliente.getTipo() == TipoCliente.PESSOA_FISICA) pessoasFisicas++;
            if (cliente.getTipo() == TipoCliente.PESSOA_JURIDICA) pessoasJuridicas++;
            if (Boolean.TRUE.equals(cliente.getStatus())) ativos++;
        }
        request.setAttribute("totalClientes", total);
        request.setAttribute("pessoasFisicas", pessoasFisicas);
        request.setAttribute("pessoasJuridicas", pessoasJuridicas);
        request.setAttribute("clientesAtivos", ativos);
    }

    private void prepararIndicadoresMotoristas(HttpServletRequest request, List<RelatorioMotoristaDTO> motoristas) {
        int total = 0;
        int ativos = 0;
        int suspensos = 0;
        int cnhsVencidas = 0;
        LocalDate hoje = LocalDate.now();
        for (RelatorioMotoristaDTO motorista : motoristas) {
            total++;
            if (motorista.getStatus() == StatusMotorista.ATIVO) ativos++;
            if (motorista.getStatus() == StatusMotorista.SUSPENSO) suspensos++;
            if (motorista.getValidadeCnh() != null && motorista.getValidadeCnh().isBefore(hoje)) cnhsVencidas++;
        }
        request.setAttribute("totalMotoristas", total);
        request.setAttribute("motoristasAtivos", ativos);
        request.setAttribute("motoristasSuspensos", suspensos);
        request.setAttribute("cnhsVencidas", cnhsVencidas);
    }

    private void prepararIndicadoresVeiculos(HttpServletRequest request, List<RelatorioVeiculoDTO> veiculos) {
        int total = 0;
        int disponiveis = 0;
        int emManutencao = 0;
        int inativos = 0;
        for (RelatorioVeiculoDTO veiculo : veiculos) {
            total++;
            if (veiculo.getStatus() == StatusVeiculo.DISPONIVEL) disponiveis++;
            if (veiculo.getStatus() == StatusVeiculo.MANUTENCAO) emManutencao++;
            if (veiculo.getStatus() == StatusVeiculo.INATIVO) inativos++;
        }
        request.setAttribute("totalVeiculos", total);
        request.setAttribute("veiculosDisponiveis", disponiveis);
        request.setAttribute("veiculosManutencao", emManutencao);
        request.setAttribute("veiculosInativos", inativos);
    }

    private boolean usuarioPodeAcessarRelatorios(Usuario usuarioLogado, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        if (!usuarioLogado.possuiPerfil(PerfilUsuario.ADMIN)
                && !usuarioLogado.possuiPerfil(PerfilUsuario.GESTOR)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return false;
        }

        return true;
    }

    private void consumirMensagemSessao(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        Object mensagemErro = session.getAttribute("mensagemErro");
        if (mensagemErro != null) {
            request.setAttribute("mensagemErro", mensagemErro);
            session.removeAttribute("mensagemErro");
        }
    }

    private boolean isRotaPdf(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath != null && servletPath.endsWith("/pdf");
    }

    private boolean isRotaHub(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return "/relatorios".equals(servletPath);
    }

    private boolean isRotaContratos(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath != null && servletPath.startsWith("/relatorios/contratos");
    }

    private boolean isRotaFinanceiro(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath != null && servletPath.startsWith("/relatorios/financeiro");
    }

    private boolean isRotaManutencoes(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath != null && servletPath.startsWith("/relatorios/manutencoes");
    }

    private boolean isRotaOcorrencias(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath != null && servletPath.startsWith("/relatorios/ocorrencias");
    }

    private boolean isRotaClientes(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath != null && servletPath.startsWith("/relatorios/clientes");
    }

    private boolean isRotaMotoristas(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath != null && servletPath.startsWith("/relatorios/motoristas");
    }

    private boolean isRotaVeiculos(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath != null && servletPath.startsWith("/relatorios/veiculos");
    }

    private Usuario obterUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Usuario) session.getAttribute("usuarioLogado");
    }

    private static class FiltroRelatorioFrete {
        private String dataInicialTexto;
        private String dataFinalTexto;
        private String statusTexto;
        private String motorista;
        private String veiculo;
        private LocalDate dataInicial;
        private LocalDate dataFinal;
        private StatusFrete status;
    }

    private static class FiltroRelatorioContrato {
        private String cliente;
        private String statusTexto;
        private String dataInicialTexto;
        private String dataFinalTexto;
        private StatusContrato status;
        private LocalDate dataInicial;
        private LocalDate dataFinal;
    }
}
