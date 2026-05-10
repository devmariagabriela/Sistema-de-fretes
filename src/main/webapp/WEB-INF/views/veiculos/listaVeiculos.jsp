<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Veículos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
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
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
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
</body>
</html>
