<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Notificações</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="notificacoes" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Alertas operacionais</span>
                    <h1>Notificações</h1>
                    <p>Acompanhamento de alertas internos sobre fretes, frota, financeiro e ocorrências.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>
            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card" aria-label="Lista de notificações">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Tipo</th>
                                <th>Status</th>
                                <th>Título</th>
                                <th>Mensagem</th>
                                <th>Referência</th>
                                <th>Criação</th>
                                <th>Leitura</th>
                                <c:if test="${podeGerenciarNotificacoes}">
                                    <th>Ações</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="notificacao" items="${notificacoes}">
                                <tr>
                                    <td>${notificacao.tipo.descricao}</td>
                                    <td>
                                        <span class="badge badge-status-${notificacao.status.name().toLowerCase()}">${notificacao.status.descricao}</span>
                                    </td>
                                    <td><strong>${notificacao.titulo}</strong></td>
                                    <td class="text-muted">${notificacao.mensagem}</td>
                                    <td class="text-muted">
                                        <c:choose>
                                            <c:when test="${not empty notificacao.referenciaTipo}">
                                                ${notificacao.referenciaTipo}
                                                <c:if test="${not empty notificacao.referenciaId}">
                                                    #${notificacao.referenciaId}
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${notificacao.dataCriacaoFormatada}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td class="text-muted">
                                        <c:choose>
                                            <c:when test="${not empty notificacao.dataLeituraFormatada}">
                                                <fmt:formatDate value="${notificacao.dataLeituraFormatada}" pattern="dd/MM/yyyy HH:mm"/>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <c:if test="${podeGerenciarNotificacoes}">
                                        <td>
                                            <div class="row-actions">
                                                <c:if test="${notificacao.status.name() == 'NAO_LIDA'}">
                                                    <form method="post" action="${pageContext.request.contextPath}/notificacoes/lida">
                                                        <input type="hidden" name="id" value="${notificacao.id}">
                                                        <button class="button button-secondary" type="submit">Marcar lida</button>
                                                    </form>
                                                </c:if>
                                                <form method="post" action="${pageContext.request.contextPath}/notificacoes/arquivar">
                                                    <input type="hidden" name="id" value="${notificacao.id}">
                                                    <button class="button button-secondary" type="submit">Arquivar</button>
                                                </form>
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty notificacoes}">
                                <tr>
                                    <td colspan="${podeGerenciarNotificacoes ? 8 : 7}">
                                        <div class="empty-state">Nenhuma notificação operacional pendente.</div>
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
