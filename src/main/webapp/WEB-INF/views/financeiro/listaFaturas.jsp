<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Financeiro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260511-voice-access">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260511-voice-access"></script>
</head>
<body class="theme-dark">
    <fmt:setLocale value="pt_BR"/>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="financeiro" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Contas a receber</span>
                    <h1>Financeiro</h1>
                    <p>Controle de faturas geradas a partir dos fretes e vinculadas aos clientes.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <c:if test="${podeGerenciarFaturas}">
                        <a class="button button-primary" href="${pageContext.request.contextPath}/financeiro/nova">Nova fatura</a>
                    </c:if>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>
            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card filter-panel" aria-label="Filtros de faturas">
                <form class="report-filters-form" action="${pageContext.request.contextPath}/financeiro" method="get">
                    <div class="form-grid report-filters-grid">
                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="">Não canceladas por padrão</option>
                                <c:forEach var="status" items="${statusFaturas}">
                                    <option value="${status.name()}" ${statusFiltro == status.name() ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="report-filters-actions">
                        <button class="button button-primary" type="submit">Consultar</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/financeiro">Limpar filtros</a>
                    </div>
                </form>
            </section>

            <section class="content-card" aria-label="Lista de faturas">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Número</th>
                                <th>Frete</th>
                                <th>Cliente</th>
                                <th>Valor</th>
                                <th>Emissão</th>
                                <th>Vencimento</th>
                                <th>Pagamento</th>
                                <th>Status</th>
                                <c:if test="${podeGerenciarFaturas}">
                                    <th>Ações</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="fatura" items="${faturas}">
                                <tr>
                                    <td><strong>${fatura.numero}</strong></td>
                                    <td class="text-muted">${fatura.frete.codigo}</td>
                                    <td>${fatura.cliente.nome}</td>
                                    <td class="text-muted">
                                        <fmt:formatNumber value="${fatura.valor}" type="currency"/>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${fatura.dataEmissaoFormatada}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${fatura.dataVencimentoFormatada}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${fatura.dataPagamentoFormatada}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td>
                                        <span class="badge badge-fatura-${fatura.status.name().toLowerCase()}">${fatura.status.descricao}</span>
                                    </td>
                                    <c:if test="${podeGerenciarFaturas}">
                                        <td>
                                            <div class="row-actions">
                                                <a class="button button-secondary" href="${pageContext.request.contextPath}/financeiro/editar?id=${fatura.id}">Editar</a>
                                                <c:if test="${fatura.status.name() != 'PAGO' && fatura.status.name() != 'CANCELADO'}">
                                                    <button class="button button-secondary" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/financeiro/pagar"
                                                            data-id="${fatura.id}"
                                                            data-title="Marcar fatura como paga"
                                                            data-message="Deseja marcar a fatura ${fatura.numero} como paga?"
                                                            data-submit="Marcar como pago">Marcar como Pago</button>
                                                    <button class="button button-danger" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/financeiro/cancelar"
                                                            data-id="${fatura.id}"
                                                            data-title="Cancelar fatura"
                                                            data-message="Deseja cancelar a fatura ${fatura.numero}?"
                                                            data-submit="Cancelar">Cancelar</button>
                                                </c:if>
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty faturas}">
                                <tr>
                                    <td colspan="${podeGerenciarFaturas ? 9 : 8}">
                                        <div class="empty-state">Nenhuma fatura cadastrada.</div>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </section>
    </main>
    <jsp:include page="/WEB-INF/views/includes/confirmacaoExclusao.jsp" />
</body>
</html>
