<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body class="dashboard-page">
    <fmt:setLocale value="pt_BR"/>
    <main class="dashboard-shell">
        <aside class="dashboard-sidebar">
            <div class="sidebar-brand">GW FRETE</div>
            <nav aria-label="Módulos principais">
                <a href="${pageContext.request.contextPath}/dashboard" class="active">Dashboard</a>
                <a href="${pageContext.request.contextPath}/clientes">Clientes</a>
                <a href="${pageContext.request.contextPath}/contratos">Contratos</a>
                <a href="${pageContext.request.contextPath}/motoristas">Motoristas</a>
                <a href="${pageContext.request.contextPath}/veiculos">Veículos</a>
                <a href="${pageContext.request.contextPath}/fretes">Fretes</a>
                <a href="${pageContext.request.contextPath}/financeiro">Financeiro</a>
                <a href="${pageContext.request.contextPath}/notificacoes">Notificações</a>
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

            <div class="dashboard-section-header executive-section-header">
                <div>
                    <span class="section-kicker">Indicadores executivos</span>
                    <h2>Consolidação gerencial</h2>
                </div>
            </div>

            <div class="kpi-grid executive-kpi-grid">
                <article class="kpi-card kpi-card-primary">
                    <span class="kpi-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M4 19h16"/><path d="M7 16V8"/><path d="M12 16V5"/><path d="M17 16v-3"/></svg>
                    </span>
                    <span>Faturamento total</span>
                    <strong class="currency-value"><fmt:formatNumber value="${kpiExecutivoDTO.faturamentoTotal}" type="currency"/></strong>
                    <small>Receita em faturas</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-danger" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M10.3 4.3 2.8 18a2 2 0 0 0 1.7 3h15a2 2 0 0 0 1.7-3L13.7 4.3a2 2 0 0 0-3.4 0z"/><path d="M12 9v4"/><path d="M12 17h.01"/></svg>
                    </span>
                    <span>Faturas vencidas</span>
                    <strong>${kpiExecutivoDTO.faturasVencidas}</strong>
                    <small>Demandam cobrança</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-success" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M7 3h10l3 3v15H4V3z"/><path d="M8 13h8"/><path d="M8 17h5"/><path d="M16 3v4h4"/></svg>
                    </span>
                    <span>Contratos ativos</span>
                    <strong>${kpiExecutivoDTO.totalContratosAtivos}</strong>
                    <small>Base contratual vigente</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-info" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9"/><path d="M10 21h4"/></svg>
                    </span>
                    <span>Notificações não lidas</span>
                    <strong>${kpiExecutivoDTO.totalNotificacoesNaoLidas}</strong>
                    <small>Alertas pendentes</small>
                </article>
                <article class="kpi-card">
                    <span class="kpi-icon kpi-icon-warning" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="m14.7 6.3 3 3"/><path d="M5 19l8.5-8.5"/><path d="M15 5l4 4-2.5 2.5-4-4z"/><path d="M4 20h4"/></svg>
                    </span>
                    <span>Manutenções em andamento</span>
                    <strong>${kpiExecutivoDTO.totalManutencoesEmAndamento}</strong>
                    <small>Frota em atenção</small>
                </article>
            </div>

            <section class="analytics-grid" aria-label="Análises executivas">
                <article class="analytics-card analytics-card-wide">
                    <div class="analytics-card-header">
                        <div>
                            <span class="section-kicker">Fretes</span>
                            <h2>Fretes por status</h2>
                        </div>
                        <span class="analytics-badge">Operacional</span>
                    </div>
                    <div class="chart-frame chart-frame-bar">
                        <canvas id="fretesStatusChart" aria-label="Gráfico de barras de fretes por status"></canvas>
                    </div>
                </article>

                <article class="analytics-card">
                    <div class="analytics-card-header">
                        <div>
                            <span class="section-kicker">Frota</span>
                            <h2>Veículos por status</h2>
                        </div>
                        <span class="analytics-badge">Frota</span>
                    </div>
                    <div class="chart-frame chart-frame-donut">
                        <canvas id="veiculosStatusChart" aria-label="Gráfico de rosca de veículos por status"></canvas>
                    </div>
                </article>

                <article class="analytics-card">
                    <div class="analytics-card-header">
                        <div>
                            <span class="section-kicker">Financeiro</span>
                            <h2>Faturas por status</h2>
                        </div>
                        <span class="analytics-badge">Recebíveis</span>
                    </div>
                    <div class="chart-frame chart-frame-donut">
                        <canvas id="faturasStatusChart" aria-label="Gráfico de faturas por status"></canvas>
                    </div>
                </article>

                <article class="analytics-card analytics-card-wide">
                    <div class="analytics-card-header">
                        <div>
                            <span class="section-kicker">Operação</span>
                            <h2>Indicadores operacionais</h2>
                        </div>
                        <span class="analytics-badge">Consolidado</span>
                    </div>
                    <div class="chart-frame chart-frame-bar">
                        <canvas id="indicadoresOperacionaisChart" aria-label="Gráfico de barras de indicadores operacionais"></canvas>
                    </div>
                </article>
            </section>

            <section class="operations-insight-grid" aria-label="Ocorrências e alertas operacionais">
                <article class="analytics-card">
                    <div class="analytics-card-header">
                        <div>
                            <span class="section-kicker">Monitoramento</span>
                            <h2>Últimas ocorrências</h2>
                        </div>
                    </div>
                    <div class="occurrence-list">
                        <div class="occurrence-item">
                            <span class="occurrence-dot occurrence-dot-info"></span>
                            <div>
                                <strong>Saída de pátio confirmada</strong>
                                <small>FRT-1048 · Recife/PE · 08:10</small>
                            </div>
                        </div>
                        <div class="occurrence-item">
                            <span class="occurrence-dot occurrence-dot-warning"></span>
                            <div>
                                <strong>Tentativa de entrega</strong>
                                <small>FRT-1039 · João Pessoa/PB · 10:45</small>
                            </div>
                        </div>
                        <div class="occurrence-item">
                            <span class="occurrence-dot occurrence-dot-success"></span>
                            <div>
                                <strong>Entrega realizada</strong>
                                <small>FRT-1027 · Natal/RN · 13:25</small>
                            </div>
                        </div>
                        <div class="occurrence-item">
                            <span class="occurrence-dot occurrence-dot-danger"></span>
                            <div>
                                <strong>Avaria registrada</strong>
                                <small>FRT-1016 · Cabo/PE · 15:40</small>
                            </div>
                        </div>
                    </div>
                </article>

                <article class="analytics-card">
                    <div class="analytics-card-header">
                        <div>
                            <span class="section-kicker">Prioridades</span>
                            <h2>Alertas operacionais</h2>
                        </div>
                    </div>
                    <div class="alert-stack">
                        <div class="operational-alert alert-warning">
                            <span>Veículos em manutenção</span>
                            <strong>${dashboardDTO.veiculosEmManutencao}</strong>
                        </div>
                        <div class="operational-alert alert-neutral">
                            <span>Fretes cancelados</span>
                            <strong>${dashboardDTO.fretesCancelados}</strong>
                        </div>
                        <div class="operational-alert alert-danger">
                            <span>Notificações não lidas</span>
                            <strong>${kpiExecutivoDTO.totalNotificacoesNaoLidas}</strong>
                        </div>
                        <div class="operational-alert alert-warning">
                            <span>Manutenções em andamento</span>
                            <strong>${kpiExecutivoDTO.totalManutencoesEmAndamento}</strong>
                        </div>
                    </div>
                </article>
            </section>
        </section>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
        const chartTextColor = "#1F2937";
        const chartMutedColor = "#6B7280";
        const chartGridColor = "#E5E7EB";

        Chart.defaults.font.family = '"Inter", "Segoe UI", Arial, Helvetica, sans-serif';
        Chart.defaults.color = chartMutedColor;
        Chart.defaults.plugins.legend.labels.boxWidth = 10;
        Chart.defaults.plugins.legend.labels.boxHeight = 10;

        new Chart(document.getElementById("fretesStatusChart"), {
            type: "bar",
            data: {
                labels: ["Em trânsito", "Entregue", "Cancelado", "Outros"],
                datasets: [{
                    label: "Fretes",
                    data: [
                        ${kpiExecutivoDTO.fretesEmTransito},
                        ${kpiExecutivoDTO.fretesEntregues},
                        ${kpiExecutivoDTO.fretesCancelados},
                        Math.max(${kpiExecutivoDTO.totalFretes} - ${kpiExecutivoDTO.fretesEmTransito} - ${kpiExecutivoDTO.fretesEntregues} - ${kpiExecutivoDTO.fretesCancelados}, 0)
                    ],
                    backgroundColor: ["#2563EB", "#16A34A", "#6B7280", "#D97706"],
                    borderRadius: 6,
                    maxBarThickness: 34
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: { backgroundColor: chartTextColor }
                },
                scales: {
                    x: { grid: { display: false }, ticks: { color: chartMutedColor } },
                    y: { beginAtZero: true, grid: { color: chartGridColor }, ticks: { precision: 0 } }
                }
            }
        });

        new Chart(document.getElementById("faturasStatusChart"), {
            type: "doughnut",
            data: {
                labels: ["Pendentes", "Vencidas", "Outras"],
                datasets: [{
                    data: [
                        ${kpiExecutivoDTO.faturasPendentes},
                        ${kpiExecutivoDTO.faturasVencidas},
                        Math.max(${kpiExecutivoDTO.totalFaturas} - ${kpiExecutivoDTO.faturasPendentes} - ${kpiExecutivoDTO.faturasVencidas}, 0)
                    ],
                    backgroundColor: ["#D97706", "#DC2626", "#2D8CFF"],
                    borderColor: "#FFFFFF",
                    borderWidth: 4,
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: "68%",
                plugins: {
                    legend: { position: "bottom" },
                    tooltip: { backgroundColor: chartTextColor }
                }
            }
        });

        new Chart(document.getElementById("veiculosStatusChart"), {
            type: "doughnut",
            data: {
                labels: ["Disponíveis", "Em manutenção", "Em rota/Inativos"],
                datasets: [{
                    data: [
                        ${dashboardDTO.veiculosDisponiveis},
                        ${dashboardDTO.veiculosEmManutencao},
                        Math.max(${dashboardDTO.totalVeiculos} - ${dashboardDTO.veiculosDisponiveis} - ${dashboardDTO.veiculosEmManutencao}, 0)
                    ],
                    backgroundColor: ["#16A34A", "#D97706", "#2D8CFF"],
                    borderColor: "#FFFFFF",
                    borderWidth: 4,
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: "68%",
                plugins: {
                    legend: { position: "bottom" },
                    tooltip: { backgroundColor: chartTextColor }
                }
            }
        });

        new Chart(document.getElementById("indicadoresOperacionaisChart"), {
            type: "bar",
            data: {
                labels: ["Clientes", "Veículos", "Motoristas", "Ocorrências", "Notificações", "Manutenções"],
                datasets: [{
                    label: "Total",
                    data: [
                        ${kpiExecutivoDTO.totalClientes},
                        ${kpiExecutivoDTO.totalVeiculos},
                        ${kpiExecutivoDTO.totalMotoristas},
                        ${kpiExecutivoDTO.totalOcorrencias},
                        ${kpiExecutivoDTO.totalNotificacoesNaoLidas},
                        ${kpiExecutivoDTO.totalManutencoesEmAndamento}
                    ],
                    backgroundColor: ["#2D8CFF", "#16A34A", "#0B2344", "#D97706", "#DC2626", "#6B7280"],
                    borderRadius: 6,
                    maxBarThickness: 32
                }]
            },
            options: {
                indexAxis: "y",
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: { backgroundColor: chartTextColor }
                },
                scales: {
                    x: { beginAtZero: true, grid: { color: chartGridColor }, ticks: { precision: 0 } },
                    y: { grid: { display: false }, ticks: { color: chartMutedColor } }
                }
            }
        });
    </script>
</body>
</html>
