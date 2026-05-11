<%@ page pageEncoding="UTF-8" %>
<%@ page import="br.com.gwfrete.notificacao.bo.NotificacaoBO" %>
<%@ page import="br.com.gwfrete.notificacao.model.Notificacao" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    if (session.getAttribute("usuarioLogado") != null) {
        List<Notificacao> notificacoesNaoLidas = new ArrayList<>();

        try {
            notificacoesNaoLidas = new NotificacaoBO().listarNaoLidas();
        } catch (Exception e) {
            notificacoesNaoLidas = new ArrayList<>();
        }

        int limiteNotificacoesHeader = Math.min(notificacoesNaoLidas.size(), 5);
        request.setAttribute("notificacoesHeader", notificacoesNaoLidas.subList(0, limiteNotificacoesHeader));
        request.setAttribute("totalNotificacoesNaoLidasHeader", notificacoesNaoLidas.size());
    }
%>

<c:if test="${not empty sessionScope.usuarioLogado}">
    <c:choose>
        <c:when test="${param.layout == 'dashboard'}">
            <header class="dashboard-topbar">
                <span class="system-status">
                    <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M22 12h-4l-3 9L9 3l-3 9H2"/></svg>
                    SISTEMA ONLINE
                </span>
                <div class="topbar-right">
                    <span class="topbar-clock" id="dashboardClock">--:--:--</span>
                    <details class="notification-menu">
                        <summary class="notification-trigger" aria-label="Abrir notificações recentes">
                            <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9"/><path d="M10 21h4"/></svg>
                            <c:if test="${totalNotificacoesNaoLidasHeader gt 0}">
                                <span class="notification-badge">${totalNotificacoesNaoLidasHeader}</span>
                            </c:if>
                        </summary>
                        <div class="notification-dropdown">
                            <div class="notification-dropdown-head">
                                <strong>Notificações</strong>
                                <span>${totalNotificacoesNaoLidasHeader} não lidas</span>
                            </div>
                            <div class="notification-list">
                                <c:forEach var="notificacaoHeader" items="${notificacoesHeader}">
                                    <article class="notification-item">
                                        <div>
                                            <strong>${notificacaoHeader.titulo}</strong>
                                            <p>${notificacaoHeader.mensagem}</p>
                                        </div>
                                        <div class="notification-meta">
                                            <span>${notificacaoHeader.tipo.descricao}</span>
                                            <time><fmt:formatDate value="${notificacaoHeader.dataCriacaoFormatada}" pattern="dd/MM/yyyy HH:mm"/></time>
                                        </div>
                                    </article>
                                </c:forEach>
                                <c:if test="${empty notificacoesHeader}">
                                    <div class="notification-empty">Nenhuma notificação nova</div>
                                </c:if>
                            </div>
                            <a class="notification-view-all" href="${pageContext.request.contextPath}/notificacoes">Ver todas</a>
                        </div>
                    </details>
                    <label class="theme-switcher">
                        <span>Tema</span>
                        <select data-theme-select aria-label="Selecionar tema visual">
                            <option value="theme-dark">Escuro</option>
                            <option value="theme-light">Claro</option>
                            <option value="theme-high-contrast">Alto contraste</option>
                        </select>
                    </label>
                    <div class="user-summary">
                        <span class="user-avatar" aria-hidden="true">
                            <svg viewBox="0 0 24 24"><path d="M20 21a8 8 0 0 0-16 0"/><path d="M12 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8z"/></svg>
                        </span>
                        <span class="user-copy">
                            <strong>${sessionScope.usuarioLogado.nome}</strong>
                            <small>${sessionScope.usuarioLogado.perfil.descricao}</small>
                        </span>
                    </div>
                    <a class="logout-link" href="${pageContext.request.contextPath}/logout">
                        <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><path d="m16 17 5-5-5-5"/><path d="M21 12H9"/></svg>
                        SAIR
                    </a>
                </div>
            </header>
            <script>
                (function () {
                    const clock = document.getElementById("dashboardClock");
                    if (!clock) {
                        return;
                    }
                    const updateClock = function () {
                        clock.textContent = new Date().toLocaleTimeString("pt-BR", {
                            hour: "2-digit",
                            minute: "2-digit",
                            second: "2-digit"
                        });
                    };
                    updateClock();
                    setInterval(updateClock, 1000);
                })();
            </script>
        </c:when>
        <c:otherwise>
            <div class="header-cluster">
                <details class="notification-menu">
                    <summary class="notification-trigger" aria-label="Abrir notificações recentes">
                        <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9"/><path d="M10 21h4"/></svg>
                        <c:if test="${totalNotificacoesNaoLidasHeader gt 0}">
                            <span class="notification-badge">${totalNotificacoesNaoLidasHeader}</span>
                        </c:if>
                    </summary>
                    <div class="notification-dropdown">
                        <div class="notification-dropdown-head">
                            <strong>Notificações</strong>
                            <span>${totalNotificacoesNaoLidasHeader} não lidas</span>
                        </div>
                        <div class="notification-list">
                            <c:forEach var="notificacaoHeader" items="${notificacoesHeader}">
                                <article class="notification-item">
                                    <div>
                                        <strong>${notificacaoHeader.titulo}</strong>
                                        <p>${notificacaoHeader.mensagem}</p>
                                    </div>
                                    <div class="notification-meta">
                                        <span>${notificacaoHeader.tipo.descricao}</span>
                                        <time><fmt:formatDate value="${notificacaoHeader.dataCriacaoFormatada}" pattern="dd/MM/yyyy HH:mm"/></time>
                                    </div>
                                </article>
                            </c:forEach>
                            <c:if test="${empty notificacoesHeader}">
                                <div class="notification-empty">Nenhuma notificação nova</div>
                            </c:if>
                        </div>
                        <a class="notification-view-all" href="${pageContext.request.contextPath}/notificacoes">Ver todas</a>
                    </div>
                </details>
                <label class="theme-switcher">
                    <span>Tema</span>
                    <select data-theme-select aria-label="Selecionar tema visual">
                        <option value="theme-dark">Escuro</option>
                        <option value="theme-light">Claro</option>
                        <option value="theme-high-contrast">Alto contraste</option>
                    </select>
                </label>
                <div class="session-user">
                    <span>${sessionScope.usuarioLogado.nome}</span>
                    <strong>${sessionScope.usuarioLogado.perfil.descricao}</strong>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</c:if>
