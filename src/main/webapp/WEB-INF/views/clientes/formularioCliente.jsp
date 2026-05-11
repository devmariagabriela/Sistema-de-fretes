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
            <jsp:param name="ativo" value="clientes" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Base cadastral</span>
                    <h1>${tituloFormulario}</h1>
                    <p>Informe os dados cadastrais do cliente para uso operacional no sistema.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/clientes">Voltar</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card form-card" aria-label="Formulário de cliente">
                <form action="${acaoFormulario}" method="post">
                    <c:if test="${not empty cliente.id}">
                        <input type="hidden" name="id" value="${cliente.id}">
                    </c:if>

                    <div class="form-grid">
                        <div class="form-field">
                            <label for="nome">Nome</label>
                            <input id="nome" name="nome" type="text" value="${cliente.nome}" maxlength="150" required>
                        </div>

                        <div class="form-field">
                            <label for="tipo">Tipo</label>
                            <select id="tipo" name="tipo" required>
                                <option value="">Selecione</option>
                                <c:forEach var="tipo" items="${tiposCliente}">
                                    <option value="${tipo}" ${cliente.tipo == tipo ? 'selected' : ''}>${tipo.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="cpfCnpj">CPF/CNPJ</label>
                            <input id="cpfCnpj" name="cpfCnpj" type="text" value="${cliente.cpfCnpj}" maxlength="18" required>
                        </div>

                        <div class="form-field">
                            <label for="email">E-mail</label>
                            <input id="email" name="email" type="email" value="${cliente.email}" maxlength="120">
                        </div>

                        <div class="form-field">
                            <label for="telefone">Telefone</label>
                            <input id="telefone" name="telefone" type="text" value="${cliente.telefone}" maxlength="20">
                        </div>

                        <div class="form-field">
                            <label for="contato">Contato</label>
                            <input id="contato" name="contato" type="text" value="${cliente.contato}" maxlength="100">
                        </div>

                        <div class="form-field form-field-wide">
                            <label for="endereco">Endereço</label>
                            <input id="endereco" name="endereco" type="text" value="${cliente.endereco}" maxlength="255">
                        </div>

                        <div class="form-field">
                            <label for="cidade">Cidade</label>
                            <input id="cidade" name="cidade" type="text" value="${cliente.cidade}" maxlength="100">
                        </div>

                        <div class="form-field">
                            <label for="estado">Estado</label>
                            <input id="estado" name="estado" type="text" value="${cliente.estado}" maxlength="2">
                        </div>

                        <div class="form-field">
                            <label for="cep">CEP</label>
                            <input id="cep" name="cep" type="text" value="${cliente.cep}" maxlength="9">
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="true" ${cliente.status == true ? 'selected' : ''}>Ativo</option>
                                <option value="false" ${cliente.status == false ? 'selected' : ''}>Inativo</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button class="button button-primary" type="submit">Salvar</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/clientes">Cancelar</a>
                    </div>
                </form>
            </section>
        </section>
    </main>
</body>
</html>
