<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Usuários</title>
</head>
<body>
    <main>
        <header>
            <h1>Gestão de usuários</h1>
            <p>Administração interna de acessos corporativos do GW FRETE.</p>
            <nav>
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/usuarios/novo">Novo usuário</a>
            </nav>
        </header>

        <c:if test="${not empty mensagemSucesso}">
            <p role="status">${mensagemSucesso}</p>
        </c:if>

        <c:if test="${not empty mensagemErro}">
            <p role="alert">${mensagemErro}</p>
        </c:if>

        <section aria-label="Usuários cadastrados">
            <table>
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>E-mail</th>
                        <th>Perfil</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="usuario" items="${usuarios}">
                        <tr>
                            <td>${usuario.nome}</td>
                            <td>${usuario.email}</td>
                            <td>${usuario.perfil.descricao}</td>
                            <td>${usuario.status.descricao}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/usuarios/editar?id=${usuario.id}">Editar</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty usuarios}">
                        <tr>
                            <td colspan="5">Nenhum usuário cadastrado.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </section>
    </main>
</body>
</html>
