<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | ${tituloFormulario}</title>
</head>
<body>
    <main>
        <header>
            <h1>${tituloFormulario}</h1>
            <p>Cadastro interno de usuários autorizados.</p>
            <nav>
                <a href="${pageContext.request.contextPath}/usuarios">Voltar para usuários</a>
            </nav>
        </header>

        <c:if test="${not empty mensagemErro}">
            <p role="alert">${mensagemErro}</p>
        </c:if>

        <form action="${acaoFormulario}" method="post" autocomplete="off">
            <c:if test="${not empty usuario.id}">
                <input type="hidden" name="id" value="${usuario.id}">
            </c:if>

            <div>
                <label for="nome">Nome</label>
                <input id="nome" name="nome" type="text" value="${usuario.nome}" maxlength="100" required>
            </div>

            <div>
                <label for="email">E-mail</label>
                <input id="email" name="email" type="email" value="${usuario.email}" maxlength="120" required>
            </div>

            <div>
                <label for="senha">Senha</label>
                <input id="senha" name="senha" type="password" maxlength="120" autocomplete="new-password" ${empty usuario.id ? 'required' : ''}>
                <c:if test="${not empty usuario.id}">
                    <small>Preencha apenas se desejar alterar a senha.</small>
                </c:if>
            </div>

            <div>
                <label for="perfil">Perfil</label>
                <select id="perfil" name="perfil" required>
                    <option value="">Selecione</option>
                    <c:forEach var="perfil" items="${perfis}">
                        <option value="${perfil}" ${usuario.perfil == perfil ? 'selected' : ''}>${perfil.descricao}</option>
                    </c:forEach>
                </select>
            </div>

            <div>
                <label for="status">Status</label>
                <select id="status" name="status" required>
                    <option value="">Selecione</option>
                    <c:forEach var="status" items="${statusUsuarios}">
                        <option value="${status}" ${usuario.status == status ? 'selected' : ''}>${status.descricao}</option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit">Salvar</button>
        </form>
    </main>
</body>
</html>
