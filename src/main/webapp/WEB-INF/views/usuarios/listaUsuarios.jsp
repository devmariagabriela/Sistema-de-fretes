<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Usuários</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510-theme">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-ui"></script>
</head>
<body class="theme-dark">
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="usuarios" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Administração corporativa</span>
                    <h1>Gestão de usuários</h1>
                    <p>Controle interno de acessos, perfis e status operacionais do GW FRETE.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-primary" href="${pageContext.request.contextPath}/usuarios/novo">Novo usuário</a>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card filter-panel" aria-label="Filtros de usuários">
                <div class="filter-panel-header">
                    <div>
                        <span class="summary-label">Filtros</span>
                        <h2>Consultar usuários</h2>
                    </div>
                    <p>Busque por nome, e-mail, perfil ou status de acesso.</p>
                </div>

                <form class="report-filters-form" action="${pageContext.request.contextPath}/usuarios" method="get">
                    <div class="form-grid report-filters-grid">
                        <div class="form-field">
                            <label for="nome">Nome</label>
                            <input id="nome" name="nome" type="text" value="${nomeFiltro}" placeholder="Nome do usuário">
                        </div>

                        <div class="form-field">
                            <label for="email">E-mail</label>
                            <input id="email" name="email" type="text" value="${emailFiltro}" placeholder="usuario@email.com">
                        </div>

                        <div class="form-field">
                            <label for="perfil">Perfil</label>
                            <select id="perfil" name="perfil">
                                <option value="">Todos os perfis</option>
                                <c:forEach var="perfil" items="${perfis}">
                                    <option value="${perfil.name()}" ${perfilFiltro == perfil.name() ? 'selected' : ''}>${perfil.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="">Todos os status</option>
                                <c:forEach var="status" items="${statusUsuarios}">
                                    <option value="${status.name()}" ${statusFiltro == status.name() ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="report-filters-actions">
                        <button class="button button-primary" type="submit">Consultar</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/usuarios">Limpar filtros</a>
                    </div>
                </form>
            </section>

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
                                            <c:if test="${usuario.status.name() == 'ATIVO'}">
                                                <button class="button button-danger" type="button"
                                                        data-soft-delete-button
                                                        data-action="${pageContext.request.contextPath}/usuarios/inativar"
                                                        data-id="${usuario.id}"
                                                        data-title="Desativar usuário"
                                                        data-message="Deseja desativar o acesso de ${usuario.nome}?"
                                                        data-submit="Desativar">Desativar</button>
                                            </c:if>
                                            <c:if test="${usuario.status.name() == 'INATIVO' || usuario.status.name() == 'BLOQUEADO'}">
                                                <button class="button button-secondary" type="button"
                                                        data-soft-delete-button
                                                        data-action="${pageContext.request.contextPath}/usuarios/ativar"
                                                        data-id="${usuario.id}"
                                                        data-title="Ativar usuário"
                                                        data-message="Deseja liberar novamente o acesso de ${usuario.nome}?"
                                                        data-submit="Ativar">Ativar</button>
                                            </c:if>
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
    <jsp:include page="/WEB-INF/views/includes/confirmacaoExclusao.jsp" />
</body>
</html>
