<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | ${tituloFormulario}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510-theme">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-ui"></script>
</head>
<body class="theme-dark">
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="fretes" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Emissão operacional</span>
                    <h1>${tituloFormulario}</h1>
                    <p>Dados de rota, carga, valores e recursos vinculados ao transporte.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/fretes">Voltar</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card form-card form-card-wide" aria-label="Formulário de frete">
                <form class="user-form" action="${acaoFormulario}" method="post" autocomplete="off">
                    <c:if test="${not empty frete.id}">
                        <input type="hidden" name="id" value="${frete.id}">
                    </c:if>

                    <div class="form-grid">
                        <div class="form-field">
                            <label for="codigo">Código</label>
                            <input id="codigo" name="codigo" type="text" value="${frete.codigo}" maxlength="20" placeholder="FRT-001" readonly required>
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status" required>
                                <option value="">Selecione</option>
                                <c:forEach var="status" items="${statusFrete}">
                                    <option value="${status}" ${frete.status == status ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="origem">Origem</label>
                            <input id="origem" name="origem" type="text" value="${frete.origem}" maxlength="255" required>
                        </div>

                        <div class="form-field">
                            <label for="destino">Destino</label>
                            <input id="destino" name="destino" type="text" value="${frete.destino}" maxlength="255" required>
                        </div>

                        <div class="form-field form-field-full">
                            <label for="descricaoCarga">Descrição da carga</label>
                            <textarea id="descricaoCarga" name="descricaoCarga" rows="4">${frete.descricaoCarga}</textarea>
                        </div>

                        <div class="form-field">
                            <label for="pesoKg">Peso em kg</label>
                            <input id="pesoKg" name="pesoKg" type="number" value="${frete.pesoKg}" min="0" step="0.01">
                        </div>

                        <div class="form-field">
                            <label for="valorFrete">Valor do frete</label>
                            <input id="valorFrete" name="valorFrete" type="number" value="${frete.valorFrete}" min="0" step="0.01">
                        </div>

                        <div class="form-field">
                            <label for="motoristaId">Motorista</label>
                            <select id="motoristaId" name="motoristaId" required>
                                <option value="">Selecione</option>
                                <c:forEach var="motorista" items="${motoristas}">
                                    <option value="${motorista.id}" ${frete.motorista.id == motorista.id ? 'selected' : ''}>${motorista.nome}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="veiculoId">Veículo</label>
                            <select id="veiculoId" name="veiculoId" required>
                                <option value="">Selecione</option>
                                <c:forEach var="veiculo" items="${veiculos}">
                                    <option value="${veiculo.id}" ${frete.veiculo.id == veiculo.id ? 'selected' : ''}>${veiculo.placa} - ${veiculo.modelo}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="dataSaida">Data saída</label>
                            <input id="dataSaida" name="dataSaida" type="datetime-local" value="${frete.dataSaida}">
                        </div>

                        <div class="form-field">
                            <label for="dataEntrega">Data entrega</label>
                            <input id="dataEntrega" name="dataEntrega" type="datetime-local" value="${frete.dataEntrega}">
                        </div>
                    </div>

                    <div class="form-actions">
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/fretes">Cancelar</a>
                        <button class="button button-primary" type="submit">Salvar frete</button>
                    </div>
                </form>
            </section>
        </section>
    </main>
</body>
</html>
