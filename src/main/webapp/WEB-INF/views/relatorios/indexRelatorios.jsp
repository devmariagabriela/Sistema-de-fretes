<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Central de Relatórios</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510-report-hub">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-ui"></script>
</head>
<body class="theme-dark">
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="relatorios" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Central gerencial</span>
                    <h1>Relatórios</h1>
                    <p>Escolha o tipo de relatório para consultar dados operacionais, financeiros e cadastrais do GW FRETE.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                </div>
            </header>

            <section class="report-hub-grid" aria-label="Tipos de relatórios disponíveis">
                <article class="content-card report-hub-card">
                    <span class="report-hub-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/><path d="M3.3 7 12 12l8.7-5"/><path d="M12 22V12"/></svg>
                    </span>
                    <h2>Relatório de Fretes</h2>
                    <p>Status, valores, motoristas, veículos e período operacional dos fretes.</p>
                    <a class="button button-primary" href="${pageContext.request.contextPath}/relatorios/fretes">Acessar relatório</a>
                </article>

                <article class="content-card report-hub-card">
                    <span class="report-hub-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M6 2h9l5 5v15H6z"/><path d="M14 2v6h6"/><path d="M9 13h6"/><path d="M9 17h6"/></svg>
                    </span>
                    <h2>Relatório de Contratos</h2>
                    <p>Carteira contratual por cliente, vigência, status e valor mensal.</p>
                    <a class="button button-primary" href="${pageContext.request.contextPath}/relatorios/contratos">Acessar relatório</a>
                </article>

                <article class="content-card report-hub-card">
                    <span class="report-hub-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M12 2v20"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7H14a3.5 3.5 0 0 1 0 7H6"/></svg>
                    </span>
                    <h2>Relatório Financeiro</h2>
                    <p>Receitas, faturas, vencimentos e visão financeira da operação.</p>
                    <a class="button button-primary" href="${pageContext.request.contextPath}/relatorios/financeiro">Acessar relatório</a>
                </article>

                <article class="content-card report-hub-card">
                    <span class="report-hub-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M14.7 6.3a5 5 0 0 0-6.9 6.9L3 18v3h3l4.8-4.8a5 5 0 0 0 6.9-6.9l-3 3-3-3 3-3z"/></svg>
                    </span>
                    <h2>Relatório de Manutenções</h2>
                    <p>Histórico, custos e situação de manutenção dos veículos.</p>
                    <a class="button button-primary" href="${pageContext.request.contextPath}/relatorios/manutencoes">Acessar relatório</a>
                </article>

                <article class="content-card report-hub-card">
                    <span class="report-hub-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M10.3 3.9 1.8 18a2 2 0 0 0 1.7 3h17a2 2 0 0 0 1.7-3L13.7 3.9a2 2 0 0 0-3.4 0z"/><path d="M12 9v4"/><path d="M12 17h.01"/></svg>
                    </span>
                    <h2>Relatório de Ocorrências</h2>
                    <p>Eventos, entregas, avarias, tentativas e ocorrências operacionais.</p>
                    <a class="button button-primary" href="${pageContext.request.contextPath}/relatorios/ocorrencias">Acessar relatório</a>
                </article>

                <article class="content-card report-hub-card">
                    <span class="report-hub-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><circle cx="12" cy="8" r="4"/><path d="M5 21a7 7 0 0 1 14 0"/></svg>
                    </span>
                    <h2>Relatório de Clientes</h2>
                    <p>Base de clientes, classificação e indicadores comerciais.</p>
                    <a class="button button-primary" href="${pageContext.request.contextPath}/relatorios/clientes">Acessar relatório</a>
                </article>

                <article class="content-card report-hub-card">
                    <span class="report-hub-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><circle cx="12" cy="7" r="4"/><path d="M6 21v-2a6 6 0 0 1 12 0v2"/><path d="M16 11l2 2 4-4"/></svg>
                    </span>
                    <h2>Relatório de Motoristas</h2>
                    <p>Motoristas, vínculos, CNH, disponibilidade e situação cadastral.</p>
                    <a class="button button-primary" href="${pageContext.request.contextPath}/relatorios/motoristas">Acessar relatório</a>
                </article>

                <article class="content-card report-hub-card">
                    <span class="report-hub-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24"><path d="M5 17h14l-1.4-5.2A3 3 0 0 0 14.7 10H9.3a3 3 0 0 0-2.9 1.8L5 17z"/><path d="M7 17v2"/><path d="M17 17v2"/><circle cx="8" cy="17" r="2"/><circle cx="16" cy="17" r="2"/></svg>
                    </span>
                    <h2>Relatório de Veículos</h2>
                    <p>Frota, status, capacidade, placas e dados cadastrais.</p>
                    <a class="button button-primary" href="${pageContext.request.contextPath}/relatorios/veiculos">Acessar relatório</a>
                </article>
            </section>
        </section>
    </main>
</body>
</html>
