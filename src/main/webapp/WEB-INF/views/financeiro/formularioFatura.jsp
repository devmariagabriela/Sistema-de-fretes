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
            <jsp:param name="ativo" value="financeiro" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Contas a receber</span>
                    <h1>${tituloFormulario}</h1>
                    <p>Informe os dados da fatura vinculada ao frete e ao cliente.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/financeiro">Voltar</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card form-card" aria-label="Formulário de fatura">
                <form action="${acaoFormulario}" method="post">
                    <c:if test="${not empty fatura.id}">
                        <input type="hidden" name="id" value="${fatura.id}">
                    </c:if>

                    <div class="form-grid">
                        <div class="form-field">
                            <label for="numero">Número</label>
                            <input id="numero" name="numero" type="text" value="${fatura.numero}" maxlength="30" required>
                        </div>

                        <div class="form-field">
                            <label for="freteId">Frete</label>
                            <select id="freteId" name="freteId" required>
                                <option value="">Selecione</option>
                                <c:forEach var="frete" items="${fretes}">
                                    <option value="${frete.id}" ${not empty fatura.frete and fatura.frete.id == frete.id ? 'selected' : ''}>${frete.codigo} - ${frete.origem} / ${frete.destino}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="clienteId">Cliente</label>
                            <select id="clienteId" name="clienteId" required>
                                <option value="">Selecione</option>
                                <c:forEach var="cliente" items="${clientes}">
                                    <option value="${cliente.id}" ${not empty fatura.cliente and fatura.cliente.id == cliente.id ? 'selected' : ''}>${cliente.nome}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="valor">Valor</label>
                            <input id="valor" name="valor" type="number" min="0.01" step="0.01" value="${fatura.valor}" required>
                        </div>

                        <div class="form-field">
                            <label for="dataEmissao">Data de emissão</label>
                            <input id="dataEmissao" name="dataEmissao" type="date" value="${fatura.dataEmissao}" required>
                        </div>

                        <div class="form-field">
                            <label for="dataVencimento">Data de vencimento</label>
                            <input id="dataVencimento" name="dataVencimento" type="date" value="${fatura.dataVencimento}" required>
                        </div>

                        <div class="form-field">
                            <label for="dataPagamento">Data de pagamento</label>
                            <input id="dataPagamento" name="dataPagamento" type="date" value="${fatura.dataPagamento}">
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status" required>
                                <option value="">Selecione</option>
                                <c:forEach var="status" items="${statusFatura}">
                                    <option value="${status}" ${fatura.status == status ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field form-field-wide">
                            <label for="observacao">Observação</label>
                            <textarea id="observacao" name="observacao" rows="4">${fatura.observacao}</textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button class="button button-primary" type="submit">Salvar</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/financeiro">Cancelar</a>
                    </div>
                </form>
            </section>
        </section>
    </main>
</body>
</html>
