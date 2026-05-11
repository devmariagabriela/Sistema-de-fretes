<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Relatório de Contratos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510-contratos-report">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-ui"></script>
</head>
<body class="theme-dark">
    <fmt:setLocale value="pt_BR"/>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="relatorios" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Relatórios gerenciais</span>
                    <h1>Relatório de contratos</h1>
                    <p>Consulta executiva de contratos por cliente, vigência, status e valor mensal.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/relatorios">Central de relatórios</a>
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/relatorios/fretes">Relatório de fretes</a>
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/contratos">Contratos</a>
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card filter-panel" aria-label="Filtros do relatório de contratos">
                <div class="filter-panel-header">
                    <div>
                        <span class="summary-label">Filtros analíticos</span>
                        <h2>Refinar relatório gerencial</h2>
                    </div>
                    <p>Combine cliente, status e período de início para acompanhar carteira e receita contratual.</p>
                </div>

                <form id="relatorio-contratos-filtros-form" class="report-filters-form" action="${pageContext.request.contextPath}/relatorios/contratos" method="get">
                    <div class="form-grid report-filters-grid report-contract-filters-grid">
                        <div class="form-field">
                            <label for="cliente">Cliente</label>
                            <input id="cliente" name="cliente" type="text" value="${clienteFiltro}" placeholder="Nome do cliente">
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="">Todos os status</option>
                                <c:forEach var="status" items="${statusContrato}">
                                    <option value="${status.name()}" ${statusFiltro == status.name() ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="dataInicial">Data inicial</label>
                            <input id="dataInicial" name="dataInicial" type="date" value="${dataInicialFiltro}">
                        </div>

                        <div class="form-field">
                            <label for="dataFinal">Data final</label>
                            <input id="dataFinal" name="dataFinal" type="date" value="${dataFinalFiltro}">
                        </div>
                    </div>

                    <div class="report-filters-actions">
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/relatorios/contratos">Limpar filtros</a>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/relatorios/contratos/pdf${queryStringFiltros}" target="_blank" rel="noopener">Exportar PDF</a>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/relatorios/contratos/excel${queryStringFiltros}">Exportar Excel</a>
                    </div>
                </form>
            </section>

            <section class="summary-grid" aria-label="Indicadores do relatório de contratos">
                <article class="summary-card">
                    <span class="summary-label">Total contratos</span>
                    <strong>${totalContratos}</strong>
                    <small>Registros filtrados</small>
                </article>
                <article class="summary-card">
                    <span class="summary-label">Contratos ativos</span>
                    <strong>${totalAtivos}</strong>
                    <small>Carteira vigente</small>
                </article>
                <article class="summary-card">
                    <span class="summary-label">Contratos encerrados</span>
                    <strong>${totalEncerrados}</strong>
                    <small>Ciclos concluídos</small>
                </article>
                <article class="summary-card">
                    <span class="summary-label">Valor mensal total</span>
                    <strong><fmt:formatNumber value="${valorMensalTotal}" type="currency"/></strong>
                    <small>Receita mensal contratual</small>
                </article>
            </section>

            <section class="content-card" aria-label="Tabela do relatório de contratos">
                <div class="table-wrap">
                    <table class="data-table report-table">
                        <thead>
                            <tr>
                                <th>Número</th>
                                <th>Cliente</th>
                                <th>Valor mensal</th>
                                <th>Data início</th>
                                <th>Data fim</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="contrato" items="${contratos}">
                                <tr>
                                    <td><strong class="document-code">${contrato.numero}</strong></td>
                                    <td>${contrato.cliente}</td>
                                    <td class="text-muted">
                                        <fmt:formatNumber value="${contrato.valorMensal}" type="currency"/>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${contrato.dataInicioFormatada}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td class="text-muted">
                                        <c:choose>
                                            <c:when test="${not empty contrato.dataFimFormatada}">
                                                <fmt:formatDate value="${contrato.dataFimFormatada}" pattern="dd/MM/yyyy"/>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="badge badge-contrato-${contrato.status.name().toLowerCase()}">${contrato.status.descricao}</span>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty contratos}">
                                <tr>
                                    <td colspan="6">
                                        <div class="empty-state">Nenhum contrato disponível para o relatório.</div>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </section>
    </main>
</body>
</html>
