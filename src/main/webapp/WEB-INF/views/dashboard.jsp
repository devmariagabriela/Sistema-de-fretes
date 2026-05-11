<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css?v=dashboard-20260510-theme">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-ui"></script>
</head>
<body class="dashboard-page theme-dark">
    <fmt:setLocale value="pt_BR"/>

    <c:set var="fretesOutros" value="${kpiExecutivoDTO.totalFretes - kpiExecutivoDTO.fretesEmTransito - kpiExecutivoDTO.fretesEntregues - kpiExecutivoDTO.fretesCancelados}" />
    <c:if test="${fretesOutros lt 0}"><c:set var="fretesOutros" value="0" /></c:if>
    <c:set var="veiculosOutros" value="${dashboardDTO.totalVeiculos - dashboardDTO.veiculosDisponiveis - dashboardDTO.veiculosEmManutencao}" />
    <c:if test="${veiculosOutros lt 0}"><c:set var="veiculosOutros" value="0" /></c:if>
    <c:set var="faturasOutras" value="${kpiExecutivoDTO.totalFaturas - kpiExecutivoDTO.faturasPendentes - kpiExecutivoDTO.faturasVencidas}" />
    <c:if test="${faturasOutras lt 0}"><c:set var="faturasOutras" value="0" /></c:if>

    <c:set var="maxFretesStatus" value="${kpiExecutivoDTO.fretesEmTransito}" />
    <c:if test="${kpiExecutivoDTO.fretesEntregues gt maxFretesStatus}"><c:set var="maxFretesStatus" value="${kpiExecutivoDTO.fretesEntregues}" /></c:if>
    <c:if test="${kpiExecutivoDTO.fretesCancelados gt maxFretesStatus}"><c:set var="maxFretesStatus" value="${kpiExecutivoDTO.fretesCancelados}" /></c:if>
    <c:if test="${fretesOutros gt maxFretesStatus}"><c:set var="maxFretesStatus" value="${fretesOutros}" /></c:if>
    <c:if test="${maxFretesStatus lt 1}"><c:set var="maxFretesStatus" value="1" /></c:if>

    <c:set var="fretesTotalChart" value="${kpiExecutivoDTO.fretesEmTransito + kpiExecutivoDTO.fretesEntregues + kpiExecutivoDTO.fretesCancelados + fretesOutros}" />
    <c:set var="veiculosTotalChart" value="${dashboardDTO.veiculosDisponiveis + dashboardDTO.veiculosEmManutencao + veiculosOutros}" />
    <c:set var="faturasTotalChart" value="${kpiExecutivoDTO.faturasPendentes + kpiExecutivoDTO.faturasVencidas + faturasOutras}" />

    <main class="dashboard-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="dashboard" />
            <jsp:param name="layout" value="dashboard" />
        </jsp:include>

        <section class="dashboard-main">
            <jsp:include page="/WEB-INF/views/includes/header.jsp">
                <jsp:param name="layout" value="dashboard" />
            </jsp:include>

            <div class="dashboard-content">
                <header class="dashboard-titlebar">
                    <div>
                        <span class="section-kicker">Operação logística</span>
                        <h1>Dashboard executivo</h1>
                    </div>
                    <span class="live-badge">
                        <svg viewBox="0 0 24 24" aria-hidden="true"><path d="m3 17 6-6 4 4 7-7"/><path d="M14 8h6v6"/></svg>
                        TEMPO REAL
                    </span>
                </header>

                <c:if test="${not empty mensagemErro}">
                    <p class="dashboard-message" role="alert">${mensagemErro}</p>
                </c:if>

                <section class="dashboard-section">
                    <div class="dashboard-section-header">
                        <span class="section-kicker">Indicadores principais</span>
                        <h2>Resumo operacional em tempo real</h2>
                    </div>
                    <div class="kpi-grid executive-kpi-grid">
                        <article class="kpi-card kpi-card-primary">
                            <span class="kpi-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="m21 16-4 4-4-4"/><path d="M17 20V4"/><path d="M3 8h10"/><path d="M3 16h7"/></svg></span>
                            <span>Total de fretes</span>
                            <strong>${dashboardDTO.totalFretes}</strong>
                            <small>Operações registradas</small>
                        </article>
                        <article class="kpi-card kpi-card-info">
                            <span class="kpi-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M3 7h10v10H3z"/><path d="M13 10h4l4 4v3h-8z"/><path d="M7 17a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"/><path d="M17 17a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"/></svg></span>
                            <span>Fretes em trânsito</span>
                            <strong>${dashboardDTO.fretesEmTransito}</strong>
                            <small>Em acompanhamento</small>
                        </article>
                        <article class="kpi-card kpi-card-success">
                            <span class="kpi-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M20 6 9 17l-5-5"/></svg></span>
                            <span>Fretes entregues</span>
                            <strong>${dashboardDTO.fretesEntregues}</strong>
                            <small>Concluídos</small>
                        </article>
                        <article class="kpi-card kpi-card-danger">
                            <span class="kpi-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg></span>
                            <span>Fretes cancelados</span>
                            <strong>${dashboardDTO.fretesCancelados}</strong>
                            <small>Interrompidos</small>
                        </article>
                    </div>
                </section>

                <section class="dashboard-section">
                    <div class="dashboard-section-header">
                        <span class="section-kicker">Indicadores executivos</span>
                        <h2>Consolidação gerencial</h2>
                    </div>
                    <div class="kpi-grid executive-kpi-grid">
                        <article class="kpi-card kpi-card-primary">
                            <span class="kpi-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M16 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><path d="M10 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8z"/><path d="m16 11 2 2 4-4"/></svg></span>
                            <span>Motoristas ativos</span>
                            <strong>${dashboardDTO.motoristasAtivos}</strong>
                            <small>Aptos na operação</small>
                        </article>
                        <article class="kpi-card kpi-card-warning">
                            <span class="kpi-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M12 9v4"/><path d="M12 17h.01"/><path d="M10.3 4.3 2.8 18a2 2 0 0 0 1.7 3h15a2 2 0 0 0 1.7-3L13.7 4.3a2 2 0 0 0-3.4 0z"/></svg></span>
                            <span>Total de ocorrências</span>
                            <strong>${dashboardDTO.totalOcorrencias}</strong>
                            <small>Eventos logísticos</small>
                        </article>
                        <article class="kpi-card kpi-card-warning">
                            <span class="kpi-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="m14.7 6.3 3 3"/><path d="M5 19l8.5-8.5"/><path d="M15 5l4 4-2.5 2.5-4-4z"/><path d="M4 20h4"/></svg></span>
                            <span>Manutenções em andamento</span>
                            <strong>${kpiExecutivoDTO.totalManutencoesEmAndamento}</strong>
                            <small>Frota em atenção</small>
                        </article>
                        <article class="kpi-card kpi-card-success">
                            <span class="kpi-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M12 2v20"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7H14a3.5 3.5 0 0 1 0 7H6"/></svg></span>
                            <span>Receita do mês</span>
                            <strong class="currency-value"><fmt:formatNumber value="${kpiExecutivoDTO.faturamentoTotal}" type="currency"/></strong>
                            <small>Faturamento operacional</small>
                        </article>
                    </div>
                </section>

                <section class="dashboard-section">
                    <div class="dashboard-section-header">
                        <span class="section-kicker">Fretes</span>
                        <h2>Fretes por status</h2>
                    </div>
                    <div class="charts-grid">
                        <article class="analytics-card">
                            <span class="chart-subtitle">Operacional</span>
                            <h3>Distribuição por status</h3>
                            <div class="bar-chart">
                                <div class="bar-item">
                                    <strong style="color:#00c6ff">${kpiExecutivoDTO.fretesEmTransito}</strong>
                                    <span class="bar-track"><span class="bar-fill" style="height:${kpiExecutivoDTO.fretesEmTransito * 100 / maxFretesStatus}%; background:#00c6ff"></span></span>
                                    <small>Em trânsito</small>
                                </div>
                                <div class="bar-item">
                                    <strong style="color:#22c55e">${kpiExecutivoDTO.fretesEntregues}</strong>
                                    <span class="bar-track"><span class="bar-fill" style="height:${kpiExecutivoDTO.fretesEntregues * 100 / maxFretesStatus}%; background:#22c55e"></span></span>
                                    <small>Entregue</small>
                                </div>
                                <div class="bar-item">
                                    <strong style="color:#ef4444">${kpiExecutivoDTO.fretesCancelados}</strong>
                                    <span class="bar-track"><span class="bar-fill" style="height:${kpiExecutivoDTO.fretesCancelados * 100 / maxFretesStatus}%; background:#ef4444"></span></span>
                                    <small>Cancelado</small>
                                </div>
                                <div class="bar-item">
                                    <strong style="color:#f59e0b">${fretesOutros}</strong>
                                    <span class="bar-track"><span class="bar-fill" style="height:${fretesOutros * 100 / maxFretesStatus}%; background:#f59e0b"></span></span>
                                    <small>Outros</small>
                                </div>
                            </div>
                        </article>

                        <article class="analytics-card">
                            <span class="chart-subtitle">Visão geral</span>
                            <h3>Fretes por status</h3>
                            <div class="donut-layout">
                                <div class="donut-chart" style="--p1:${fretesTotalChart gt 0 ? kpiExecutivoDTO.fretesEmTransito * 100 / fretesTotalChart : 0}%; --p2:${fretesTotalChart gt 0 ? (kpiExecutivoDTO.fretesEmTransito + kpiExecutivoDTO.fretesEntregues) * 100 / fretesTotalChart : 0}%; --p3:${fretesTotalChart gt 0 ? (kpiExecutivoDTO.fretesEmTransito + kpiExecutivoDTO.fretesEntregues + kpiExecutivoDTO.fretesCancelados) * 100 / fretesTotalChart : 0}%; --c1:#00c6ff; --c2:#22c55e; --c3:#ef4444; --c4:${fretesTotalChart gt 0 ? '#f59e0b' : 'rgba(255,255,255,0.06)'};">
                                    <strong>${fretesTotalChart}</strong>
                                    <span>total</span>
                                </div>
                                <div class="chart-legend">
                                    <span><i style="background:#00c6ff"></i>Em trânsito <strong>${kpiExecutivoDTO.fretesEmTransito}</strong></span>
                                    <span><i style="background:#22c55e"></i>Entregue <strong>${kpiExecutivoDTO.fretesEntregues}</strong></span>
                                    <span><i style="background:#ef4444"></i>Cancelado <strong>${kpiExecutivoDTO.fretesCancelados}</strong></span>
                                    <span><i style="background:#f59e0b"></i>Outros <strong>${fretesOutros}</strong></span>
                                </div>
                            </div>
                        </article>
                    </div>
                </section>

                <section class="dashboard-section">
                    <div class="charts-grid">
                        <div>
                            <div class="dashboard-section-header compact">
                                <span class="section-kicker">Frota</span>
                                <h2>Veículos por status</h2>
                            </div>
                            <article class="analytics-card">
                                <span class="chart-subtitle">Frota</span>
                                <h3>Status da frota</h3>
                                <div class="donut-layout">
                                    <div class="donut-chart" style="--p1:${veiculosTotalChart gt 0 ? dashboardDTO.veiculosDisponiveis * 100 / veiculosTotalChart : 0}%; --p2:${veiculosTotalChart gt 0 ? (dashboardDTO.veiculosDisponiveis + dashboardDTO.veiculosEmManutencao) * 100 / veiculosTotalChart : 0}%; --p3:${veiculosTotalChart gt 0 ? 100 : 0}%; --c1:#22c55e; --c2:#f59e0b; --c3:#00c6ff; --c4:${veiculosTotalChart gt 0 ? '#00c6ff' : 'rgba(255,255,255,0.06)'};">
                                        <strong>${veiculosTotalChart}</strong>
                                        <span>veículos</span>
                                    </div>
                                    <div class="chart-legend">
                                        <span><i style="background:#22c55e"></i>Disponíveis <strong>${dashboardDTO.veiculosDisponiveis}</strong></span>
                                        <span><i style="background:#f59e0b"></i>Em manutenção <strong>${dashboardDTO.veiculosEmManutencao}</strong></span>
                                        <span><i style="background:#00c6ff"></i>Em rota/Inativos <strong>${veiculosOutros}</strong></span>
                                    </div>
                                </div>
                            </article>
                        </div>

                        <div>
                            <div class="dashboard-section-header compact">
                                <span class="section-kicker">Financeiro</span>
                                <h2>Faturas por status</h2>
                            </div>
                            <article class="analytics-card">
                                <span class="chart-subtitle">Financeiro</span>
                                <h3>Recebíveis</h3>
                                <div class="donut-layout">
                                    <div class="donut-chart" style="--p1:${faturasTotalChart gt 0 ? kpiExecutivoDTO.faturasPendentes * 100 / faturasTotalChart : 0}%; --p2:${faturasTotalChart gt 0 ? (kpiExecutivoDTO.faturasPendentes + kpiExecutivoDTO.faturasVencidas) * 100 / faturasTotalChart : 0}%; --p3:${faturasTotalChart gt 0 ? 100 : 0}%; --c1:#f59e0b; --c2:#ef4444; --c3:#00c6ff; --c4:${faturasTotalChart gt 0 ? '#00c6ff' : 'rgba(255,255,255,0.06)'};">
                                        <strong>${faturasTotalChart}</strong>
                                        <span>faturas</span>
                                    </div>
                                    <div class="chart-legend">
                                        <span><i style="background:#f59e0b"></i>Pendentes <strong>${kpiExecutivoDTO.faturasPendentes}</strong></span>
                                        <span><i style="background:#ef4444"></i>Vencidas <strong>${kpiExecutivoDTO.faturasVencidas}</strong></span>
                                        <span><i style="background:#00c6ff"></i>Outras <strong>${faturasOutras}</strong></span>
                                    </div>
                                </div>
                            </article>
                        </div>
                    </div>
                </section>

                <section class="dashboard-section dashboard-section-last">
                    <div class="dashboard-section-header">
                        <span class="section-kicker">Operação</span>
                        <h2>Indicadores operacionais</h2>
                    </div>
                    <div class="operations-grid">
                        <article class="analytics-card activity-card">
                            <div class="card-heading-row">
                                <h3>Atividade recente</h3>
                                <span>TEMPO REAL</span>
                            </div>
                            <div class="activity-list">
                                <div class="activity-item">
                                    <time>08:12</time>
                                    <i style="background:#22c55e"></i>
                                    <div><strong>Coleta confirmada</strong><small>CT-E 58.421 · Recife → Curitiba</small></div>
                                </div>
                                <div class="activity-item">
                                    <time>11:40</time>
                                    <i style="background:#00c6ff"></i>
                                    <div><strong>Em trânsito</strong><small>Motorista ativo · Frota em acompanhamento</small></div>
                                </div>
                                <div class="activity-item">
                                    <time>16:25</time>
                                    <i style="background:#f59e0b"></i>
                                    <div><strong>Previsão entrega</strong><small>Estimado: 16:25 · No prazo</small></div>
                                </div>
                            </div>
                        </article>

                        <article class="analytics-card alerts-card">
                            <div class="alerts-heading">
                                <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9"/><path d="M10 21h4"/></svg>
                                <h3>Alertas</h3>
                            </div>
                            <div class="alert-stack">
                                <div class="operational-alert alert-warning"><i></i><span>Manutenção preventiva pendente · ${kpiExecutivoDTO.totalManutencoesEmAndamento} em andamento</span></div>
                                <div class="operational-alert alert-info"><i></i><span>${dashboardDTO.fretesEmTransito} fretes em trânsito no momento</span></div>
                                <div class="operational-alert alert-success"><i></i><span>Sistema operando normalmente</span></div>
                            </div>
                        </article>
                    </div>
                </section>
            </div>
        </section>
    </main>
</body>
</html>
