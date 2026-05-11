<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510-notif">
</head>
<body>
    <fmt:setLocale value="pt_BR"/>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="fretes" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Operação de fretes</span>
                    <h1>Fretes</h1>
                    <p>Monitoramento de rotas, cargas, veículos, motoristas e status do ciclo logístico.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                    <c:if test="${podeGerenciarFretes}">
                        <a class="button button-primary" href="${pageContext.request.contextPath}/fretes/novo">Novo frete</a>
                    </c:if>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="summary-grid" aria-label="Resumo operacional de fretes">
                <article class="summary-card">
                    <span class="summary-label">Fretes cadastrados</span>
                    <strong>${fn:length(fretes)}</strong>
                    <small>Operações registradas no TMS</small>
                </article>
                <article class="summary-card">
                    <span class="summary-label">Fluxo operacional</span>
                    <strong>6 status</strong>
                    <small>Agendamento, coleta, trânsito e entrega</small>
                </article>
                <article class="summary-card">
                    <span class="summary-label">Recursos vinculados</span>
                    <strong>Frota + motorista</strong>
                    <small>Fretes sempre ligados à operação</small>
                </article>
            </section>

            <section class="content-card" aria-label="Fretes cadastrados">
                <div class="table-wrap">
                    <table class="data-table freight-table">
                        <thead>
                            <tr>
                                <th>Código</th>
                                <th>Origem</th>
                                <th>Destino</th>
                                <th>Motorista</th>
                                <th>Veículo</th>
                                <th>Status</th>
                                <th>Data de saída</th>
                                <th>Data de entrega</th>
                                <th>Valor do frete</th>
                                <c:if test="${podeGerenciarFretes}">
                                    <th>Ações</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="frete" items="${fretes}">
                                <tr>
                                    <td>
                                        <strong class="freight-code">${frete.codigo}</strong>
                                    </td>
                                    <td>${frete.origem}</td>
                                    <td>${frete.destino}</td>
                                    <td class="text-muted">${frete.motorista.nome}</td>
                                    <td>
                                        <div class="stacked-cell">
                                            <span class="plate-code">${frete.veiculo.placa}</span>
                                            <small class="text-muted">${frete.veiculo.modelo}</small>
                                        </div>
                                    </td>
                                    <td>
                                        <span class="badge badge-frete-${frete.status.name().toLowerCase()}">${frete.status.descricao}</span>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${frete.dataSaidaFormatada}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${frete.dataEntregaFormatada}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatNumber value="${frete.valorFrete}" type="currency"/>
                                    </td>
                                    <c:if test="${podeGerenciarFretes}">
                                        <td>
                                            <div class="row-actions">
                                                <a class="button button-secondary" href="${pageContext.request.contextPath}/fretes/editar?id=${frete.id}">Editar</a>
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty fretes}">
                                <tr>
                                    <td colspan="${podeGerenciarFretes ? 10 : 9}">
                                        <div class="empty-state">Nenhum frete cadastrado.</div>
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
