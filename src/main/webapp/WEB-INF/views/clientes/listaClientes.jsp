<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Clientes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260511-voice-access">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260511-voice-access"></script>
</head>
<body class="theme-dark">
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="clientes" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Base cadastral</span>
                    <h1>Clientes</h1>
                    <p>Cadastro de pessoas físicas e jurídicas para vínculo operacional com fretes.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <c:if test="${podeGerenciarClientes}">
                        <a class="button button-primary" href="${pageContext.request.contextPath}/clientes/novo">Novo cliente</a>
                    </c:if>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>
            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card filter-panel" aria-label="Filtros de clientes">
                <div class="filter-panel-header">
                    <div>
                        <span class="summary-label">Filtros</span>
                        <h2>Consultar clientes</h2>
                    </div>
                    <p>Refine a base por nome, documento, tipo, cidade ou status cadastral.</p>
                </div>

                <form class="report-filters-form" action="${pageContext.request.contextPath}/clientes" method="get">
                    <div class="form-grid report-filters-grid">
                        <div class="form-field">
                            <label for="nome">Nome</label>
                            <input id="nome" name="nome" type="text" value="${nomeFiltro}" placeholder="Nome do cliente">
                        </div>

                        <div class="form-field">
                            <label for="cpfCnpj">CPF/CNPJ</label>
                            <input id="cpfCnpj" name="cpfCnpj" type="text" value="${cpfCnpjFiltro}" placeholder="Documento">
                        </div>

                        <div class="form-field">
                            <label for="tipo">Tipo</label>
                            <select id="tipo" name="tipo">
                                <option value="">Todos os tipos</option>
                                <c:forEach var="tipo" items="${tiposCliente}">
                                    <option value="${tipo.name()}" ${tipoFiltro == tipo.name() ? 'selected' : ''}>${tipo.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="cidade">Cidade</label>
                            <input id="cidade" name="cidade" type="text" value="${cidadeFiltro}" placeholder="Cidade">
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="">Todos os status</option>
                                <option value="true" ${statusFiltro == 'true' ? 'selected' : ''}>Ativo</option>
                                <option value="false" ${statusFiltro == 'false' ? 'selected' : ''}>Inativo</option>
                            </select>
                        </div>
                    </div>

                    <div class="report-filters-actions">
                        <button class="button button-primary" type="submit">Consultar</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/clientes">Limpar filtros</a>
                    </div>
                </form>
            </section>

            <section class="content-card" aria-label="Lista de clientes">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Nome</th>
                                <th>Tipo</th>
                                <th>CPF/CNPJ</th>
                                <th>Contato</th>
                                <th>Cidade/UF</th>
                                <th>Status</th>
                                <th>Cadastro</th>
                                <c:if test="${podeGerenciarClientes}">
                                    <th>Ações</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cliente" items="${clientes}">
                                <tr>
                                    <td><strong>${cliente.nome}</strong></td>
                                    <td>${cliente.tipo.descricao}</td>
                                    <td class="text-muted">${cliente.cpfCnpj}</td>
                                    <td class="text-muted">${cliente.email}</td>
                                    <td class="text-muted">${cliente.cidade}/${cliente.estado}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${cliente.status}">
                                                <span class="badge badge-ativo">Ativo</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-inativo">Inativo</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${cliente.dataCriacaoFormatada}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <c:if test="${podeGerenciarClientes}">
                                        <td>
                                            <div class="row-actions">
                                                <a class="button button-secondary" href="${pageContext.request.contextPath}/clientes/editar?id=${cliente.id}">Editar</a>
                                                <c:if test="${cliente.status}">
                                                    <button class="button button-danger" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/clientes/inativar"
                                                            data-id="${cliente.id}"
                                                            data-title="Inativar cliente"
                                                            data-message="Deseja inativar o cliente ${cliente.nome}?"
                                                            data-submit="Inativar">Inativar</button>
                                                </c:if>
                                                <c:if test="${!cliente.status}">
                                                    <button class="button button-secondary" type="button"
                                                            data-soft-delete-button
                                                            data-action="${pageContext.request.contextPath}/clientes/ativar"
                                                            data-id="${cliente.id}"
                                                            data-title="Ativar cliente"
                                                            data-message="Deseja ativar o cliente ${cliente.nome}?"
                                                            data-submit="Ativar">Ativar</button>
                                                </c:if>
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty clientes}">
                                <tr>
                                    <td colspan="${podeGerenciarClientes ? 8 : 7}">
                                        <div class="empty-state">Nenhum cliente cadastrado.</div>
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
