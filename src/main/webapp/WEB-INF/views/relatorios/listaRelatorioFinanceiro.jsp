<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Relatório Financeiro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510-reports-full">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-ui"></script>
</head>
<body class="theme-dark">
    <fmt:setLocale value="pt_BR"/>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp"><jsp:param name="ativo" value="relatorios" /></jsp:include>
        <section class="app-content">
            <header class="page-header">
                <div><span class="page-kicker">Relatórios gerenciais</span><h1>Relatório financeiro</h1><p>Faturas, vencimentos, pagamentos e valores financeiros da operação.</p></div>
                <div class="page-actions"><jsp:include page="/WEB-INF/views/includes/header.jsp" /><a class="button button-secondary" href="${pageContext.request.contextPath}/relatorios">Central de relatórios</a><a class="button button-secondary" href="${pageContext.request.contextPath}/financeiro">Financeiro</a></div>
            </header>
            <c:if test="${not empty mensagemErro}"><p class="message message-error" role="alert">${mensagemErro}</p></c:if>
            <section class="content-card filter-panel" aria-label="Ações do relatório financeiro">
                <div class="filter-panel-header"><div><span class="summary-label">Ações</span><h2>Relatório financeiro</h2></div><p>Gere a visão atual ou exporte os dados em PDF.</p></div>
                <div class="report-filters-actions"><a class="button button-primary" href="${pageContext.request.contextPath}/relatorios/financeiro">Gerar relatório</a><a class="button button-secondary" href="${pageContext.request.contextPath}/relatorios/financeiro/pdf" target="_blank" rel="noopener">Exportar PDF</a></div>
            </section>
            <section class="summary-grid" aria-label="Indicadores financeiros">
                <article class="summary-card"><span class="summary-label">Total faturas</span><strong>${totalFaturas}</strong><small>Registros financeiros</small></article>
                <article class="summary-card"><span class="summary-label">Faturas pagas</span><strong>${faturasPagas}</strong><small>Liquidadas</small></article>
                <article class="summary-card"><span class="summary-label">Faturas pendentes</span><strong>${faturasPendentes}</strong><small>Aguardando pagamento</small></article>
                <article class="summary-card"><span class="summary-label">Valor total</span><strong><fmt:formatNumber value="${valorTotal}" type="currency"/></strong><small>Valor faturado</small></article>
            </section>
            <section class="content-card" aria-label="Tabela financeira"><div class="table-wrap"><table class="data-table report-table"><thead><tr><th>Número</th><th>Cliente</th><th>Frete</th><th>Valor</th><th>Emissão</th><th>Vencimento</th><th>Pagamento</th><th>Status</th></tr></thead><tbody>
                <c:forEach var="fatura" items="${faturas}"><tr><td><strong class="document-code">${fatura.numero}</strong></td><td>${fatura.cliente}</td><td>${fatura.frete}</td><td class="text-muted"><fmt:formatNumber value="${fatura.valor}" type="currency"/></td><td class="text-muted"><fmt:formatDate value="${fatura.dataEmissaoFormatada}" pattern="dd/MM/yyyy"/></td><td class="text-muted"><fmt:formatDate value="${fatura.dataVencimentoFormatada}" pattern="dd/MM/yyyy"/></td><td class="text-muted"><c:choose><c:when test="${not empty fatura.dataPagamentoFormatada}"><fmt:formatDate value="${fatura.dataPagamentoFormatada}" pattern="dd/MM/yyyy"/></c:when><c:otherwise>-</c:otherwise></c:choose></td><td><span class="badge badge-fatura-${fatura.status.name().toLowerCase()}">${fatura.status.descricao}</span></td></tr></c:forEach>
                <c:if test="${empty faturas}"><tr><td colspan="8"><div class="empty-state">Nenhuma fatura disponível para o relatório.</div></td></tr></c:if>
            </tbody></table></div></section>
        </section>
    </main>
</body>
</html>
