<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | ${tituloFormulario}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260511-voice-access">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260511-voice-access"></script>
    <script defer src="${pageContext.request.contextPath}/assets/js/mascaras.js?v=mask-20260511-doc-recebedor"></script>
</head>
<body class="theme-dark">
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
                <form action="${acaoFormulario}" method="post" data-mask-form="cliente">
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
                            <input
                                id="cpfCnpj"
                                name="cpfCnpj"
                                type="text"
                                value="${cliente.cpfCnpj}"
                                maxlength="18"
                                inputmode="numeric"
                                autocomplete="off"
                                aria-describedby="cpfCnpjFeedback"
                                required>
                            <small id="cpfCnpjFeedback" class="field-feedback" data-field-feedback="cpfCnpj" hidden></small>
                        </div>

                        <div class="form-field">
                            <label for="email">E-mail</label>
                            <input id="email" name="email" type="email" value="${cliente.email}" maxlength="120">
                        </div>

                        <div class="form-field">
                            <label for="telefone">Telefone</label>
                            <input id="telefone" name="telefone" type="text" value="${cliente.telefone}" maxlength="15" inputmode="numeric">
                        </div>

                        <div class="form-field">
                            <label for="contato">Responsável</label>
                            <input id="contato" name="contato" type="text" value="${cliente.contato}" maxlength="100" placeholder="Nome da pessoa responsável">
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
                            <select id="estado" name="estado" autocomplete="address-level1">
                                <option value="">Selecione</option>
                                <option value="AC" ${cliente.estado == 'AC' ? 'selected' : ''}>AC</option>
                                <option value="AL" ${cliente.estado == 'AL' ? 'selected' : ''}>AL</option>
                                <option value="AP" ${cliente.estado == 'AP' ? 'selected' : ''}>AP</option>
                                <option value="AM" ${cliente.estado == 'AM' ? 'selected' : ''}>AM</option>
                                <option value="BA" ${cliente.estado == 'BA' ? 'selected' : ''}>BA</option>
                                <option value="CE" ${cliente.estado == 'CE' ? 'selected' : ''}>CE</option>
                                <option value="DF" ${cliente.estado == 'DF' ? 'selected' : ''}>DF</option>
                                <option value="ES" ${cliente.estado == 'ES' ? 'selected' : ''}>ES</option>
                                <option value="GO" ${cliente.estado == 'GO' ? 'selected' : ''}>GO</option>
                                <option value="MA" ${cliente.estado == 'MA' ? 'selected' : ''}>MA</option>
                                <option value="MT" ${cliente.estado == 'MT' ? 'selected' : ''}>MT</option>
                                <option value="MS" ${cliente.estado == 'MS' ? 'selected' : ''}>MS</option>
                                <option value="MG" ${cliente.estado == 'MG' ? 'selected' : ''}>MG</option>
                                <option value="PA" ${cliente.estado == 'PA' ? 'selected' : ''}>PA</option>
                                <option value="PB" ${cliente.estado == 'PB' ? 'selected' : ''}>PB</option>
                                <option value="PR" ${cliente.estado == 'PR' ? 'selected' : ''}>PR</option>
                                <option value="PE" ${cliente.estado == 'PE' ? 'selected' : ''}>PE</option>
                                <option value="PI" ${cliente.estado == 'PI' ? 'selected' : ''}>PI</option>
                                <option value="RJ" ${cliente.estado == 'RJ' ? 'selected' : ''}>RJ</option>
                                <option value="RN" ${cliente.estado == 'RN' ? 'selected' : ''}>RN</option>
                                <option value="RS" ${cliente.estado == 'RS' ? 'selected' : ''}>RS</option>
                                <option value="RO" ${cliente.estado == 'RO' ? 'selected' : ''}>RO</option>
                                <option value="RR" ${cliente.estado == 'RR' ? 'selected' : ''}>RR</option>
                                <option value="SC" ${cliente.estado == 'SC' ? 'selected' : ''}>SC</option>
                                <option value="SP" ${cliente.estado == 'SP' ? 'selected' : ''}>SP</option>
                                <option value="SE" ${cliente.estado == 'SE' ? 'selected' : ''}>SE</option>
                                <option value="TO" ${cliente.estado == 'TO' ? 'selected' : ''}>TO</option>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="cep">CEP</label>
                            <input id="cep" name="cep" type="text" value="${cliente.cep}" maxlength="9" inputmode="numeric" autocomplete="postal-code">
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
