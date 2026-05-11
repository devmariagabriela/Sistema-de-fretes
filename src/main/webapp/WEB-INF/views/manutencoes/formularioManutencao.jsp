<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | ${tituloFormulario}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510-notif">
</head>
<body>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="manutencoes" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Manutenção da frota</span>
                    <h1>${tituloFormulario}</h1>
                    <p>Registro operacional de serviços preventivos e corretivos realizados nos veículos.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/manutencoes">Voltar</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card form-card" aria-label="Formulário de manutenção">
                <form class="user-form" action="${acaoFormulario}" method="post" autocomplete="off">
                    <c:if test="${not empty manutencao.id}">
                        <input type="hidden" name="id" value="${manutencao.id}">
                    </c:if>

                    <div class="form-grid">
                        <div class="form-field">
                            <label for="veiculoId">Veículo</label>
                            <select id="veiculoId" name="veiculoId" required>
                                <option value="">Selecione</option>
                                <c:forEach var="veiculo" items="${veiculos}">
                                    <option value="${veiculo.id}" ${not empty manutencao.veiculo and manutencao.veiculo.id == veiculo.id ? 'selected' : ''}>
                                        ${veiculo.placa} - ${veiculo.modelo}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="tipo">Tipo</label>
                            <select id="tipo" name="tipo" required>
                                <option value="">Selecione</option>
                                <c:forEach var="tipo" items="${tiposManutencao}">
                                    <option value="${tipo}" ${manutencao.tipo == tipo ? 'selected' : ''}>${tipo.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status" required>
                                <option value="">Selecione</option>
                                <c:forEach var="status" items="${statusManutencao}">
                                    <option value="${status}" ${manutencao.status == status ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="dataAgendada">Data agendada</label>
                            <input id="dataAgendada" name="dataAgendada" type="date" value="${manutencao.dataAgendada}" required>
                        </div>

                        <div class="form-field">
                            <label for="dataInicio">Data de início</label>
                            <input id="dataInicio" name="dataInicio" type="date" value="${manutencao.dataInicio}">
                        </div>

                        <div class="form-field">
                            <label for="dataConclusao">Data de conclusão</label>
                            <input id="dataConclusao" name="dataConclusao" type="date" value="${manutencao.dataConclusao}">
                        </div>

                        <div class="form-field">
                            <label for="oficina">Oficina</label>
                            <input id="oficina" name="oficina" type="text" value="${manutencao.oficina}" maxlength="150">
                        </div>

                        <div class="form-field">
                            <label for="custo">Custo</label>
                            <input id="custo" name="custo" type="number" value="${manutencao.custo}" min="0" step="0.01">
                        </div>

                        <div class="form-field">
                            <label for="quilometragem">Quilometragem</label>
                            <input id="quilometragem" name="quilometragem" type="number" value="${manutencao.quilometragem}" min="0" step="1">
                        </div>

                        <div class="form-field form-field-wide">
                            <label for="descricao">Descrição</label>
                            <textarea id="descricao" name="descricao" rows="4" maxlength="1000" required>${manutencao.descricao}</textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/manutencoes">Cancelar</a>
                        <button class="button button-primary" type="submit">Salvar manutenção</button>
                    </div>
                </form>
            </section>
        </section>
    </main>
</body>
</html>
