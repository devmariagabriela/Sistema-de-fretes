<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Clientes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="app-shell">
        <aside class="app-sidebar">
            <div class="app-brand">GW FRETE</div>
            <nav aria-label="Módulos principais">
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/clientes" class="active">Clientes</a>
                <a href="${pageContext.request.contextPath}/motoristas">Motoristas</a>
                <a href="${pageContext.request.contextPath}/veiculos">Veículos</a>
                <a href="${pageContext.request.contextPath}/fretes">Fretes</a>
                <a href="${pageContext.request.contextPath}/ocorrencias-frete">Ocorrências</a>
                <a href="${pageContext.request.contextPath}/relatorios/fretes">Relatórios</a>
            </nav>
        </aside>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Base cadastral</span>
                    <h1>Clientes</h1>
                    <p>Cadastro de pessoas físicas e jurídicas para vínculo operacional com fretes.</p>
                </div>
                <div class="page-actions">
                    <c:if test="${podeGerenciarClientes}">
                        <a class="button button-primary" href="${pageContext.request.contextPath}/clientes/novo">Novo cliente</a>
                    </c:if>
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>
            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

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
                                            <a class="button button-secondary" href="${pageContext.request.contextPath}/clientes/editar?id=${cliente.id}">Editar</a>
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
</body>
</html>
