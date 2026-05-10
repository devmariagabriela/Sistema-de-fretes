<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="perfilLogado" value="${sessionScope.usuarioLogado.perfil.name()}" />
<c:set var="classeSidebar" value="${param.layout == 'dashboard' ? 'dashboard-sidebar' : 'app-sidebar'}" />
<c:set var="classeMarca" value="${param.layout == 'dashboard' ? 'sidebar-brand' : 'app-brand'}" />

<aside class="${classeSidebar}">
    <div class="${classeMarca}">GW FRETE</div>
    <nav aria-label="Módulos principais">
        <a href="${pageContext.request.contextPath}/dashboard" class="${param.ativo == 'dashboard' ? 'active' : ''}">Dashboard</a>

        <c:if test="${perfilLogado == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/usuarios" class="${param.ativo == 'usuarios' ? 'active' : ''}">Usuários</a>
        </c:if>

        <a href="${pageContext.request.contextPath}/clientes" class="${param.ativo == 'clientes' ? 'active' : ''}">Clientes</a>
        <a href="${pageContext.request.contextPath}/veiculos" class="${param.ativo == 'veiculos' ? 'active' : ''}">Veículos</a>
        <a href="${pageContext.request.contextPath}/motoristas" class="${param.ativo == 'motoristas' ? 'active' : ''}">Motoristas</a>
        <a href="${pageContext.request.contextPath}/fretes" class="${param.ativo == 'fretes' ? 'active' : ''}">Fretes</a>
        <a href="${pageContext.request.contextPath}/ocorrencias-frete" class="${param.ativo == 'ocorrencias' ? 'active' : ''}">Ocorrências</a>
        <a href="${pageContext.request.contextPath}/rastreamentos" class="${param.ativo == 'rastreamentos' ? 'active' : ''}">Rastreamentos</a>

        <c:if test="${perfilLogado == 'ADMIN' || perfilLogado == 'GESTOR'}">
            <a href="${pageContext.request.contextPath}/relatorios/fretes" class="${param.ativo == 'relatorios' ? 'active' : ''}">Relatórios</a>
            <a href="${pageContext.request.contextPath}/financeiro" class="${param.ativo == 'financeiro' ? 'active' : ''}">Financeiro</a>
            <a href="${pageContext.request.contextPath}/manutencoes" class="${param.ativo == 'manutencoes' ? 'active' : ''}">Manutenções</a>
        </c:if>

        <a href="${pageContext.request.contextPath}/notificacoes" class="${param.ativo == 'notificacoes' ? 'active' : ''}">Notificações</a>

        <c:if test="${perfilLogado == 'ADMIN' || perfilLogado == 'GESTOR'}">
            <a href="${pageContext.request.contextPath}/contratos" class="${param.ativo == 'contratos' ? 'active' : ''}">Contratos</a>
        </c:if>
    </nav>
</aside>
