(function () {
    function onlyDigits(value) {
        return (value || "").replace(/\D/g, "");
    }

    function onlyLettersAndSpaces(value) {
        return (value || "").replace(/[^\p{L}\s]/gu, "").replace(/\s{2,}/g, " ");
    }

    function maskCpf(value) {
        var digits = onlyDigits(value).slice(0, 11);
        return digits
            .replace(/^(\d{3})(\d)/, "$1.$2")
            .replace(/^(\d{3})\.(\d{3})(\d)/, "$1.$2.$3")
            .replace(/^(\d{3})\.(\d{3})\.(\d{3})(\d)/, "$1.$2.$3-$4");
    }

    function maskCnpj(value) {
        var digits = onlyDigits(value).slice(0, 14);
        return digits
            .replace(/^(\d{2})(\d)/, "$1.$2")
            .replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3")
            .replace(/^(\d{2})\.(\d{3})\.(\d{3})(\d)/, "$1.$2.$3/$4")
            .replace(/^(\d{2})\.(\d{3})\.(\d{3})\/(\d{4})(\d)/, "$1.$2.$3/$4-$5");
    }

    function maskPhone(value) {
        var digits = onlyDigits(value).slice(0, 11);
        return digits
            .replace(/^(\d{2})(\d)/, "($1) $2")
            .replace(/^(\(\d{2}\)\s)(\d{5})(\d)/, "$1$2-$3")
            .replace(/^(\(\d{2}\)\s)(\d{4})(\d{1,4})$/, "$1$2-$3");
    }

    function maskCep(value) {
        return onlyDigits(value).slice(0, 8).replace(/^(\d{5})(\d)/, "$1-$2");
    }

    function showFieldMessage(field, message, isError) {
        if (!field) {
            return;
        }

        field.textContent = message || "";
        field.classList.toggle("field-feedback-error", Boolean(isError && message));
        field.hidden = !message;
    }

    function configureClienteForm(form) {
        var tipo = form.querySelector("#tipo");
        var cpfCnpj = form.querySelector("#cpfCnpj");
        var cpfField = cpfCnpj ? cpfCnpj.closest(".form-field") : null;
        var cpfFeedback = form.querySelector("[data-field-feedback='cpfCnpj']");
        var telefone = form.querySelector("#telefone");
        var cep = form.querySelector("#cep");
        var estado = form.querySelector("#estado");
        var nome = form.querySelector("#nome");
        var cidade = form.querySelector("#cidade");

        if (!tipo || !cpfCnpj) {
            return;
        }

        function selectedType() {
            return tipo.value || "";
        }

        function isPessoaFisica() {
            return selectedType() === "PESSOA_FISICA";
        }

        function isPessoaJuridica() {
            return selectedType() === "PESSOA_JURIDICA";
        }

        function configureCpfCnpj(options) {
            var shouldClear = options && options.clear;
            var type = selectedType();

            if (!type) {
                cpfCnpj.value = "";
                cpfCnpj.disabled = true;
                cpfCnpj.setCustomValidity("");
                cpfCnpj.maxLength = 18;
                cpfCnpj.placeholder = "Selecione o tipo primeiro";
                cpfCnpj.inputMode = "numeric";
                cpfCnpj.setAttribute("aria-describedby", "cpfCnpjFeedback");
                showFieldMessage(cpfFeedback, "", false);
                return;
            }

            cpfCnpj.disabled = false;
            cpfCnpj.inputMode = "numeric";
            cpfCnpj.maxLength = isPessoaFisica() ? 14 : 18;
            cpfCnpj.placeholder = isPessoaFisica() ? "000.000.000-00" : "00.000.000/0000-00";
            cpfCnpj.setAttribute("aria-describedby", "cpfCnpjFeedback");

            if (shouldClear) {
                cpfCnpj.value = "";
                cpfCnpj.setCustomValidity("");
                showFieldMessage(cpfFeedback, "Documento limpo para o novo tipo selecionado.", false);
                return;
            }

            cpfCnpj.value = isPessoaFisica() ? maskCpf(cpfCnpj.value) : maskCnpj(cpfCnpj.value);
            updateCpfCnpj();
        }

        function updateCpfCnpj() {
            var digits = onlyDigits(cpfCnpj.value);
            var maxDigits = isPessoaFisica() ? 11 : 14;
            cpfCnpj.value = isPessoaFisica() ? maskCpf(digits) : maskCnpj(digits);

            if (digits.length > 0 && digits.length < maxDigits) {
                var message = isPessoaFisica() ? "CPF deve conter 11 dígitos." : "CNPJ deve conter 14 dígitos.";
                cpfCnpj.setCustomValidity(message);
                showFieldMessage(cpfFeedback, message, true);
            } else {
                cpfCnpj.setCustomValidity("");
                showFieldMessage(cpfFeedback, "", false);
            }
        }

        tipo.addEventListener("change", function () {
            configureCpfCnpj({ clear: true });
            cpfCnpj.focus();
        });

        if (cpfField) {
            cpfField.addEventListener("click", function () {
                if (!selectedType()) {
                    showFieldMessage(cpfFeedback, "Selecione o tipo de cliente antes de preencher CPF/CNPJ.", true);
                    tipo.focus();
                }
            });
        }

        cpfCnpj.addEventListener("input", updateCpfCnpj);

        cpfCnpj.addEventListener("paste", function (event) {
            event.preventDefault();
            var text = (event.clipboardData || window.clipboardData).getData("text");
            cpfCnpj.value = onlyDigits(text);
            updateCpfCnpj();
        });

        if (telefone) {
            telefone.inputMode = "numeric";
            telefone.placeholder = "(00) 00000-0000";
            telefone.addEventListener("input", function () {
                telefone.value = maskPhone(telefone.value);
            });
            telefone.value = maskPhone(telefone.value);
        }

        if (cep) {
            cep.inputMode = "numeric";
            cep.placeholder = "00000-000";
            cep.addEventListener("input", function () {
                cep.value = maskCep(cep.value);
            });
            cep.value = maskCep(cep.value);
        }

        if (estado && estado.tagName !== "SELECT") {
            estado.maxLength = 2;
            estado.placeholder = "UF";
            estado.addEventListener("input", function () {
                estado.value = estado.value.replace(/[^a-zA-Z]/g, "").slice(0, 2).toUpperCase();
            });
            estado.value = estado.value.replace(/[^a-zA-Z]/g, "").slice(0, 2).toUpperCase();
        }

        if (nome) {
            nome.addEventListener("input", function () {
                nome.value = onlyLettersAndSpaces(nome.value);
            });
        }

        if (cidade) {
            cidade.addEventListener("input", function () {
                cidade.value = onlyLettersAndSpaces(cidade.value);
            });
        }

        form.addEventListener("submit", function () {
            cpfCnpj.disabled = false;
            cpfCnpj.value = onlyDigits(cpfCnpj.value);
        });

        configureCpfCnpj({ clear: false });
    }

    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll("[data-mask-form='cliente']").forEach(configureClienteForm);
    });
})();
