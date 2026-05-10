<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Ocorrências de Frete</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="ocorrencias" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Rastreamento operacional</span>
                    <h1>Ocorrências de frete</h1>
                    <p>Histórico de movimentações, entregas e eventos críticos do transporte.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/fretes">Fretes</a>
                    <c:if test="${podeGerenciarOcorrencias}">
                        <c:url var="novaOcorrenciaUrl" value="/ocorrencias-frete/nova">
                            <c:if test="${not empty freteSelecionadoId}">
                                <c:param name="freteId" value="${freteSelecionadoId}"/>
                            </c:if>
                        </c:url>
                        <a class="button button-primary" href="${novaOcorrenciaUrl}">Nova ocorrência</a>
                    </c:if>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card filter-card" aria-label="Filtro de ocorrências">
                <form class="inline-filter" action="${pageContext.request.contextPath}/ocorrencias-frete/frete" method="get">
                    <div class="form-field">
                        <label for="freteId">Frete</label>
                        <select id="freteId" name="freteId" required>
                            <option value="">Selecione um frete</option>
                            <c:forEach var="frete" items="${fretes}">
                                <option value="${frete.id}" ${freteSelecionadoId == frete.id ? 'selected' : ''}>
                                    ${frete.codigo} - ${frete.origem} → ${frete.destino}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <button class="button button-primary" type="submit">Consultar</button>
                </form>
            </section>

            <section class="content-card" aria-label="Timeline de ocorrências">
                <div class="timeline">
                    <c:forEach var="ocorrencia" items="${ocorrencias}">
                        <article class="timeline-item">
                            <div class="timeline-marker badge-ocorrencia-${ocorrencia.tipo.name().toLowerCase()}"></div>
                            <div class="timeline-content">
                                <div class="timeline-head">
                                    <div>
                                        <span class="badge badge-ocorrencia-${ocorrencia.tipo.name().toLowerCase()}">${ocorrencia.tipo.descricao}</span>
                                        <strong>${ocorrencia.frete.codigo}</strong>
                                    </div>
                                    <span class="text-muted">
                                        <fmt:formatDate value="${ocorrencia.dataHoraFormatada}" pattern="dd/MM/yyyy HH:mm"/>
                                    </span>
                                </div>

                                <div class="timeline-grid">
                                    <div>
                                        <span class="summary-label">Localização</span>
                                        <p>${ocorrencia.localizacao}</p>
                                    </div>
                                    <div>
                                        <span class="summary-label">Status visual</span>
                                        <span class="badge badge-frete-${ocorrencia.frete.status.name().toLowerCase()}">${ocorrencia.frete.status.descricao}</span>
                                    </div>
                                    <div class="timeline-span">
                                        <span class="summary-label">Descrição</span>
                                        <p class="text-muted">${empty ocorrencia.descricao ? 'Sem descrição adicional.' : ocorrencia.descricao}</p>
                                    </div>
                                    <div class="timeline-span">
                                        <span class="summary-label">Recebedor</span>
                                        <p class="text-muted">
                                            <c:choose>
                                                <c:when test="${not empty ocorrencia.nomeRecebedor}">
                                                    ${ocorrencia.nomeRecebedor} · ${ocorrencia.documentoRecebedor}
                                                </c:when>
                                                <c:otherwise>Não informado.</c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </article>
                    </c:forEach>

                    <c:if test="${empty ocorrencias}">
                        <div class="empty-state">Selecione um frete para visualizar o rastreamento operacional.</div>
                    </c:if>
                </div>
            </section>

            <section class="content-card" aria-label="Tabela de ocorrências">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Frete</th>
                                <th>Tipo ocorrência</th>
                                <th>Data/hora</th>
                                <th>Localização</th>
                                <th>Descrição</th>
                                <th>Recebedor</th>
                                <th>Status visual</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ocorrencia" items="${ocorrencias}">
                                <tr>
                                    <td><strong class="freight-code">${ocorrencia.frete.codigo}</strong></td>
                                    <td><span class="badge badge-ocorrencia-${ocorrencia.tipo.name().toLowerCase()}">${ocorrencia.tipo.descricao}</span></td>
                                    <td class="text-muted"><fmt:formatDate value="${ocorrencia.dataHoraFormatada}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td>${ocorrencia.localizacao}</td>
                                    <td class="text-muted">${ocorrencia.descricao}</td>
                                    <td class="text-muted">${ocorrencia.nomeRecebedor}</td>
                                    <td><span class="badge badge-frete-${ocorrencia.frete.status.name().toLowerCase()}">${ocorrencia.frete.status.descricao}</span></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty ocorrencias}">
                                <tr>
                                    <td colspan="7">
                                        <div class="empty-state">Nenhuma ocorrência encontrada para o frete selecionado.</div>
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
