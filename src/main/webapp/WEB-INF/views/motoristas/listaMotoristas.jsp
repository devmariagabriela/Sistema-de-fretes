<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Motoristas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="app-shell">
        <aside class="app-sidebar">
            <div class="app-brand">GW FRETE</div>
            <nav aria-label="Módulos principais">
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/usuarios">Usuários</a>
                <a href="${pageContext.request.contextPath}/motoristas" class="active">Motoristas</a>
                <a href="${pageContext.request.contextPath}/veiculos">Veículos</a>
                <a href="#">Fretes</a>
                <a href="#">Ocorrências</a>
                <a href="#">Relatórios</a>
            </nav>
        </aside>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Gestão operacional</span>
                    <h1>Motoristas</h1>
                    <p>Controle de condutores, CNH, vínculo e disponibilidade para operações logísticas.</p>
                </div>
                <div class="page-actions">
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
</body>
</html>
