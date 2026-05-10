<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty sessionScope.usuarioLogado}">
    <div class="${param.layout == 'dashboard' ? 'user-summary' : 'session-user'}">
        <span>${sessionScope.usuarioLogado.nome}</span>
        <strong>${sessionScope.usuarioLogado.perfil.descricao}</strong>
        <c:if test="${param.layout == 'dashboard'}">
            <a href="${pageContext.request.contextPath}/logout">Sair</a>
        </c:if>
    </div>
</c:if>
