<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Recuperar senha</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css?v=login-20260510-login-theme">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-login"></script>
</head>
<body class="login-screen theme-dark">
    <main class="login-shell single-login-shell" aria-label="Recuperação de senha">
        <section class="login-area">
            <div class="login-card" aria-labelledby="forgot-title">
                <div class="login-header">
                    <span class="access-badge">
                        <i></i>
                        RECUPERAÇÃO SEGURA
                    </span>
                    <h1 id="forgot-title">Recuperar <span>senha</span></h1>
                    <p>Informe o e-mail cadastrado para receber as instruções de redefinição.</p>
                </div>

                <c:if test="${not empty mensagemSucesso}">
                    <div class="alert-success" role="status">${mensagemSucesso}</div>
                </c:if>

                <form class="login-form" action="${pageContext.request.contextPath}/recuperar-senha" method="post" autocomplete="on">
                    <div class="form-field">
                        <label for="email">E-mail</label>
                        <div class="input-wrap">
                            <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                                <path d="M4 6h16v12H4z"></path>
                                <path d="m4 7 8 6 8-6"></path>
                            </svg>
                            <input id="email" name="email" type="email" placeholder="seu.usuario@gwfrete.com" autocomplete="email" required>
                        </div>
                    </div>

                    <button class="primary-button" type="submit">
                        <span class="button-shimmer"></span>
                        <span class="button-label">
                            <span>Enviar instruções</span>
                            <b>→</b>
                        </span>
                    </button>
                </form>

                <div class="restricted-box">
                    <a class="forgot-link" href="${pageContext.request.contextPath}/login">Voltar para o login</a>
                </div>
            </div>
        </section>
    </main>
</body>
</html>
