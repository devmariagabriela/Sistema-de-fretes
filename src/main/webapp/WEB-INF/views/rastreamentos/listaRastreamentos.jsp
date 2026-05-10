<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Rastreamentos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="rastreamentos" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Rastreamento operacional</span>
                    <h1>Rastreamentos</h1>
                    <p>Histórico de pontos de localização registrados para acompanhamento de fretes.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <c:if test="${podeGerenciarRastreamentos}">
                        <c:choose>
                            <c:when test="${not empty freteIdSelecionado}">
                                <a class="button button-primary" href="${pageContext.request.contextPath}/rastreamentos/novo?freteId=${freteIdSelecionado}">Novo ponto</a>
                            </c:when>
                            <c:otherwise>
                                <a class="button button-primary" href="${pageContext.request.contextPath}/rastreamentos/novo">Novo ponto</a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>
            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card" aria-label="Filtro de rastreamentos">
                <form action="${pageContext.request.contextPath}/rastreamentos/frete" method="get">
                    <div class="form-grid">
                        <div class="form-field">
                            <label for="freteId">Frete</label>
                            <select id="freteId" name="freteId" required>
                                <option value="">Selecione</option>
                                <c:forEach var="frete" items="${fretes}">
                                    <option value="${frete.id}" ${freteIdSelecionado == frete.id ? 'selected' : ''}>${frete.codigo} - ${frete.origem} / ${frete.destino}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-actions">
                        <button class="button button-primary" type="submit">Consultar</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/rastreamentos">Limpar</a>
                    </div>
                </form>
            </section>

            <section class="content-card" aria-label="Lista de rastreamentos">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Data/hora</th>
                                <th>Frete</th>
                                <th>Localização</th>
                                <th>Latitude</th>
                                <th>Longitude</th>
                                <th>Observação</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="rastreamento" items="${rastreamentos}">
                                <tr>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${rastreamento.dataHoraFormatada}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td><strong>${rastreamento.frete.codigo}</strong></td>
                                    <td>${rastreamento.localizacao}</td>
                                    <td class="text-muted">${empty rastreamento.latitude ? '-' : rastreamento.latitude}</td>
                                    <td class="text-muted">${empty rastreamento.longitude ? '-' : rastreamento.longitude}</td>
                                    <td class="text-muted">${empty rastreamento.observacao ? '-' : rastreamento.observacao}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty rastreamentos}">
                                <tr>
                                    <td colspan="6">
                                        <div class="empty-state">Selecione um frete para consultar o histórico de rastreamento.</div>
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
