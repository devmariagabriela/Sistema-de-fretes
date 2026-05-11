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
            <jsp:param name="ativo" value="veiculos" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Cadastro de frota</span>
                    <h1>${tituloFormulario}</h1>
                    <p>Dados operacionais para disponibilidade, capacidade e acompanhamento da frota.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/veiculos">Voltar</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card form-card" aria-label="Formulário de veículo">
                <form class="user-form" action="${acaoFormulario}" method="post" autocomplete="off">
                    <c:if test="${not empty veiculo.id}">
                        <input type="hidden" name="id" value="${veiculo.id}">
                    </c:if>

                    <div class="form-grid">
                        <div class="form-field">
                            <label for="placa">Placa</label>
                            <input id="placa" name="placa" type="text" value="${veiculo.placa}" maxlength="10" placeholder="ABC1D23" required>
                        </div>

                        <div class="form-field">
                            <label for="tipo">Tipo</label>
                            <select id="tipo" name="tipo" required>
                                <option value="">Selecione</option>
                                <c:forEach var="tipo" items="${tiposVeiculo}">
                                    <option value="${tipo}" ${veiculo.tipo == tipo ? 'selected' : ''}>${tipo.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="modelo">Modelo</label>
                            <input id="modelo" name="modelo" type="text" value="${veiculo.modelo}" maxlength="100" required>
                        </div>

                        <div class="form-field">
                            <label for="marca">Marca</label>
                            <input id="marca" name="marca" type="text" value="${veiculo.marca}" maxlength="100" required>
                        </div>

                        <div class="form-field">
                            <label for="ano">Ano</label>
                            <input id="ano" name="ano" type="number" value="${veiculo.ano}" min="1980" max="2100" required>
                        </div>

                        <div class="form-field">
                            <label for="capacidadeKg">Capacidade em kg</label>
                            <input id="capacidadeKg" name="capacidadeKg" type="number" value="${veiculo.capacidadeKg}" min="0.01" step="0.01" required>
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status" required>
                                <option value="">Selecione</option>
                                <c:forEach var="status" items="${statusVeiculo}">
                                    <option value="${status}" ${veiculo.status == status ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="quilometragem">Quilometragem</label>
                            <input id="quilometragem" name="quilometragem" type="number" value="${veiculo.quilometragem}" min="0" step="1" required>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/veiculos">Cancelar</a>
                        <button class="button button-primary" type="submit">Salvar veículo</button>
                    </div>
                </form>
            </section>
        </section>
    </main>
</body>
</html>
