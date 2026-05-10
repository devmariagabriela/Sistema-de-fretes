<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Contratos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <fmt:setLocale value="pt_BR"/>
    <main class="app-shell">
        <aside class="app-sidebar">
            <div class="app-brand">GW FRETE</div>
            <nav aria-label="Módulos principais">
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/clientes">Clientes</a>
                <a href="${pageContext.request.contextPath}/contratos" class="active">Contratos</a>
                <a href="${pageContext.request.contextPath}/motoristas">Motoristas</a>
                <a href="${pageContext.request.contextPath}/veiculos">Veículos</a>
                <a href="${pageContext.request.contextPath}/fretes">Fretes</a>
                <a href="${pageContext.request.contextPath}/financeiro">Financeiro</a>
                <a href="${pageContext.request.contextPath}/relatorios/fretes">Relatórios</a>
            </nav>
        </aside>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Gestão comercial</span>
                    <h1>Contratos</h1>
                    <p>Controle de contratos firmados com clientes, vigência, valores e status.</p>
                </div>
                <div class="page-actions">
                    <c:if test="${podeGerenciarContratos}">
                        <a class="button button-primary" href="${pageContext.request.contextPath}/contratos/novo">Novo contrato</a>
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

            <section class="content-card" aria-label="Lista de contratos">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Número</th>
                                <th>Cliente</th>
                                <th>Valor mensal</th>
                                <th>Início</th>
                                <th>Fim</th>
                                <th>Reajuste</th>
                                <th>Status</th>
                                <c:if test="${podeGerenciarContratos}">
                                    <th>Ações</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="contrato" items="${contratos}">
                                <tr>
                                    <td><strong>${contrato.numero}</strong></td>
                                    <td>${contrato.cliente.nome}</td>
                                    <td class="text-muted">
                                        <fmt:formatNumber value="${contrato.valorMensal}" type="currency"/>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${contrato.dataInicioFormatada}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td class="text-muted">
                                        <c:choose>
                                            <c:when test="${not empty contrato.dataFimFormatada}">
                                                <fmt:formatDate value="${contrato.dataFimFormatada}" pattern="dd/MM/yyyy"/>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-muted">
                                        <c:choose>
                                            <c:when test="${not empty contrato.reajustePercentual}">
                                                <fmt:formatNumber value="${contrato.reajustePercentual}" pattern="#,##0.00"/>%
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="badge badge-status-${contrato.status.name().toLowerCase()}">${contrato.status.descricao}</span>
                                    </td>
                                    <c:if test="${podeGerenciarContratos}">
                                        <td>
                                            <a class="button button-secondary" href="${pageContext.request.contextPath}/contratos/editar?id=${contrato.id}">Editar</a>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty contratos}">
                                <tr>
                                    <td colspan="${podeGerenciarContratos ? 8 : 7}">
                                        <div class="empty-state">Nenhum contrato cadastrado.</div>
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
