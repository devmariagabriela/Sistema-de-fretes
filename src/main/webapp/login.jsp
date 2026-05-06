<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body>
    <main class="login-shell" aria-label="Acesso ao sistema GW FRETE">
        <section class="operations-panel" aria-hidden="true">
            <span class="ambient-light ambient-light-one"></span>
            <span class="ambient-light ambient-light-two"></span>

            <div class="panel-brand">
                <span class="brand-icon">
                    <svg class="moving-truck" viewBox="0 0 32 24" focusable="false" aria-hidden="true">
                        <g class="truck-body">
                            <path d="M3.5 7.5h14v8h-14z"></path>
                            <path d="M17.5 10h5.2l3.8 4v1.5h-9z"></path>
                            <path d="M20.4 11.7h2.1l1.9 2h-4z"></path>
                            <path d="M5.5 15.5h17.8"></path>
                        </g>
                        <circle class="truck-wheel truck-wheel-left" cx="8.5" cy="17" r="2"></circle>
                        <circle class="truck-wheel truck-wheel-right" cx="22.5" cy="17" r="2"></circle>
                    </svg>
                </span>
                <div>
                    <strong>GW FRETE</strong>
                    <span>Transportation Management System</span>
                </div>
            </div>

            <div class="panel-copy">
                <h1>Controle operacional de fretes, veículos e entregas</h1>
                <p>Monitore sua frota em tempo real, acompanhe rotas, status de entregas e indicadores operacionais em um painel corporativo.</p>
            </div>

            <div class="route-visual">
                <div class="map-points">
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                </div>

                <svg class="route-svg" viewBox="0 0 640 240" preserveAspectRatio="none" focusable="false">
                    <path class="route-shadow" d="M48 180 C160 150 190 72 312 104 C420 132 458 186 590 64"></path>
                    <path class="route-path" d="M48 180 C160 150 190 72 312 104 C420 132 458 186 590 64"></path>
                    <circle class="route-anchor route-start" cx="48" cy="180" r="9"></circle>
                    <circle class="route-anchor route-check" cx="312" cy="104" r="6"></circle>
                    <circle class="route-anchor route-end" cx="590" cy="64" r="8"></circle>
                </svg>

                <span class="tracking-dot"></span>

                <div class="route-chip route-chip-live">
                    <span class="status-dot"></span>
                    <strong>CTE-58.421</strong>
                    <small>SP → Curitiba</small>
                </div>

                <div class="route-chip route-chip-count">
                    <span class="pin-dot"></span>
                    <small>12 veículos rastreados agora</small>
                </div>
            </div>

            <div class="operations-metrics">
                <article>
                    <span class="metric-icon">
                        <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                            <path d="M4 7h10v9H4z"></path>
                            <path d="M14 10h3.5l2.5 3v3h-6z"></path>
                            <circle cx="8" cy="17" r="1.7"></circle>
                            <circle cx="17" cy="17" r="1.7"></circle>
                        </svg>
                    </span>
                    <small>Frota ativa</small>
                    <strong>148</strong>
                    <span>+4 hoje</span>
                </article>
                <article>
                    <span class="metric-icon">
                        <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                            <path d="M12 3 4.5 7.2 12 11.4l7.5-4.2z"></path>
                            <path d="M4.5 7.2v9.2L12 21l7.5-4.6V7.2"></path>
                            <path d="M12 11.4V21"></path>
                        </svg>
                    </span>
                    <small>Entregas / dia</small>
                    <strong>2.347</strong>
                    <span>98,2% no prazo</span>
                </article>
                <article>
                    <span class="metric-icon">
                        <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                            <path d="M4 13h4l2-6 4 12 2-6h4"></path>
                        </svg>
                    </span>
                    <small>OS abertas</small>
                    <strong>36</strong>
                    <span>6 críticas</span>
                </article>
            </div>

            <div class="operation-timeline">
                <span><i class="timeline-ok"></i> Coleta confirmada · 08:12</span>
                <span><i class="timeline-route"></i> Em trânsito · 11:40</span>
                <span><i class="timeline-eta"></i> Previsão entrega · 16:25</span>
            </div>
        </section>

        <section class="login-area">
            <div class="login-card" aria-labelledby="login-title">
                <div class="login-header">
                    <span class="access-badge">Ambiente corporativo</span>
                    <h1 id="login-title">Acesso ao sistema</h1>
                    <p>Informe suas credenciais corporativas para continuar.</p>
                </div>

                <div class="alert-error" role="alert">${erroLogin}</div>

                <form class="login-form" action="${pageContext.request.contextPath}/login" method="post" autocomplete="on">
                    <div class="form-field">
                        <label for="email">Usuário ou e-mail</label>
                        <div class="input-wrap">
                            <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                                <path d="M20 21a8 8 0 0 0-16 0"></path>
                                <circle cx="12" cy="7" r="4"></circle>
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
                            <a href="#" class="forgot-link">Esqueci minha senha</a>
                        </div>
                        <div class="input-wrap">
                            <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                                <rect x="4" y="10" width="16" height="10" rx="2"></rect>
                                <path d="M8 10V7a4 4 0 0 1 8 0v3"></path>
                            </svg>
                            <input
                                id="senha"
                                name="senha"
                                type="password"
                                placeholder="Informe sua senha"
                                autocomplete="current-password"
                                required>
                        </div>
                    </div>

                    <button class="primary-button" type="submit">Entrar</button>
                </form>

                <div class="restricted-box">
                    <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                        <path d="M12 3 5 6v5c0 4.4 2.8 8.3 7 10 4.2-1.7 7-5.6 7-10V6z"></path>
                        <path d="m9.5 12 1.8 1.8 3.7-4"></path>
                    </svg>
                    <span>Acesso restrito a usuários autorizados</span>
                </div>
            </div>
            <footer class="login-footer">
                <span>© 2026 GW FRETE</span>
                <span>v4.12.0</span>
            </footer>
        </section>
    </main>
</body>
</html>
