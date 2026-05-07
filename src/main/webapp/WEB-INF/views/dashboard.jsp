<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
                    <h1>Dashboard operacional</h1>
                </div>
                <div class="user-summary">
                    <span>${sessionScope.usuarioLogado.nome}</span>
                    <strong>${sessionScope.usuarioLogado.perfil.descricao}</strong>
                    <a href="${pageContext.request.contextPath}/logout">Sair</a>
                </div>
            </header>

            <div class="kpi-grid">
                <article>
                    <span>Fretes ativos</span>
                    <strong>128</strong>
                </article>
                <article>
                    <span>Em trânsito</span>
                    <strong>74</strong>
                </article>
                <article>
                    <span>Entregues hoje</span>
                    <strong>86</strong>
                </article>
                <article>
                    <span>Atrasados</span>
                    <strong>9</strong>
                </article>
            </div>
        </section>
    </main>
</body>
</html>
