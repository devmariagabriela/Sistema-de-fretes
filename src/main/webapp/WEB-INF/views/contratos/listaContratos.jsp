<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Contratos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260511-voice-access">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260511-voice-access"></script>
</head>
<body class="theme-dark">
    <fmt:setLocale value="pt_BR"/>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="contratos" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Gestão comercial</span>
                    <h1>Contratos</h1>
                    <p>Controle de contratos firmados com clientes, vigência, valores e status.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <c:if test="${podeGerenciarContratos}">
                        <a class="button button-primary" href="${pageContext.request.contextPath}/contratos/novo">Novo contrato</a>
                    </c:if>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>
            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card filter-panel" aria-label="Filtros de contratos">
                <form class="report-filters-form" action="${pageContext.request.contextPath}/contratos" method="get">
                    <div class="form-grid report-filters-grid">
                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="">Ativos por padrão</option>
                                <c:forEach var="status" items="${statusContratos}">
                                    <option value="${status.name()}" ${statusFiltro == status.name() ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="report-filters-actions">
                        <button class="button button-primary" type="submit">Consultar</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/contratos">Limpar filtros</a>
                    </div>
                </form>
            </section>

            <section class="content-card" aria-label="Lista de contratos">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Número</th>
                                <th>Cliente</th>
                                <th>Valor mensal</th>
                                <th>Início</th>
                                <th>Fim</th>
                                <th>Reajuste</th>
                                <th>Status</th>
                                <c:if test="${podeGerenciarContratos}">
                                    <th>Ações</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="contrato" items="${contratos}">
                                <tr>
                                    <td><strong>${contrato.numero}</strong></td>
                                    <td>${contrato.cliente.nome}</td>
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
                                    <td class="text-muted">
                                        <c:choose>
                                            <c:when test="${not empty contrato.reajustePercentual}">
                                                <fmt:formatNumber value="${contrato.reajustePercentual}" pattern="#,##0.00"/>%
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="badge badge-status-${contrato.status.name().toLowerCase()}">${contrato.status.descricao}</span>
                                    </td>
                                    <c:if test="${podeGerenciarContratos}">
                                        <td>
                                            <div class="row-actions">
                                                <a class="button button-secondary" href="${pageContext.request.contextPath}/contratos/editar?id=${contrato.id}">Editar</a>
                                                <c:if test="${contrato.status.name() == 'ATIVO'}">
                                                    <button class="button button-secondary" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/contratos/suspender"
                                                            data-id="${contrato.id}"
                                                            data-title="Suspender contrato"
                                                            data-message="Deseja suspender o contrato ${contrato.numero}?"
                                                            data-submit="Suspender">Suspender</button>
                                                    <button class="button button-danger" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/contratos/encerrar"
                                                            data-id="${contrato.id}"
                                                            data-title="Encerrar contrato"
                                                            data-message="Deseja encerrar o contrato ${contrato.numero}?"
                                                            data-submit="Encerrar">Encerrar</button>
                                                    <button class="button button-danger" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/contratos/cancelar"
                                                            data-id="${contrato.id}"
                                                            data-title="Cancelar contrato"
                                                            data-message="Deseja cancelar o contrato ${contrato.numero}?"
                                                            data-submit="Cancelar">Cancelar</button>
                                                </c:if>
                                                <c:if test="${contrato.status.name() == 'SUSPENSO'}">
                                                    <button class="button button-danger" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/contratos/encerrar"
                                                            data-id="${contrato.id}"
                                                            data-title="Encerrar contrato"
                                                            data-message="Deseja encerrar o contrato ${contrato.numero}?"
                                                            data-submit="Encerrar">Encerrar</button>
                                                    <button class="button button-danger" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/contratos/cancelar"
                                                            data-id="${contrato.id}"
                                                            data-title="Cancelar contrato"
                                                            data-message="Deseja cancelar o contrato ${contrato.numero}?"
                                                            data-submit="Cancelar">Cancelar</button>
                                                </c:if>
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty contratos}">
                                <tr>
                                    <td colspan="${podeGerenciarContratos ? 8 : 7}">
                                        <div class="empty-state">Nenhum contrato cadastrado.</div>
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
