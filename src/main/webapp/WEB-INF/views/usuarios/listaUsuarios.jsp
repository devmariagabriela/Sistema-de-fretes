<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Usuários</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="app-shell">
        <aside class="app-sidebar">
            <div class="app-brand">GW FRETE</div>
            <nav aria-label="Módulos principais">
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/usuarios" class="active">Usuários</a>
                <a href="#">Fretes</a>
                <a href="#">Ocorrências</a>
                <a href="#">Relatórios</a>
            </nav>
        </aside>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Administração corporativa</span>
                    <h1>Gestão de usuários</h1>
                    <p>Controle interno de acessos, perfis e status operacionais do GW FRETE.</p>
                </div>
                <div class="page-actions">
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                    <a class="button button-primary" href="${pageContext.request.contextPath}/usuarios/novo">Novo usuário</a>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card" aria-label="Usuários cadastrados">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Nome</th>
                                <th>E-mail</th>
                                <th>Perfil</th>
                                <th>Status</th>
                                <th>Data de criação</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="usuario" items="${usuarios}">
                                <tr>
                                    <td>
                                        <strong>${usuario.nome}</strong>
                                    </td>
                                    <td class="text-muted">${usuario.email}</td>
                                    <td>
                                        <span class="badge badge-${usuario.perfil.name().toLowerCase()}">${usuario.perfil.descricao}</span>
                                    </td>
                                    <td>
                                        <span class="badge badge-${usuario.status.name().toLowerCase()}">${usuario.status.descricao}</span>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${usuario.dataCriacaoFormatada}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <div class="row-actions">
                                            <a class="button button-secondary" href="${pageContext.request.contextPath}/usuarios/editar?id=${usuario.id}">Editar</a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty usuarios}">
                                <tr>
                                    <td colspan="6">
                                        <div class="empty-state">Nenhum usuário cadastrado.</div>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </section>
    </main>
</body>
</html>
