<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css?v=login-20260510-login-theme">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-login"></script>
</head>
<body class="login-screen theme-dark">
    <script>
        (function () {
            var themes = ["theme-dark", "theme-light", "theme-high-contrast"];
            var theme = "theme-dark";

            try {
                var storedTheme = localStorage.getItem("theme") || localStorage.getItem("gwfrete-theme");
                theme = themes.indexOf(storedTheme) >= 0 ? storedTheme : "theme-dark";
            } catch (error) {
                theme = "theme-dark";
            }

            document.body.classList.remove("theme-dark", "theme-light", "theme-high-contrast");
            document.body.classList.add(theme);
        })();
    </script>
    <header class="login-topbar" aria-hidden="true">
        <div class="topbar-brand">
            <div class="brand-mark">
                <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                    <path d="M3 7h11v9H3z"></path>
                    <path d="M14 10h4l3 3v3h-7z"></path>
                    <circle cx="7" cy="18" r="2"></circle>
                    <circle cx="17" cy="18" r="2"></circle>
                </svg>
            </div>
            <div>
                <strong>GW FRETE</strong>
                <span>TMS // V4.12.0</span>
            </div>
        </div>

        <div class="system-state">
            <label class="login-theme-switcher">
                <span>Tema</span>
                <select data-theme-select aria-label="Selecionar tema visual">
                    <option value="theme-dark">Escuro</option>
                    <option value="theme-light">Claro</option>
                    <option value="theme-high-contrast">Alto contraste</option>
                </select>
            </label>
            <span class="online-pill">
                <i></i>
                SISTEMA ONLINE
                <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                    <path d="M22 12h-4l-3 7-6-14-3 7H2"></path>
                </svg>
            </span>
            <span class="live-clock" id="liveClock">--:--:--</span>
        </div>
    </header>

    <main class="login-shell" aria-label="Acesso ao sistema GW FRETE">
        <section class="operations-panel" aria-hidden="true">
            <span class="ambient-light ambient-light-one"></span>
            <span class="ambient-light ambient-light-two"></span>

            <div class="platform-badge">
                <span></span>
                PLATAFORMA LOGÍSTICA INTELIGENTE
            </div>

            <div class="panel-copy">
                <h1>Controle <span>total</span> da sua frota, em tempo real.</h1>
                <p>Monitore rotas, entregas, CT-es e indicadores operacionais em um cockpit corporativo desenhado para operações que não param.</p>
            </div>

            <div class="route-visual">
                <canvas class="route-canvas" id="routeCanvas"></canvas>

                <div class="route-label">
                    <span></span>
                    CT-E 58.421 · SP → Curitiba
                </div>

                <div class="route-count">
                    <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                        <path d="m13 2-8 11h7l-1 9 8-12h-7z"></path>
                    </svg>
                    12 veículos rastreados agora
                </div>
            </div>

            <div class="operations-metrics">
                <article class="stat-card" style="--delay: 0.8s">
                    <small>Frota ativa</small>
                    <strong class="metric-value" data-target="148">0</strong>
                    <span>+4 hoje</span>
                </article>
                <article class="stat-card" style="--delay: 0.95s">
                    <small>Entregas / dia</small>
                    <strong class="metric-value" data-target="2347">0</strong>
                    <span>98,2% no prazo</span>
                </article>
                <article class="stat-card" style="--delay: 1.1s">
                    <small>OS abertas</small>
                    <strong class="metric-value" data-target="36">0</strong>
                    <span>6 críticas</span>
                </article>
            </div>
        </section>

        <section class="login-area">
            <div class="login-card" aria-labelledby="login-title">
                <span class="corner corner-top-left"></span>
                <span class="corner corner-top-right"></span>
                <span class="corner corner-bottom-left"></span>
                <span class="corner corner-bottom-right"></span>

                <div class="login-header">
                    <span class="access-badge">
                        <i></i>
                        AUTENTICAÇÃO SEGURA
                    </span>
                    <h1 id="login-title">Acesso ao <span>cockpit</span></h1>
                    <p>Informe suas credenciais corporativas para iniciar a sessão.</p>
                </div>

                <div class="alert-error" role="alert">${erroLogin}</div>

                <c:if test="${not empty mensagemSucesso}">
                    <div class="alert-success" role="status">${mensagemSucesso}</div>
                </c:if>

                <form class="login-form" action="${pageContext.request.contextPath}/login" method="post" autocomplete="on">
                    <div class="form-field">
                        <label for="email">Usuário ou e-mail</label>
                        <div class="input-wrap">
                            <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                                <path d="M4 6h16v12H4z"></path>
                                <path d="m4 7 8 6 8-6"></path>
                            </svg>
                            <input
                                id="email"
                                name="email"
                                type="text"
                                value="${emailInformado}"
                                placeholder="seu.usuario@gwfrete.com"
                                autocomplete="username"
                                required>
                        </div>
                    </div>

                    <div class="form-field">
                        <div class="label-row">
                            <label for="senha">Senha</label>
                            <a href="${pageContext.request.contextPath}/esqueci-senha" class="forgot-link">Esqueci a senha</a>
                        </div>
                        <div class="input-wrap">
                            <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                                <rect x="5" y="10" width="14" height="10" rx="2"></rect>
                                <path d="M8 10V7a4 4 0 0 1 8 0v3"></path>
                            </svg>
                            <input
                                id="senha"
                                name="senha"
                                type="password"
                                placeholder="••••••••••"
                                autocomplete="current-password"
                                required>
                            <button class="password-toggle" type="button" aria-label="Mostrar senha" aria-controls="senha">
                                <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                                    <path class="eye-open" d="M2 12s3.5-6 10-6 10 6 10 6-3.5 6-10 6S2 12 2 12z"></path>
                                    <circle class="eye-open" cx="12" cy="12" r="3"></circle>
                                    <path class="eye-closed" d="M3 3l18 18"></path>
                                </svg>
                            </button>
                        </div>
                    </div>

                    <button class="primary-button" type="submit" aria-busy="false">
                        <span class="button-shimmer"></span>
                        <span class="button-loader" aria-hidden="true"></span>
                        <span class="button-label">
                            <span data-login-button-text>Entrar no sistema</span>
                            <b>→</b>
                        </span>
                    </button>
                </form>

                <div class="divider"><span>ou</span></div>

                <button class="biometric-button" type="button" disabled>
                    <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                        <path d="M12 11v2"></path>
                        <path d="M8 11a4 4 0 0 1 8 0v2"></path>
                        <path d="M6 13v-2a6 6 0 0 1 12 0v2"></path>
                        <path d="M4 13v-2a8 8 0 0 1 16 0v2"></path>
                        <path d="M8 16a4 4 0 0 0 8 0"></path>
                    </svg>
                    Entrar com biometria corporativa
                </button>

                <div class="restricted-box">
                    <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                        <path d="M12 3 5 6v5c0 4.4 2.8 8.3 7 10 4.2-1.7 7-5.6 7-10V6z"></path>
                    </svg>
                    <span>Conexão criptografada · Acesso restrito a usuários autorizados</span>
                </div>
            </div>
        </section>
    </main>

    <footer class="system-footer" aria-hidden="true">
        <span>© 2026 GW FRETE</span>
        <div>
            <span><i class="timeline-ok"></i>Coleta confirmada · 08:12</span>
            <span><i class="timeline-route"></i>Em trânsito · 11:40</span>
            <span><i class="timeline-eta"></i>Previsão entrega · 16:25</span>
        </div>
        <span>v4.12.0 · build 2026.05</span>
    </footer>

    <div class="login-loading-overlay" aria-live="polite" aria-hidden="true">
        <div class="loading-card" role="status">
            <div class="loading-truck-scene" aria-hidden="true">
                <div class="loading-road">
                    <span></span>
                    <span></span>
                    <span></span>
                </div>
                <svg class="loading-truck" viewBox="0 0 170 86" focusable="false" aria-hidden="true">
                    <path class="truck-cargo" d="M18 25h82v37H18z"></path>
                    <path class="truck-cab" d="M100 37h28l20 16v9h-48z"></path>
                    <path class="truck-window" d="M109 42h17l10 10h-27z"></path>
                    <circle class="truck-wheel" cx="45" cy="66" r="10"></circle>
                    <circle class="truck-wheel" cx="122" cy="66" r="10"></circle>
                    <circle class="truck-hub" cx="45" cy="66" r="4"></circle>
                    <circle class="truck-hub" cx="122" cy="66" r="4"></circle>
                </svg>
            </div>
            <strong>Autenticando acesso</strong>
            <span>Carregando ambiente operacional...</span>
        </div>
    </div>

    <script>
        (function () {
            var toggle = document.querySelector(".password-toggle");
            var password = document.getElementById("senha");
            var clock = document.getElementById("liveClock");
            var form = document.querySelector(".login-form");
            var submit = document.querySelector(".primary-button");
            var loadingOverlay = document.querySelector(".login-loading-overlay");
            var submitText = submit ? submit.querySelector("[data-login-button-text]") : null;
            var submitArrow = submit ? submit.querySelector(".button-label b") : null;
            var submitDefaultText = submitText ? submitText.textContent : "Entrar no sistema";

            if (toggle && password) {
                toggle.addEventListener("click", function () {
                    var showing = password.type === "text";
                    password.type = showing ? "password" : "text";
                    toggle.classList.toggle("is-visible", !showing);
                    toggle.setAttribute("aria-label", showing ? "Mostrar senha" : "Ocultar senha");
                });
            }

            if (clock) {
                var tickClock = function () {
                    clock.textContent = new Date().toLocaleTimeString("pt-BR", {
                        hour: "2-digit",
                        minute: "2-digit",
                        second: "2-digit"
                    });
                };
                tickClock();
                window.setInterval(tickClock, 1000);
            }

            if (form && submit) {
                form.addEventListener("submit", function (event) {
                    if (form.dataset.submitting === "true") {
                        return;
                    }

                    if (form.checkValidity && !form.checkValidity()) {
                        return;
                    }

                    event.preventDefault();

                    submit.classList.add("is-loading");
                    submit.disabled = true;
                    submit.setAttribute("aria-busy", "true");
                    form.dataset.submitting = "true";

                    if (loadingOverlay) {
                        loadingOverlay.classList.add("is-visible");
                        loadingOverlay.setAttribute("aria-hidden", "false");
                    }

                    if (submitText) {
                        submitText.textContent = "Autenticando...";
                    }

                    if (submitArrow) {
                        submitArrow.hidden = true;
                    }

                    window.setTimeout(function () {
                        form.submit();
                    }, 1400);
                });

                window.addEventListener("pageshow", function () {
                    submit.classList.remove("is-loading");
                    submit.disabled = false;
                    submit.setAttribute("aria-busy", "false");
                    delete form.dataset.submitting;

                    if (loadingOverlay) {
                        loadingOverlay.classList.remove("is-visible");
                        loadingOverlay.setAttribute("aria-hidden", "true");
                    }

                    if (submitText) {
                        submitText.textContent = submitDefaultText;
                    }

                    if (submitArrow) {
                        submitArrow.hidden = false;
                    }
                });
            }

            var metrics = document.querySelectorAll(".metric-value");
            Array.prototype.forEach.call(metrics, function (metric, index) {
                var target = parseInt(metric.getAttribute("data-target"), 10) || 0;
                var duration = 1800;
                var startDelay = 700 + index * 160;

                window.setTimeout(function () {
                    var startedAt = null;
                    var animate = function (timestamp) {
                        if (!startedAt) {
                            startedAt = timestamp;
                        }

                        var progress = Math.min((timestamp - startedAt) / duration, 1);
                        var value = Math.floor(progress * target);
                        metric.textContent = value.toLocaleString("pt-BR");

                        if (progress < 1) {
                            window.requestAnimationFrame(animate);
                        }
                    };

                    window.requestAnimationFrame(animate);
                }, startDelay);
            });

            var canvas = document.getElementById("routeCanvas");
            if (!canvas || !canvas.getContext) {
                return;
            }

            var context = canvas.getContext("2d");
            var progress = 0;
            var animationId = 0;
            var points = [
                { x: 0.08, y: 0.72 },
                { x: 0.25, y: 0.45 },
                { x: 0.45, y: 0.60 },
                { x: 0.65, y: 0.38 },
                { x: 0.85, y: 0.25 }
            ];

            var resizeCanvas = function () {
                var ratio = window.devicePixelRatio || 1;
                var width = canvas.offsetWidth;
                var height = canvas.offsetHeight;
                canvas.width = width * ratio;
                canvas.height = height * ratio;
                context.setTransform(ratio, 0, 0, ratio, 0, 0);
            };

            var catmull = function (a, b, c, d, t) {
                return 0.5 * ((2 * b) + (-a + c) * t + (2 * a - 5 * b + 4 * c - d) * t * t + (-a + 3 * b - 3 * c + d) * t * t * t);
            };

            var getPoint = function (t) {
                var total = points.length - 1;
                var index = Math.min(Math.floor(t * total), total - 1);
                var local = t * total - index;
                var p0 = points[Math.max(index - 1, 0)];
                var p1 = points[index];
                var p2 = points[Math.min(index + 1, total)];
                var p3 = points[Math.min(index + 2, total)];
                var width = canvas.offsetWidth;
                var height = canvas.offsetHeight;

                return {
                    x: catmull(p0.x * width, p1.x * width, p2.x * width, p3.x * width, local),
                    y: catmull(p0.y * height, p1.y * height, p2.y * height, p3.y * height, local)
                };
            };

            var drawPath = function (limit) {
                context.beginPath();
                for (var i = 0; i <= limit; i++) {
                    var point = getPoint(i / 200);
                    if (i === 0) {
                        context.moveTo(point.x, point.y);
                    } else {
                        context.lineTo(point.x, point.y);
                    }
                }
            };

            var drawRoute = function () {
                var width = canvas.offsetWidth;
                var height = canvas.offsetHeight;
                context.clearRect(0, 0, width, height);

                context.strokeStyle = "rgba(0,180,255,0.07)";
                context.lineWidth = 1;
                for (var x = 0; x < width; x += 40) {
                    context.beginPath();
                    context.moveTo(x, 0);
                    context.lineTo(x, height);
                    context.stroke();
                }
                for (var y = 0; y < height; y += 40) {
                    context.beginPath();
                    context.moveTo(0, y);
                    context.lineTo(width, y);
                    context.stroke();
                }

                drawPath(200);
                context.strokeStyle = "rgba(0,180,255,0.16)";
                context.lineWidth = 2;
                context.setLineDash([6, 8]);
                context.stroke();
                context.setLineDash([]);

                var steps = Math.floor(progress * 200);
                if (steps > 1) {
                    drawPath(steps);
                    var gradient = context.createLinearGradient(0, 0, width, 0);
                    gradient.addColorStop(0, "rgba(0,200,255,0.26)");
                    gradient.addColorStop(1, "rgba(0,238,255,1)");
                    context.strokeStyle = gradient;
                    context.lineWidth = 2.7;
                    context.shadowColor = "#00ccff";
                    context.shadowBlur = 9;
                    context.stroke();
                    context.shadowBlur = 0;
                }

                for (var p = 0; p < points.length; p++) {
                    if (progress >= p / (points.length - 1) - 0.01) {
                        var px = points[p].x * width;
                        var py = points[p].y * height;
                        context.beginPath();
                        context.arc(px, py, 5, 0, Math.PI * 2);
                        context.fillStyle = "#00ccff";
                        context.shadowColor = "#00ccff";
                        context.shadowBlur = 12;
                        context.fill();
                        context.shadowBlur = 0;
                        context.beginPath();
                        context.arc(px, py, 10, 0, Math.PI * 2);
                        context.strokeStyle = "rgba(0,204,255,0.32)";
                        context.lineWidth = 1.5;
                        context.stroke();
                    }
                }

                var current = getPoint(progress);
                context.beginPath();
                context.arc(current.x, current.y, 7, 0, Math.PI * 2);
                context.fillStyle = "#00eeff";
                context.shadowColor = "#00eeff";
                context.shadowBlur = 22;
                context.fill();
                context.shadowBlur = 0;
                context.beginPath();
                context.arc(current.x, current.y, 14, 0, Math.PI * 2);
                context.strokeStyle = "rgba(0,238,255,0.4)";
                context.lineWidth = 2;
                context.stroke();

                progress += 0.0016;
                if (progress > 1) {
                    progress = 0;
                }

                animationId = window.requestAnimationFrame(drawRoute);
            };

            resizeCanvas();
            drawRoute();
            window.addEventListener("resize", resizeCanvas);
            window.addEventListener("beforeunload", function () {
                window.cancelAnimationFrame(animationId);
            });
        }());
    </script>
</body>
</html>
