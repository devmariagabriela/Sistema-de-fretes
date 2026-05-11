<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Veículos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260511-voice-access">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260511-voice-access"></script>
</head>
<body class="theme-dark">
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="veiculos" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Gestão de frota</span>
                    <h1>Veículos</h1>
                    <p>Controle operacional de placas, capacidade, status de rota e disponibilidade da frota.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <c:if test="${podeGerenciarVeiculos}">
                        <a class="button button-primary" href="${pageContext.request.contextPath}/veiculos/novo">Novo veículo</a>
                    </c:if>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card filter-panel" aria-label="Filtros de veículos">
                <div class="filter-panel-header">
                    <div>
                        <span class="summary-label">Filtros</span>
                        <h2>Consultar veículos</h2>
                    </div>
                    <p>Localize veículos por placa, modelo, tipo ou status operacional.</p>
                </div>

                <form class="report-filters-form" action="${pageContext.request.contextPath}/veiculos" method="get">
                    <div class="form-grid report-filters-grid">
                        <div class="form-field">
                            <label for="placa">Placa</label>
                            <input id="placa" name="placa" type="text" value="${placaFiltro}" placeholder="ABC-1234">
                        </div>

                        <div class="form-field">
                            <label for="modelo">Modelo</label>
                            <input id="modelo" name="modelo" type="text" value="${modeloFiltro}" placeholder="Modelo">
                        </div>

                        <div class="form-field">
                            <label for="tipo">Tipo</label>
                            <select id="tipo" name="tipo">
                                <option value="">Todos os tipos</option>
                                <c:forEach var="tipo" items="${tiposVeiculo}">
                                    <option value="${tipo.name()}" ${tipoFiltro == tipo.name() ? 'selected' : ''}>${tipo.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="">Todos os status</option>
                                <c:forEach var="status" items="${statusVeiculo}">
                                    <option value="${status.name()}" ${statusFiltro == status.name() ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="report-filters-actions">
                        <button class="button button-primary" type="submit">Consultar</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/veiculos">Limpar filtros</a>
                    </div>
                </form>
            </section>

            <section class="content-card" aria-label="Veículos cadastrados">
                <div class="table-wrap">
                    <table class="data-table fleet-table">
                        <thead>
                            <tr>
                                <th>Placa</th>
                                <th>Modelo</th>
                                <th>Marca</th>
                                <th>Tipo</th>
                                <th>Status</th>
                                <th>Quilometragem</th>
                                <th>Capacidade</th>
                                <c:if test="${podeGerenciarVeiculos}">
                                    <th>Ações</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="veiculo" items="${veiculos}">
                                <tr>
                                    <td>
                                        <strong class="plate-code">${veiculo.placa}</strong>
                                    </td>
                                    <td>${veiculo.modelo}</td>
                                    <td class="text-muted">${veiculo.marca}</td>
                                    <td>
                                        <span class="badge badge-tipo-${veiculo.tipo.name().toLowerCase()}">${veiculo.tipo.descricao}</span>
                                    </td>
                                    <td>
                                        <span class="badge badge-status-${veiculo.status.name().toLowerCase()}">${veiculo.status.descricao}</span>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatNumber value="${veiculo.quilometragem}" pattern="#,##0"/> km
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatNumber value="${veiculo.capacidadeKg}" pattern="#,##0.00"/> kg
                                    </td>
                                    <c:if test="${podeGerenciarVeiculos}">
                                        <td>
                                            <div class="row-actions">
                                                <a class="button button-secondary" href="${pageContext.request.contextPath}/veiculos/editar?id=${veiculo.id}">Editar</a>
                                                <c:if test="${veiculo.status.name() != 'INATIVO'}">
                                                    <button class="button button-danger" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/veiculos/inativar"
                                                            data-id="${veiculo.id}"
                                                            data-title="Inativar veículo"
                                                            data-message="Deseja inativar o veículo ${veiculo.placa}?"
                                                            data-submit="Inativar">Inativar</button>
                                                </c:if>
                                                <c:if test="${veiculo.status.name() == 'INATIVO'}">
                                                    <button class="button button-secondary" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/veiculos/ativar"
                                                            data-id="${veiculo.id}"
                                                            data-title="Ativar veículo"
                                                            data-message="Deseja ativar o veículo ${veiculo.placa} como disponível?"
                                                            data-submit="Ativar">Ativar</button>
                                                </c:if>
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty veiculos}">
                                <tr>
                                    <td colspan="${podeGerenciarVeiculos ? 8 : 7}">
                                        <div class="empty-state">Nenhum veículo cadastrado.</div>
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
