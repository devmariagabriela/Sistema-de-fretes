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
            <jsp:param name="ativo" value="ocorrencias" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Registro de rastreamento</span>
                    <h1>${tituloFormulario}</h1>
                    <p>Atualize o histórico operacional do frete com localização, evento e dados de entrega.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/ocorrencias-frete">Voltar</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card form-card form-card-wide" aria-label="Formulário de ocorrência de frete">
                <form class="user-form" action="${acaoFormulario}" method="post" autocomplete="off">
                    <div class="form-grid">
                        <div class="form-field">
                            <label for="freteId">Frete</label>
                            <select id="freteId" name="freteId" required>
                                <option value="">Selecione</option>
                                <c:forEach var="frete" items="${fretes}">
                                    <option value="${frete.id}" ${ocorrencia.frete.id == frete.id ? 'selected' : ''}>
                                        ${frete.codigo} - ${frete.origem} → ${frete.destino}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="tipo">Tipo ocorrência</label>
                            <select id="tipo" name="tipo" required>
                                <option value="">Selecione</option>
                                <c:forEach var="tipo" items="${tiposOcorrencia}">
                                    <option value="${tipo}" ${ocorrencia.tipo == tipo ? 'selected' : ''}>${tipo.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="dataHora">Data/hora</label>
                            <input id="dataHora" name="dataHora" type="datetime-local" value="${ocorrencia.dataHora}" required>
                        </div>

                        <div class="form-field">
                            <label for="localizacao">Localização</label>
                            <input id="localizacao" name="localizacao" type="text" value="${ocorrencia.localizacao}" maxlength="255" required>
                        </div>

                        <div class="form-field form-field-full">
                            <label for="descricao">Descrição</label>
                            <textarea id="descricao" name="descricao" rows="4">${ocorrencia.descricao}</textarea>
                            <span class="field-help">Obrigatória para avaria, extravio e outros.</span>
                        </div>

                        <div class="form-field">
                            <label for="nomeRecebedor">Nome recebedor</label>
                            <input id="nomeRecebedor" name="nomeRecebedor" type="text" value="${ocorrencia.nomeRecebedor}" maxlength="150">
                        </div>

                        <div class="form-field">
                            <label for="documentoRecebedor">Documento recebedor</label>
                            <input id="documentoRecebedor" name="documentoRecebedor" type="text" value="${ocorrencia.documentoRecebedor}" maxlength="50">
                        </div>
                    </div>

                    <div class="form-actions">
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/ocorrencias-frete">Cancelar</a>
                        <button class="button button-primary" type="submit">Salvar ocorrência</button>
                    </div>
                </form>
            </section>
        </section>
    </main>
</body>
</html>
