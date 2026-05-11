(function () {
    if (window.GWFreteMascarasLoaded) {
        return;
    }
    window.GWFreteMascarasLoaded = true;

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

    function maskPlate(value) {
        var raw = (value || "").replace(/[^a-zA-Z0-9]/g, "").toUpperCase().slice(0, 7);
        if (raw.length <= 3) {
            return raw;
        }
        return raw.slice(0, 3) + "-" + raw.slice(3);
    }

    function maskInteger(value, maxLength) {
        var digits = onlyDigits(value);
        return maxLength ? digits.slice(0, maxLength) : digits;
    }

    function maskDecimal(value) {
        var normalized = (value || "").replace(/[^\d,.]/g, "").replace(/\./g, ",");
        var parts = normalized.split(",");
        var integerPart = parts.shift() || "";
        var decimalPart = parts.join("").slice(0, 2);
        return decimalPart ? integerPart + "," + decimalPart : integerPart;
    }

    function maskMoney(value) {
        var digits = onlyDigits(value);
        if (!digits) {
            return "";
        }

        while (digits.length < 3) {
            digits = "0" + digits;
        }

        var integerPart = digits.slice(0, -2).replace(/^0+(?=\d)/, "");
        var decimalPart = digits.slice(-2);
        integerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
        return integerPart + "," + decimalPart;
    }

    function getDocumentType(input) {
        var sourceId = input.getAttribute("data-mask-type-source");
        var source = sourceId ? document.getElementById(sourceId) : null;
        return source ? source.value : "";
    }

    function maskDocumentBySelect(input) {
        var type = getDocumentType(input);

        if (type === "CPF") {
            return maskCpf(input.value);
        }

        if (type === "CNPJ") {
            return maskCnpj(input.value);
        }

        if (type === "OUTRO") {
            return (input.value || "").slice(0, 50);
        }

        return onlyDigits(input.value).length > 11 ? maskCnpj(input.value) : maskCpf(input.value);
    }

    function configureDocumentBySelect(input) {
        var type = getDocumentType(input);

        if (type === "CPF") {
            input.inputMode = "numeric";
            input.maxLength = 14;
            input.placeholder = "000.000.000-00";
        } else if (type === "CNPJ") {
            input.inputMode = "numeric";
            input.maxLength = 18;
            input.placeholder = "00.000.000/0000-00";
        } else if (type === "OUTRO") {
            input.inputMode = "text";
            input.maxLength = 50;
            input.placeholder = "RG, passaporte ou outro";
        } else {
            input.inputMode = "numeric";
            input.maxLength = 18;
            input.placeholder = "Selecione o tipo primeiro";
        }

        input.value = maskDocumentBySelect(input);
    }

    function normalizeDecimal(value) {
        var masked = maskDecimal(value);
        return masked.replace(",", ".");
    }

    function normalizeMoney(value) {
        return (value || "").replace(/\./g, "").replace(",", ".");
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

    function inferMask(input) {
        var type = (input.type || "").toLowerCase();
        if (type === "date" || type === "datetime-local" || type === "time"
                || type === "month" || type === "week" || type === "hidden"
                || type === "password") {
            return "";
        }

        var explicitMask = input.getAttribute("data-mask");
        if (explicitMask) {
            return explicitMask;
        }

        var id = (input.id || "").toLowerCase();
        var name = (input.name || "").toLowerCase();
        var key = id + " " + name;

        if (key.indexOf("cpfcnpj") >= 0 || key.indexOf("cpf_cnpj") >= 0) {
            return "document";
        }
        if (key.indexOf("cpf") >= 0) {
            return "cpf";
        }
        if (key.indexOf("cnpj") >= 0) {
            return "cnpj";
        }
        if (key.indexOf("telefone") >= 0) {
            return "phone";
        }
        if (key.indexOf("cep") >= 0) {
            return "cep";
        }
        if (key.indexOf("placa") >= 0) {
            return "plate";
        }
        if (key.indexOf("cnh") >= 0 || key.indexOf("quilometragem") >= 0) {
            return "integer";
        }
        if (key.indexOf("ano") >= 0) {
            return "year";
        }
        if (key.indexOf("valor") >= 0 || key.indexOf("custo") >= 0) {
            return "money";
        }
        if (key.indexOf("peso") >= 0 || key.indexOf("capacidade") >= 0
                || key.indexOf("reajuste") >= 0) {
            return "decimal";
        }

        return "";
    }

    function applyMask(input, mask) {
        if (!mask) {
            return;
        }

        if (mask === "cpf") {
            input.value = maskCpf(input.value);
        } else if (mask === "cnpj") {
            input.value = maskCnpj(input.value);
        } else if (mask === "document") {
            input.value = onlyDigits(input.value).length > 11 ? maskCnpj(input.value) : maskCpf(input.value);
        } else if (mask === "document-by-select") {
            input.value = maskDocumentBySelect(input);
        } else if (mask === "phone") {
            input.value = maskPhone(input.value);
        } else if (mask === "cep") {
            input.value = maskCep(input.value);
        } else if (mask === "plate") {
            input.value = maskPlate(input.value);
        } else if (mask === "integer") {
            input.value = maskInteger(input.value);
        } else if (mask === "year") {
            input.value = maskInteger(input.value, 4);
        } else if (mask === "decimal") {
            input.value = maskDecimal(input.value);
        } else if (mask === "money") {
            input.value = maskMoney(input.value);
        }
    }

    function configureGenericInput(input) {
        if (input.dataset.maskConfigured === "true") {
            return;
        }

        var mask = inferMask(input);
        if (!mask) {
            return;
        }

        input.dataset.maskConfigured = "true";

        if (mask === "cpf" || mask === "cnpj" || mask === "document" || mask === "document-by-select" || mask === "phone"
                || mask === "cep" || mask === "integer" || mask === "year") {
            input.inputMode = "numeric";
        }

        if (mask === "decimal" || mask === "money") {
            input.inputMode = "decimal";
        }

        if (mask === "money" && !input.placeholder) {
            input.placeholder = "0,00";
        }

        if (mask === "plate") {
            input.maxLength = 8;
            if (!input.placeholder) {
                input.placeholder = "ABC-1234";
            }
        }

        if (mask === "document-by-select") {
            var source = document.getElementById(input.getAttribute("data-mask-type-source"));
            if (source) {
                source.addEventListener("change", function () {
                    configureDocumentBySelect(input);
                    input.focus();
                });
            }
            configureDocumentBySelect(input);
        }

        input.addEventListener("input", function () {
            if (mask === "document-by-select") {
                configureDocumentBySelect(input);
                return;
            }

            applyMask(input, mask);
        });

        applyMask(input, mask);
    }

    function configureGenericMasks() {
        document.querySelectorAll("input").forEach(configureGenericInput);

        document.querySelectorAll("form").forEach(function (form) {
            if (form.dataset.maskSubmitConfigured === "true") {
                return;
            }

            form.dataset.maskSubmitConfigured = "true";
            form.addEventListener("submit", function () {
                form.querySelectorAll("input").forEach(function (input) {
                    var mask = inferMask(input);
                    if (mask === "cpf" || mask === "cnpj" || mask === "document"
                            || mask === "cep" || mask === "integer" || mask === "year") {
                        input.value = onlyDigits(input.value);
                    } else if (mask === "document-by-select") {
                        var documentType = getDocumentType(input);
                        input.value = documentType === "CPF" || documentType === "CNPJ"
                                ? onlyDigits(input.value)
                                : (input.value || "").trim();
                    } else if (mask === "decimal") {
                        input.value = normalizeDecimal(input.value);
                    } else if (mask === "money") {
                        input.value = normalizeMoney(input.value);
                    }
                });
            });
        });
    }

    function todayDateValue() {
        return new Date().toISOString().slice(0, 10);
    }

    function nowDateTimeValue() {
        var now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        return now.toISOString().slice(0, 16);
    }

    function configureDateRules() {
        document.querySelectorAll("input[data-max-today='true']").forEach(function (input) {
            input.max = todayDateValue();
        });

        document.querySelectorAll("input[data-max-now='true']").forEach(function (input) {
            input.max = nowDateTimeValue();
        });

        document.querySelectorAll("input[data-min-from]").forEach(function (input) {
            var source = document.getElementById(input.getAttribute("data-min-from"));
            var fallback = document.getElementById(input.getAttribute("data-fallback-min-from"));

            function updateMin() {
                input.min = source && source.value ? source.value : fallback && fallback.value ? fallback.value : "";
            }

            if (source) {
                source.addEventListener("change", updateMin);
                source.addEventListener("input", updateMin);
            }

            if (fallback) {
                fallback.addEventListener("change", updateMin);
                fallback.addEventListener("input", updateMin);
            }

            updateMin();
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll("[data-mask-form='cliente']").forEach(configureClienteForm);
        configureGenericMasks();
        configureDateRules();
    });
})();
