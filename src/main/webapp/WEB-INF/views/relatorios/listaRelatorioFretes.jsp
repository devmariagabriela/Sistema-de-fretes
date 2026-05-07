<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Relatório de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <fmt:setLocale value="pt_BR"/>
    <main class="app-shell">
        <aside class="app-sidebar">
            <div class="app-brand">GW FRETE</div>
            <nav aria-label="Módulos principais">
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/usuarios">Usuários</a>
                <a href="${pageContext.request.contextPath}/motoristas">Motoristas</a>
                <a href="${pageContext.request.contextPath}/veiculos">Veículos</a>
                <a href="${pageContext.request.contextPath}/fretes">Fretes</a>
                <a href="${pageContext.request.contextPath}/ocorrencias-frete">Ocorrências</a>
                <a href="${pageContext.request.contextPath}/relatorios/fretes" class="active">Relatórios</a>
            </nav>
        </aside>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Relatórios operacionais</span>
                    <h1>Relatório de fretes</h1>
                    <p>Visão consolidada dos fretes, recursos alocados, status e valores operacionais.</p>
                </div>
                <div class="page-actions">
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/fretes">Fretes</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="summary-grid" aria-label="Indicadores do relatório de fretes">
                <article class="summary-card">
                    <span class="summary-label">Total fretes</span>
                    <strong>${totalFretes}</strong>
                    <small>Registros operacionais</small>
                </article>
                <article class="summary-card">
                    <span class="summary-label">Entregues</span>
                    <strong>${totalEntregues}</strong>
                    <small>Concluídos no ciclo logístico</small>
                </article>
                <article class="summary-card">
                    <span class="summary-label">Em trânsito</span>
                    <strong>${totalEmTransito}</strong>
                    <small>Operações em andamento</small>
                </article>
                <article class="summary-card">
                    <span class="summary-label">Cancelados</span>
                    <strong>${totalCancelados}</strong>
                    <small>Fretes interrompidos</small>
                </article>
            </section>

            <section class="content-card" aria-label="Tabela do relatório de fretes">
                <div class="table-wrap">
                    <table class="data-table report-table">
                        <thead>
                            <tr>
                                <th>Código</th>
                                <th>Origem</th>
                                <th>Destino</th>
                                <th>Motorista</th>
                                <th>Veículo</th>
                                <th>Status</th>
                                <th>Data saída</th>
                                <th>Data entrega</th>
                                <th>Valor frete</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="frete" items="${fretes}">
                                <tr>
                                    <td><strong class="freight-code">${frete.codigo}</strong></td>
                                    <td>${frete.origem}</td>
                                    <td>${frete.destino}</td>
                                    <td class="text-muted">${frete.motorista}</td>
                                    <td class="text-muted">${frete.veiculo}</td>
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
                                </tr>
                            </c:forEach>
                            <c:if test="${empty fretes}">
                                <tr>
                                    <td colspan="9">
                                        <div class="empty-state">Nenhum frete disponível para o relatório.</div>
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
