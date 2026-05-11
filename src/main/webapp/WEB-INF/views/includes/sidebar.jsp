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
        <a href="${pageContext.request.contextPath}/dashboard" class="${param.ativo == 'dashboard' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><rect x="3" y="3" width="7" height="7" rx="1.5"/><rect x="14" y="3" width="7" height="7" rx="1.5"/><rect x="3" y="14" width="7" height="7" rx="1.5"/><rect x="14" y="14" width="7" height="7" rx="1.5"/></svg></span>Dashboard</a>

        <c:if test="${perfilLogado == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/usuarios" class="${param.ativo == 'usuarios' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg></span>Usuários</a>
        </c:if>

        <a href="${pageContext.request.contextPath}/clientes" class="${param.ativo == 'clientes' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><circle cx="12" cy="8" r="4"/><path d="M5 21a7 7 0 0 1 14 0"/></svg></span>Clientes</a>
        <a href="${pageContext.request.contextPath}/veiculos" class="${param.ativo == 'veiculos' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M5 17h14l-1.4-5.2A3 3 0 0 0 14.7 10H9.3a3 3 0 0 0-2.9 1.8L5 17z"/><path d="M7 17v2"/><path d="M17 17v2"/><circle cx="8" cy="17" r="2"/><circle cx="16" cy="17" r="2"/></svg></span>Veículos</a>
        <a href="${pageContext.request.contextPath}/motoristas" class="${param.ativo == 'motoristas' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><circle cx="12" cy="7" r="4"/><path d="M6 21v-2a6 6 0 0 1 12 0v2"/><path d="M16 11l2 2 4-4"/></svg></span>Motoristas</a>
        <a href="${pageContext.request.contextPath}/fretes" class="${param.ativo == 'fretes' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/><path d="M3.3 7 12 12l8.7-5"/><path d="M12 22V12"/></svg></span>Fretes</a>
        <a href="${pageContext.request.contextPath}/ocorrencias-frete" class="${param.ativo == 'ocorrencias' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M10.3 3.9 1.8 18a2 2 0 0 0 1.7 3h17a2 2 0 0 0 1.7-3L13.7 3.9a2 2 0 0 0-3.4 0z"/><path d="M12 9v4"/><path d="M12 17h.01"/></svg></span>Ocorrências</a>
        <a href="${pageContext.request.contextPath}/rastreamentos" class="${param.ativo == 'rastreamentos' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M12 21s7-4.7 7-11a7 7 0 0 0-14 0c0 6.3 7 11 7 11z"/><circle cx="12" cy="10" r="2.5"/></svg></span>Rastreamentos</a>

        <c:if test="${perfilLogado == 'ADMIN' || perfilLogado == 'GESTOR'}">
            <a href="${pageContext.request.contextPath}/relatorios/fretes" class="${param.ativo == 'relatorios' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M4 19V5"/><path d="M4 19h16"/><path d="M8 16v-5"/><path d="M12 16V8"/><path d="M16 16v-3"/></svg></span>Relatórios</a>
            <a href="${pageContext.request.contextPath}/financeiro" class="${param.ativo == 'financeiro' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M12 2v20"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7H14a3.5 3.5 0 0 1 0 7H6"/></svg></span>Financeiro</a>
            <a href="${pageContext.request.contextPath}/manutencoes" class="${param.ativo == 'manutencoes' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M14.7 6.3a5 5 0 0 0-6.9 6.9L3 18v3h3l4.8-4.8a5 5 0 0 0 6.9-6.9l-3 3-3-3 3-3z"/></svg></span>Manutenções</a>
        </c:if>

        <a href="${pageContext.request.contextPath}/notificacoes" class="${param.ativo == 'notificacoes' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9"/><path d="M10 21h4"/></svg></span>Notificações</a>

        <c:if test="${perfilLogado == 'ADMIN' || perfilLogado == 'GESTOR'}">
            <a href="${pageContext.request.contextPath}/contratos" class="${param.ativo == 'contratos' ? 'active' : ''}"><span class="nav-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M6 2h9l5 5v15H6z"/><path d="M14 2v6h6"/><path d="M9 13h6"/><path d="M9 17h6"/></svg></span>Contratos</a>
        </c:if>
    </nav>
    <span class="sidebar-footer">© 2026 GW FRETE</span>
</aside>
