<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="perfilLogado" value="${sessionScope.usuarioLogado.perfil.name()}" />
<c:set var="classeSidebar" value="${param.layout == 'dashboard' ? 'dashboard-sidebar' : 'app-sidebar'}" />
<c:set var="classeMarca" value="${param.layout == 'dashboard' ? 'sidebar-brand' : 'app-brand'}" />

<aside class="${classeSidebar}">
    <div class="${classeMarca}">
        <span class="brand-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24"><path d="M3 7h10v10H3z"/><path d="M13 10h4l4 4v3h-8z"/><path d="M7 17a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"/><path d="M17 17a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"/></svg>
        </span>
        <span>
            <strong>GW FRETE</strong>
            <small>TMS // V4.12.0</small>
        </span>
    </div>
    <nav aria-label="Módulos principais">
        <a href="${pageContext.request.contextPath}/dashboard" class="${param.ativo == 'dashboard' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Dashboard</a>

        <c:if test="${perfilLogado == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/usuarios" class="${param.ativo == 'usuarios' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Usuários</a>
        </c:if>

        <a href="${pageContext.request.contextPath}/clientes" class="${param.ativo == 'clientes' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Clientes</a>
        <a href="${pageContext.request.contextPath}/veiculos" class="${param.ativo == 'veiculos' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Veículos</a>
        <a href="${pageContext.request.contextPath}/motoristas" class="${param.ativo == 'motoristas' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Motoristas</a>
        <a href="${pageContext.request.contextPath}/fretes" class="${param.ativo == 'fretes' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Fretes</a>
        <a href="${pageContext.request.contextPath}/ocorrencias-frete" class="${param.ativo == 'ocorrencias' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Ocorrências</a>
        <a href="${pageContext.request.contextPath}/rastreamentos" class="${param.ativo == 'rastreamentos' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Rastreamentos</a>

        <c:if test="${perfilLogado == 'ADMIN' || perfilLogado == 'GESTOR'}">
            <a href="${pageContext.request.contextPath}/relatorios/fretes" class="${param.ativo == 'relatorios' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Relatórios</a>
            <a href="${pageContext.request.contextPath}/financeiro" class="${param.ativo == 'financeiro' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Financeiro</a>
            <a href="${pageContext.request.contextPath}/manutencoes" class="${param.ativo == 'manutencoes' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Manutenções</a>
        </c:if>

        <a href="${pageContext.request.contextPath}/notificacoes" class="${param.ativo == 'notificacoes' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Notificações</a>

        <c:if test="${perfilLogado == 'ADMIN' || perfilLogado == 'GESTOR'}">
            <a href="${pageContext.request.contextPath}/contratos" class="${param.ativo == 'contratos' ? 'active' : ''}"><span class="nav-mark" aria-hidden="true"></span>Contratos</a>
        </c:if>
    </nav>
    <span class="sidebar-footer">© 2026 GW FRETE</span>
</aside>
