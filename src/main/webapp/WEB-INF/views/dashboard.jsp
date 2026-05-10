<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body class="dashboard-page">
    <main class="dashboard-shell">
        <aside class="dashboard-sidebar">
            <div class="sidebar-brand">GW FRETE</div>
            <nav aria-label="Módulos principais">
                <a href="${pageContext.request.contextPath}/dashboard" class="active">Dashboard</a>
                <a href="#">Clientes</a>
                <a href="${pageContext.request.contextPath}/motoristas">Motoristas</a>
                <a href="${pageContext.request.contextPath}/veiculos">Veículos</a>
                <a href="${pageContext.request.contextPath}/fretes">Fretes</a>
                <a href="${pageContext.request.contextPath}/ocorrencias-frete">Ocorrências</a>
                <a href="${pageContext.request.contextPath}/relatorios/fretes">Relatórios</a>
            </nav>
        </aside>

        <section class="dashboard-content">
            <header class="dashboard-header">
                <div>
                    <span class="section-kicker">Operação logística</span>
                    <h1>Dashboard executivo</h1>
                </div>
                <div class="user-summary">
                    <span>${sessionScope.usuarioLogado.nome}</span>
                    <strong>${sessionScope.usuarioLogado.perfil.descricao}</strong>
                    <a href="${pageContext.request.contextPath}/logout">Sair</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="dashboard-message" role="alert">${mensagemErro}</p>
            </c:if>

            <div class="dashboard-section-header">
                <div>
                    <span class="section-kicker">Indicadores principais</span>
                    <h2>Resumo operacional em tempo real</h2>
                </div>
            </div>

            <div class="kpi-grid executive-kpi-grid">
                <article class="kpi-card kpi-card-primary">
                    <span class="kpi-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M3 7h10v10H3z"/><path d="M13 10h4l4 4v3h-8z"/><path d="M7 17a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"/><path d="M17 17a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"/></svg>
                    </span>
                    <span>Total de fretes</span>
                    <strong>${dashboardDTO.totalFretes}</strong>
                    <small>Operações registradas</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-info" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M4 17V7"/><path d="M20 17V7"/><path d="M7 12h10"/><path d="m14 9 3 3-3 3"/><path d="M4 7h4"/><path d="M16 17h4"/></svg>
                    </span>
                    <span>Fretes em trânsito</span>
                    <strong>${dashboardDTO.fretesEmTransito}</strong>
                    <small>Em acompanhamento</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-success" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M20 6 9 17l-5-5"/></svg>
                    </span>
                    <span>Fretes entregues</span>
                    <strong>${dashboardDTO.fretesEntregues}</strong>
                    <small>Concluídos</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-neutral" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg>
                    </span>
                    <span>Fretes cancelados</span>
                    <strong>${dashboardDTO.fretesCancelados}</strong>
                    <small>Interrompidos</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M5 11h14l-2-5H7z"/><path d="M7 16h10"/><path d="M6 16a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"/><path d="M18 16a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"/></svg>
                    </span>
                    <span>Total de veículos</span>
                    <strong>${dashboardDTO.totalVeiculos}</strong>
                    <small>Frota cadastrada</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-success" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M3 12h18"/><path d="M6 8h12"/><path d="M7 16h10"/><path d="M12 3v18"/></svg>
                    </span>
                    <span>Veículos disponíveis</span>
                    <strong>${dashboardDTO.veiculosDisponiveis}</strong>
                    <small>Prontos para operação</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-warning" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="m14.7 6.3 3 3"/><path d="M5 19l8.5-8.5"/><path d="M15 5l4 4-2.5 2.5-4-4z"/><path d="M4 20h4"/></svg>
                    </span>
                    <span>Veículos em manutenção</span>
                    <strong>${dashboardDTO.veiculosEmManutencao}</strong>
                    <small>Fora da disponibilidade</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M16 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><path d="M10 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8z"/><path d="M20 21v-2a4 4 0 0 0-3-3.9"/><path d="M17 3.1a4 4 0 0 1 0 7.8"/></svg>
                    </span>
                    <span>Total de motoristas</span>
                    <strong>${dashboardDTO.totalMotoristas}</strong>
                    <small>Equipe cadastrada</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-success" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8z"/><path d="M4 21a8 8 0 0 1 16 0"/><path d="m16 11 2 2 4-4"/></svg>
                    </span>
                    <span>Motoristas ativos</span>
                    <strong>${dashboardDTO.motoristasAtivos}</strong>
                    <small>Aptos na operação</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-warning" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M12 9v4"/><path d="M12 17h.01"/><path d="M10.3 4.3 2.8 18a2 2 0 0 0 1.7 3h15a2 2 0 0 0 1.7-3L13.7 4.3a2 2 0 0 0-3.4 0z"/></svg>
                    </span>
                    <span>Total de ocorrências</span>
                    <strong>${dashboardDTO.totalOcorrencias}</strong>
                    <small>Eventos logísticos</small>
                </article>
            </div>
        </section>
    </main>
</body>
</html>
