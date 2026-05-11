(function () {
    if (window.GWFreteVoiceAssistantLoaded) {
        return;
    }

    window.GWFreteVoiceAssistantLoaded = true;

    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;

    function text(selector, fallback) {
        const element = document.querySelector(selector);
        return element && element.textContent ? element.textContent.trim() : fallback;
    }

    function pageSummary() {
        const title = text("h1", document.title || "GW Frete");

        if (document.body.classList.contains("login-screen")) {
            return "Voce esta na tela de login. Informe usuario ou e-mail, depois a senha, e acione entrar no sistema.";
        }

        if (document.body.classList.contains("dashboard-page")) {
            return "Voce esta no dashboard executivo. Aqui ficam os indicadores principais, fretes por status, notificacoes e atalhos do menu lateral.";
        }

        return "Voce esta na tela " + title + ". Use o menu lateral para navegar e os botoes principais para executar as acoes disponiveis.";
    }

    function speak(message, status) {
        if (!("speechSynthesis" in window)) {
            status.textContent = "Seu navegador nao oferece fala automatica.";
            return;
        }

        window.speechSynthesis.cancel();

        const utterance = new SpeechSynthesisUtterance(message);
        utterance.lang = "pt-BR";
        utterance.rate = 0.92;
        utterance.pitch = 1;
        status.textContent = "Falando orientacao...";
        utterance.onend = function () {
            status.textContent = "Assistente pronta.";
        };

        window.speechSynthesis.speak(utterance);
    }

    function focusFirstField(status) {
        const field = document.querySelector("input:not([type='hidden']), select, textarea, button, a");
        if (field) {
            field.focus();
            status.textContent = "Primeiro controle da tela selecionado.";
        }
    }

    function commandHelp(status) {
        speak("Comandos disponiveis: ouvir tela, primeiro campo, parar fala, e abrir dashboard quando o link existir.", status);
    }

    function handleCommand(command, status) {
        const normalized = command.toLowerCase();

        if (normalized.indexOf("parar") >= 0) {
            window.speechSynthesis && window.speechSynthesis.cancel();
            status.textContent = "Fala interrompida.";
            return;
        }

        if (normalized.indexOf("primeiro") >= 0 || normalized.indexOf("campo") >= 0) {
            focusFirstField(status);
            return;
        }

        if (normalized.indexOf("dashboard") >= 0) {
            const dashboardLink = Array.from(document.querySelectorAll("a")).find(function (link) {
                return link.textContent.toLowerCase().indexOf("dashboard") >= 0 || link.href.indexOf("dashboard") >= 0;
            });
            if (dashboardLink) {
                status.textContent = "Abrindo dashboard.";
                dashboardLink.click();
                return;
            }
        }

        if (normalized.indexOf("ajuda") >= 0) {
            commandHelp(status);
            return;
        }

        speak(pageSummary(), status);
    }

    function createPanel() {
        const panel = document.createElement("section");
        panel.className = "voice-assistant-panel";
        panel.setAttribute("aria-live", "polite");
        panel.innerHTML = [
            "<h2>Assistente de voz</h2>",
            "<p>Use esta assistente para ouvir orientacoes da tela. No Chrome, o microfone pode aceitar comandos simples.</p>",
            "<div class=\"voice-assistant-actions\">",
            "<button type=\"button\" data-voice-action=\"speak\">Ouvir tela</button>",
            "<button type=\"button\" data-voice-action=\"listen\">Falar comando</button>",
            "<button type=\"button\" data-voice-action=\"focus\">Primeiro campo</button>",
            "<button type=\"button\" data-voice-action=\"stop\">Parar</button>",
            "</div>",
            "<span class=\"voice-assistant-status\">Assistente pronta.</span>"
        ].join("");
        document.body.appendChild(panel);
        return panel;
    }

    document.addEventListener("DOMContentLoaded", function () {
        const toggles = document.querySelectorAll("[data-voice-assistant-toggle]");
        if (!toggles.length) {
            return;
        }

        const panel = createPanel();
        const status = panel.querySelector(".voice-assistant-status");

        toggles.forEach(function (toggle) {
            toggle.addEventListener("click", function () {
                const isOpen = panel.classList.toggle("is-open");
                toggles.forEach(function (item) {
                    item.setAttribute("aria-expanded", isOpen ? "true" : "false");
                });

                if (isOpen) {
                    speak("Assistente de voz ativada. " + pageSummary(), status);
                }
            });
        });

        panel.addEventListener("click", function (event) {
            const action = event.target && event.target.getAttribute("data-voice-action");
            if (!action) {
                return;
            }

            if (action === "speak") {
                speak(pageSummary(), status);
                return;
            }

            if (action === "focus") {
                focusFirstField(status);
                return;
            }

            if (action === "stop") {
                window.speechSynthesis && window.speechSynthesis.cancel();
                status.textContent = "Assistente pausada.";
                return;
            }

            if (action === "listen") {
                if (!SpeechRecognition) {
                    status.textContent = "Reconhecimento de voz indisponivel neste navegador.";
                    speak("Reconhecimento de voz indisponivel neste navegador.", status);
                    return;
                }

                const recognition = new SpeechRecognition();
                recognition.lang = "pt-BR";
                recognition.interimResults = false;
                recognition.maxAlternatives = 1;
                status.textContent = "Ouvindo comando...";
                recognition.onresult = function (resultEvent) {
                    const command = resultEvent.results[0][0].transcript;
                    status.textContent = "Comando: " + command;
                    handleCommand(command, status);
                };
                recognition.onerror = function () {
                    status.textContent = "Nao consegui ouvir o comando.";
                };
                recognition.start();
            }
        });
    });
})();
