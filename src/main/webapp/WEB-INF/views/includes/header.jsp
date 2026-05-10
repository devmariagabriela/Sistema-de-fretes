<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
            <div class="session-user">
                <span>${sessionScope.usuarioLogado.nome}</span>
                <strong>${sessionScope.usuarioLogado.perfil.descricao}</strong>
            </div>
        </c:otherwise>
    </c:choose>
</c:if>
