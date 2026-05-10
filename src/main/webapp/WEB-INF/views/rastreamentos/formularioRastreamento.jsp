<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Novo rastreamento</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510">
</head>
<body>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="rastreamentos" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Rastreamento operacional</span>
                    <h1>Novo ponto de rastreamento</h1>
                    <p>Registre uma localização para compor o histórico do frete.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/rastreamentos">Voltar</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card form-card" aria-label="Formulário de rastreamento">
                <form action="${acaoFormulario}" method="post">
                    <div class="form-grid">
                        <div class="form-field">
                            <label for="freteId">Frete</label>
                            <select id="freteId" name="freteId" required>
                                <option value="">Selecione</option>
                                <c:forEach var="frete" items="${fretes}">
                                    <option value="${frete.id}" ${not empty rastreamento.frete and rastreamento.frete.id == frete.id ? 'selected' : ''}>${frete.codigo} - ${frete.origem} / ${frete.destino}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="dataHora">Data/hora</label>
                            <input id="dataHora" name="dataHora" type="datetime-local" value="${rastreamento.dataHora}" required>
                        </div>

                        <div class="form-field">
                            <label for="localizacao">Localização</label>
                            <input id="localizacao" name="localizacao" type="text" value="${rastreamento.localizacao}" maxlength="255" required>
                        </div>

                        <div class="form-field">
                            <label for="latitude">Latitude</label>
                            <input id="latitude" name="latitude" type="number" value="${rastreamento.latitude}" step="0.0000001">
                        </div>

                        <div class="form-field">
                            <label for="longitude">Longitude</label>
                            <input id="longitude" name="longitude" type="number" value="${rastreamento.longitude}" step="0.0000001">
                        </div>

                        <div class="form-field form-field-wide">
                            <label for="observacao">Observação</label>
                            <textarea id="observacao" name="observacao" rows="4">${rastreamento.observacao}</textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button class="button button-primary" type="submit">Salvar ponto</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/rastreamentos">Cancelar</a>
                    </div>
                </form>
            </section>
        </section>
    </main>
</body>
</html>
