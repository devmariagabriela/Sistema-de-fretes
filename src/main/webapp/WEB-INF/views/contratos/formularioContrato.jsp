<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | ${tituloFormulario}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
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
                    <h1>${tituloFormulario}</h1>
                    <p>Informe cliente, vigência, valor mensal e situação do contrato.</p>
                </div>
                <div class="page-actions">
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/contratos">Voltar</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card form-card" aria-label="Formulário de contrato">
                <form action="${acaoFormulario}" method="post">
                    <c:if test="${not empty contrato.id}">
                        <input type="hidden" name="id" value="${contrato.id}">
                    </c:if>

                    <div class="form-grid">
                        <div class="form-field">
                            <label for="numero">Número</label>
                            <input id="numero" name="numero" type="text" value="${contrato.numero}" maxlength="30" required>
                        </div>

                        <div class="form-field">
                            <label for="clienteId">Cliente</label>
                            <select id="clienteId" name="clienteId" required>
                                <option value="">Selecione</option>
                                <c:forEach var="cliente" items="${clientes}">
                                    <option value="${cliente.id}" ${not empty contrato.cliente and contrato.cliente.id == cliente.id ? 'selected' : ''}>${cliente.nome}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="valorMensal">Valor mensal</label>
                            <input id="valorMensal" name="valorMensal" type="number" min="0.01" step="0.01" value="${contrato.valorMensal}" required>
                        </div>

                        <div class="form-field">
                            <label for="dataInicio">Data de início</label>
                            <input id="dataInicio" name="dataInicio" type="date" value="${contrato.dataInicio}" required>
                        </div>

                        <div class="form-field">
                            <label for="dataFim">Data de fim</label>
                            <input id="dataFim" name="dataFim" type="date" value="${contrato.dataFim}">
                        </div>

                        <div class="form-field">
                            <label for="reajustePercentual">Reajuste %</label>
                            <input id="reajustePercentual" name="reajustePercentual" type="number" step="0.01" value="${contrato.reajustePercentual}">
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status" required>
                                <option value="">Selecione</option>
                                <c:forEach var="status" items="${statusContrato}">
                                    <option value="${status}" ${contrato.status == status ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field form-field-wide">
                            <label for="descricao">Descrição</label>
                            <textarea id="descricao" name="descricao" rows="3">${contrato.descricao}</textarea>
                        </div>

                        <div class="form-field form-field-wide">
                            <label for="observacao">Observação</label>
                            <textarea id="observacao" name="observacao" rows="4">${contrato.observacao}</textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button class="button button-primary" type="submit">Salvar contrato</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/contratos">Cancelar</a>
                    </div>
                </form>
            </section>
        </section>
    </main>
</body>
</html>
