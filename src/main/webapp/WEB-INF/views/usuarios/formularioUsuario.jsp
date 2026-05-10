<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | ${tituloFormulario}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510">
</head>
<body>
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="usuarios" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Acesso interno</span>
                    <h1>${tituloFormulario}</h1>
                    <p>Cadastro e manutenção de usuários autorizados para operação do TMS.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/usuarios">Voltar</a>
                </div>
            </header>

            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card form-card" aria-label="Formulário de usuário">
                <form class="user-form" action="${acaoFormulario}" method="post" autocomplete="off">
                    <c:if test="${not empty usuario.id}">
                        <input type="hidden" name="id" value="${usuario.id}">
                    </c:if>

                    <div class="form-grid">
                        <div class="form-field form-field-full">
                            <label for="nome">Nome</label>
                            <input id="nome" name="nome" type="text" value="${usuario.nome}" maxlength="100" required>
                        </div>

                        <div class="form-field">
                            <label for="email">E-mail</label>
                            <input id="email" name="email" type="email" value="${usuario.email}" maxlength="120" required>
                        </div>

                        <div class="form-field">
                            <label for="senha">Senha</label>
                            <input id="senha" name="senha" type="password" maxlength="120" autocomplete="new-password" ${empty usuario.id ? 'required' : ''}>
                            <c:if test="${not empty usuario.id}">
                                <span class="field-help">Preencha apenas se desejar alterar a senha.</span>
                            </c:if>
                        </div>

                        <div class="form-field">
                            <label for="perfil">Perfil</label>
                            <select id="perfil" name="perfil" required>
                                <option value="">Selecione</option>
                                <c:forEach var="perfil" items="${perfis}">
                                    <option value="${perfil}" ${usuario.perfil == perfil ? 'selected' : ''}>${perfil.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="status">Status</label>
                            <select id="status" name="status" required>
                                <option value="">Selecione</option>
                                <c:forEach var="status" items="${statusUsuarios}">
                                    <option value="${status}" ${usuario.status == status ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/usuarios">Cancelar</a>
                        <button class="button button-primary" type="submit">Salvar usuário</button>
                    </div>
                </form>
            </section>
        </section>
    </main>
</body>
</html>
