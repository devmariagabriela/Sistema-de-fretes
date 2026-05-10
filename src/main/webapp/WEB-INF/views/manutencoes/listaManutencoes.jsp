<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Manutenções</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="app-shell">
        <aside class="app-sidebar">
            <div class="app-brand">GW FRETE</div>
            <nav aria-label="Módulos principais">
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/clientes">Clientes</a>
                <a href="${pageContext.request.contextPath}/usuarios">Usuários</a>
                <a href="${pageContext.request.contextPath}/veiculos">Veículos</a>
                <a href="${pageContext.request.contextPath}/manutencoes" class="active">Manutenções</a>
                <a href="${pageContext.request.contextPath}/fretes">Fretes</a>
                <a href="${pageContext.request.contextPath}/ocorrencias-frete">Ocorrências</a>
                <a href="${pageContext.request.contextPath}/relatorios/fretes">Relatórios</a>
            </nav>
        </aside>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Gestão de frota</span>
                    <h1>Manutenções de veículos</h1>
                    <p>Controle preventivo e corretivo das paradas técnicas da frota.</p>
                </div>
                <div class="page-actions">
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                    <c:if test="${podeGerenciarManutencoes}">
                        <a class="button button-primary" href="${pageContext.request.contextPath}/manutencoes/nova">Nova manutenção</a>
                    </c:if>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card" aria-label="Manutenções cadastradas">
                <div class="table-wrap">
                    <table class="data-table fleet-table">
                        <thead>
                            <tr>
                                <th>Veículo</th>
                                <th>Tipo</th>
                                <th>Status</th>
                                <th>Descrição</th>
                                <th>Oficina</th>
                                <th>Custo</th>
                                <th>Agendada</th>
                                <th>Conclusão</th>
                                <c:if test="${podeGerenciarManutencoes}">
                                    <th>Ações</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="manutencao" items="${manutencoes}">
                                <tr>
                                    <td>
                                        <strong class="plate-code">${manutencao.veiculo.placa}</strong>
                                        <span class="text-muted">${manutencao.veiculo.modelo}</span>
                                    </td>
                                    <td>
                                        <span class="badge">${manutencao.tipo.descricao}</span>
                                    </td>
                                    <td>
                                        <span class="badge badge-status-${manutencao.status.name().toLowerCase()}">${manutencao.status.descricao}</span>
                                    </td>
                                    <td>${manutencao.descricao}</td>
                                    <td class="text-muted">${empty manutencao.oficina ? '-' : manutencao.oficina}</td>
                                    <td class="text-muted">
                                        <c:choose>
                                            <c:when test="${not empty manutencao.custo}">
                                                R$ <fmt:formatNumber value="${manutencao.custo}" pattern="#,##0.00"/>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${manutencao.dataAgendadaFormatada}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td class="text-muted">
                                        <c:choose>
                                            <c:when test="${not empty manutencao.dataConclusaoFormatada}">
                                                <fmt:formatDate value="${manutencao.dataConclusaoFormatada}" pattern="dd/MM/yyyy"/>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <c:if test="${podeGerenciarManutencoes}">
                                        <td>
                                            <div class="row-actions">
                                                <a class="button button-secondary" href="${pageContext.request.contextPath}/manutencoes/editar?id=${manutencao.id}">Editar</a>
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty manutencoes}">
                                <tr>
                                    <td colspan="${podeGerenciarManutencoes ? 9 : 8}">
                                        <div class="empty-state">Nenhuma manutenção cadastrada.</div>
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
