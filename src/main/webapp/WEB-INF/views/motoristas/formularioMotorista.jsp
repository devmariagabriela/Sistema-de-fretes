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
            <jsp:param name="ativo" value="motoristas" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Cadastro operacional</span>
                    <h1>${tituloFormulario}</h1>
                    <p>Dados cadastrais, vínculo e documentação do condutor para operação logística.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/motoristas">Voltar</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card form-card" aria-label="Formulário de motorista">
                <form class="user-form" action="${acaoFormulario}" method="post" autocomplete="off">
                    <c:if test="${not empty motorista.id}">
                        <input type="hidden" name="id" value="${motorista.id}">
                    </c:if>

                    <div class="form-grid">
                        <div class="form-field form-field-full">
                            <label for="nome">Nome</label>
                            <input id="nome" name="nome" type="text" value="${motorista.nome}" maxlength="150" required>
                        </div>

                        <div class="form-field">
                            <label for="cpf">CPF</label>
                            <input id="cpf" name="cpf" type="text" value="${motorista.cpf}" maxlength="14" inputmode="numeric" placeholder="00000000000" required>
                        </div>

                        <div class="form-field">
                            <label for="dataNascimento">Data de nascimento</label>
                            <input id="dataNascimento" name="dataNascimento" type="date" value="${motorista.dataNascimento}" required>
                        </div>

                        <div class="form-field">
                            <label for="telefone">Telefone</label>
                            <input id="telefone" name="telefone" type="tel" value="${motorista.telefone}" maxlength="20" placeholder="(00) 00000-0000">
                        </div>

                        <div class="form-field">
                            <label for="cnhNumero">Número CNH</label>
                            <input id="cnhNumero" name="cnhNumero" type="text" value="${motorista.cnhNumero}" maxlength="20" inputmode="numeric" required>
                        </div>

                        <div class="form-field">
                            <label for="cnhCategoria">Categoria CNH</label>
                            <select id="cnhCategoria" name="cnhCategoria" required>
                                <option value="">Selecione</option>
                                <c:forEach var="categoria" items="${categoriasCnh}">
                                    <option value="${categoria}" ${motorista.cnhCategoria == categoria ? 'selected' : ''}>${categoria.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="cnhValidade">Validade CNH</label>
                            <input id="cnhValidade" name="cnhValidade" type="date" value="${motorista.cnhValidade}" required>
                            <span class="field-help">CNH vencida pode ser cadastrada, mas não deve ser usada em fretes futuros.</span>
                        </div>

                        <div class="form-field">
                            <label for="tipoVinculo">Vínculo</label>
                            <select id="tipoVinculo" name="tipoVinculo" required>
                                <option value="">Selecione</option>
                                <c:forEach var="vinculo" items="${tiposVinculo}">
                                    <option value="${vinculo}" ${motorista.tipoVinculo == vinculo ? 'selected' : ''}>${vinculo.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status" required>
                                <option value="">Selecione</option>
                                <c:forEach var="status" items="${statusMotorista}">
                                    <option value="${status}" ${motorista.status == status ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/motoristas">Cancelar</a>
                        <button class="button button-primary" type="submit">Salvar motorista</button>
                    </div>
                </form>
            </section>
        </section>
    </main>
</body>
</html>
