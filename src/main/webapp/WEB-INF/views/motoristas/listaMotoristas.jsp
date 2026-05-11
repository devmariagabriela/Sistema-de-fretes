<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Motoristas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510-theme">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-ui"></script>
</head>
<body class="theme-dark">
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="motoristas" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Gestão operacional</span>
                    <h1>Motoristas</h1>
                    <p>Controle de condutores, CNH, vínculo e disponibilidade para operações logísticas.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                    <c:if test="${podeGerenciarMotoristas}">
                        <a class="button button-primary" href="${pageContext.request.contextPath}/motoristas/novo">Novo motorista</a>
                    </c:if>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card filter-panel" aria-label="Filtros de motoristas">
                <div class="filter-panel-header">
                    <div>
                        <span class="summary-label">Filtros</span>
                        <h2>Consultar motoristas</h2>
                    </div>
                    <p>Filtre condutores por identificação, CNH, categoria ou status.</p>
                </div>

                <form class="report-filters-form" action="${pageContext.request.contextPath}/motoristas" method="get">
                    <div class="form-grid report-filters-grid">
                        <div class="form-field">
                            <label for="nome">Nome</label>
                            <input id="nome" name="nome" type="text" value="${nomeFiltro}" placeholder="Nome do motorista">
                        </div>

                        <div class="form-field">
                            <label for="cpf">CPF</label>
                            <input id="cpf" name="cpf" type="text" value="${cpfFiltro}" placeholder="CPF">
                        </div>

                        <div class="form-field">
                            <label for="cnh">CNH</label>
                            <input id="cnh" name="cnh" type="text" value="${cnhFiltro}" placeholder="Número da CNH">
                        </div>

                        <div class="form-field">
                            <label for="categoriaCnh">Categoria CNH</label>
                            <select id="categoriaCnh" name="categoriaCnh">
                                <option value="">Todas as categorias</option>
                                <c:forEach var="categoria" items="${categoriasCnh}">
                                    <option value="${categoria.name()}" ${categoriaCnhFiltro == categoria.name() ? 'selected' : ''}>${categoria.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="">Todos os status</option>
                                <c:forEach var="status" items="${statusMotorista}">
                                    <option value="${status.name()}" ${statusFiltro == status.name() ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="report-filters-actions">
                        <button class="button button-primary" type="submit">Consultar</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/motoristas">Limpar filtros</a>
                    </div>
                </form>
            </section>

            <section class="content-card" aria-label="Motoristas cadastrados">
                <div class="table-wrap">
                    <table class="data-table driver-table">
                        <thead>
                            <tr>
                                <th>Nome</th>
                                <th>CPF</th>
                                <th>Categoria CNH</th>
                                <th>Validade CNH</th>
                                <th>Vínculo</th>
                                <th>Status</th>
                                <c:if test="${podeGerenciarMotoristas}">
                                    <th>Ações</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="motorista" items="${motoristas}">
                                <tr>
                                    <td>
                                        <strong>${motorista.nome}</strong>
                                    </td>
                                    <td>
                                        <span class="document-code">${motorista.cpf}</span>
                                    </td>
                                    <td>
                                        <span class="badge badge-cnh-${motorista.cnhCategoria.name().toLowerCase()}">${motorista.cnhCategoria.descricao}</span>
                                    </td>
                                    <td>
                                        <div class="stacked-cell">
                                            <span class="text-muted">
                                                <fmt:formatDate value="${motorista.cnhValidadeFormatada}" pattern="dd/MM/yyyy"/>
                                            </span>
                                            <c:if test="${motorista.cnhVencida}">
                                                <span class="badge badge-cnh-vencida">CNH vencida</span>
                                            </c:if>
                                        </div>
                                    </td>
                                    <td>
                                        <span class="badge badge-vinculo-${motorista.tipoVinculo.name().toLowerCase()}">${motorista.tipoVinculo.descricao}</span>
                                    </td>
                                    <td>
                                        <span class="badge badge-motorista-${motorista.status.name().toLowerCase()}">${motorista.status.descricao}</span>
                                    </td>
                                    <c:if test="${podeGerenciarMotoristas}">
                                        <td>
                                            <div class="row-actions">
                                                <a class="button button-secondary" href="${pageContext.request.contextPath}/motoristas/editar?id=${motorista.id}">Editar</a>
                                                <c:if test="${motorista.status.name() == 'ATIVO'}">
                                                    <button class="button button-danger" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/motoristas/inativar"
                                                            data-id="${motorista.id}"
                                                            data-title="Inativar motorista"
                                                            data-message="Deseja inativar o motorista ${motorista.nome}?"
                                                            data-submit="Inativar">Inativar</button>
                                                </c:if>
                                                <c:if test="${motorista.status.name() == 'INATIVO'}">
                                                    <button class="button button-secondary" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/motoristas/ativar"
                                                            data-id="${motorista.id}"
                                                            data-title="Ativar motorista"
                                                            data-message="Deseja ativar o motorista ${motorista.nome}?"
                                                            data-submit="Ativar">Ativar</button>
                                                </c:if>
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty motoristas}">
                                <tr>
                                    <td colspan="${podeGerenciarMotoristas ? 7 : 6}">
                                        <div class="empty-state">Nenhum motorista cadastrado.</div>
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
