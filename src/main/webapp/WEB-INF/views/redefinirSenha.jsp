<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Redefinir senha</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css?v=login-20260510-login-theme">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-login"></script>
</head>
<body class="login-screen theme-dark">
    <main class="login-shell single-login-shell" aria-label="Redefinição de senha">
        <section class="login-area">
            <div class="login-card" aria-labelledby="reset-title">
                <div class="login-header">
                    <span class="access-badge">
                        <i></i>
                        NOVA SENHA
                    </span>
                    <h1 id="reset-title">Redefinir <span>senha</span></h1>
                    <p>Crie uma senha nova para voltar a acessar o sistema.</p>
                </div>

                <c:if test="${not empty mensagemErro}">
                    <div class="alert-error" role="alert">${mensagemErro}</div>
                </c:if>

                <c:choose>
                    <c:when test="${not empty token}">
                        <form class="login-form" action="${pageContext.request.contextPath}/redefinir-senha" method="post" autocomplete="off">
                            <input type="hidden" name="token" value="${token}">

                            <div class="form-field">
                                <label for="novaSenha">Nova senha</label>
                                <div class="input-wrap">
                                    <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                                        <rect x="5" y="10" width="14" height="10" rx="2"></rect>
                                        <path d="M8 10V7a4 4 0 0 1 8 0v3"></path>
                                    </svg>
                                    <input id="novaSenha" name="novaSenha" type="password" minlength="6" autocomplete="new-password" required>
                                </div>
                            </div>

                            <div class="form-field">
                                <label for="confirmacaoSenha">Confirmar senha</label>
                                <div class="input-wrap">
                                    <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                                        <rect x="5" y="10" width="14" height="10" rx="2"></rect>
                                        <path d="M8 10V7a4 4 0 0 1 8 0v3"></path>
                                    </svg>
                                    <input id="confirmacaoSenha" name="confirmacaoSenha" type="password" minlength="6" autocomplete="new-password" required>
                                </div>
                            </div>

                            <button class="primary-button" type="submit">
                                <span class="button-shimmer"></span>
                                <span class="button-label">
                                    <span>Salvar nova senha</span>
                                    <b>→</b>
                                </span>
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="restricted-box">
                            <a class="forgot-link" href="${pageContext.request.contextPath}/esqueci-senha">Solicitar novo link</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </main>
</body>
</html>
