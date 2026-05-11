<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GW FRETE | Rastreamentos</title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=app-20260510-tracking">
    <script defer src="${pageContext.request.contextPath}/assets/js/theme.js?v=theme-20260510-ui"></script>
</head>
<body class="theme-dark">
    <main class="app-shell">
        <jsp:include page="/WEB-INF/views/includes/sidebar.jsp">
            <jsp:param name="ativo" value="rastreamentos" />
        </jsp:include>

        <section class="app-content">
            <header class="page-header">
                <div>
                    <span class="page-kicker">Rastreamento operacional</span>
                    <h1>Rastreamentos</h1>
                    <p>Histórico de pontos de localização registrados para acompanhamento de fretes.</p>
                </div>
                <div class="page-actions">
                    <jsp:include page="/WEB-INF/views/includes/header.jsp" />
                    <c:if test="${podeGerenciarRastreamentos}">
                        <c:choose>
                            <c:when test="${not empty freteIdSelecionado}">
                                <a class="button button-primary" href="${pageContext.request.contextPath}/rastreamentos/novo?freteId=${freteIdSelecionado}">Novo ponto</a>
                            </c:when>
                            <c:otherwise>
                                <a class="button button-primary" href="${pageContext.request.contextPath}/rastreamentos/novo">Novo ponto</a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                </div>
            </header>

            <c:if test="${not empty mensagemSucesso}">
                <p class="message message-success" role="status">${mensagemSucesso}</p>
            </c:if>
            <c:if test="${not empty mensagemErro}">
                <p class="message message-error" role="alert">${mensagemErro}</p>
            </c:if>

            <section class="content-card" aria-label="Filtro de rastreamentos">
                <form action="${pageContext.request.contextPath}/rastreamentos/frete" method="get">
                    <div class="form-grid">
                        <div class="form-field">
                            <label for="freteId">Frete</label>
                            <select id="freteId" name="freteId" required>
                                <option value="">Selecione</option>
                                <c:forEach var="frete" items="${fretes}">
                                    <option value="${frete.id}" ${freteIdSelecionado == frete.id ? 'selected' : ''}>${frete.codigo} - ${frete.origem} / ${frete.destino}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-actions">
                        <button class="button button-primary" type="submit">Consultar</button>
                        <a class="button button-secondary" href="${pageContext.request.contextPath}/rastreamentos">Limpar</a>
                    </div>
                </form>
            </section>

            <section class="content-card tracking-map-card" aria-label="Mapa de rastreamento">
                <div class="tracking-map-header">
                    <div>
                        <span class="summary-label">Mapa de rastreamento</span>
                        <h2>Mapa de rastreamento</h2>
                    </div>
                    <div class="tracking-map-meta">
                        <span class="tracking-map-status" id="trackingMapStatus">Aguardando coordenadas</span>
                        <span class="tracking-map-warning" id="trackingMapWarning" hidden>Coordenada próxima de 0,0. Confira latitude e longitude cadastradas.</span>
                    </div>
                </div>
                <div class="tracking-map-frame">
                    <div id="trackingMap" class="tracking-map" role="img" aria-label="Mapa com a localização do frete selecionado"></div>
                    <div id="trackingMapEmpty" class="tracking-map-empty" hidden>Nenhuma coordenada disponível para este frete</div>
                </div>
                <div id="trackingMapData" hidden>
                    <c:forEach var="rastreamento" items="${rastreamentos}">
                        <c:if test="${not empty rastreamento.latitude and not empty rastreamento.longitude}">
                            <span class="tracking-point"
                                  data-lat="${rastreamento.latitude}"
                                  data-lng="${rastreamento.longitude}">
                                <span class="tracking-point-frete"><c:out value="${rastreamento.frete.codigo}"/></span>
                                <span class="tracking-point-localizacao"><c:out value="${rastreamento.localizacao}"/></span>
                                <span class="tracking-point-data-hora"><fmt:formatDate value="${rastreamento.dataHoraFormatada}" pattern="dd/MM/yyyy HH:mm"/></span>
                                <span class="tracking-point-observacao"><c:out value="${rastreamento.observacao}"/></span>
                            </span>
                        </c:if>
                    </c:forEach>
                </div>
            </section>

            <section class="content-card" aria-label="Lista de rastreamentos">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Data/hora</th>
                                <th>Frete</th>
                                <th>Localização</th>
                                <th>Latitude</th>
                                <th>Longitude</th>
                                <th>Observação</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="rastreamento" items="${rastreamentos}">
                                <tr>
                                    <td class="text-muted">
                                        <fmt:formatDate value="${rastreamento.dataHoraFormatada}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td><strong>${rastreamento.frete.codigo}</strong></td>
                                    <td>${rastreamento.localizacao}</td>
                                    <td class="text-muted">${empty rastreamento.latitude ? '-' : rastreamento.latitude}</td>
                                    <td class="text-muted">${empty rastreamento.longitude ? '-' : rastreamento.longitude}</td>
                                    <td class="text-muted">${empty rastreamento.observacao ? '-' : rastreamento.observacao}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty rastreamentos}">
                                <tr>
                                    <td colspan="6">
                                        <div class="empty-state">Selecione um frete para consultar o histórico de rastreamento.</div>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </section>
    </main>
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <script>
        (function () {
            var mapElement = document.getElementById("trackingMap");
            var emptyElement = document.getElementById("trackingMapEmpty");
            var statusElement = document.getElementById("trackingMapStatus");
            var warningElement = document.getElementById("trackingMapWarning");
            var dataElement = document.getElementById("trackingMapData");

            if (!mapElement || !emptyElement || !dataElement || typeof L === "undefined") {
                return;
            }

            var points = Array.prototype.slice.call(dataElement.querySelectorAll(".tracking-point"))
                .map(function (element) {
                    return {
                        lat: Number(element.dataset.lat),
                        lng: Number(element.dataset.lng),
                        frete: obterTexto(element, ".tracking-point-frete") || "-",
                        localizacao: obterTexto(element, ".tracking-point-localizacao") || "Não informada",
                        dataHora: obterTexto(element, ".tracking-point-data-hora") || "-",
                        observacao: obterTexto(element, ".tracking-point-observacao") || "Sem observação"
                    };
                })
                .filter(function (point) {
                    return Number.isFinite(point.lat) && Number.isFinite(point.lng);
                });

            if (!points.length) {
                emptyElement.hidden = false;
                mapElement.classList.add("tracking-map-muted");
                if (statusElement) {
                    statusElement.textContent = "Sem coordenadas";
                }
                return;
            }

            var hasSuspiciousOriginPoint = points.some(function (point) {
                return Math.abs(point.lat) < 0.01 && Math.abs(point.lng) < 0.01;
            });

            if (warningElement && hasSuspiciousOriginPoint) {
                warningElement.hidden = false;
            }

            resolverPontosSuspeitos(points).then(function (resolvedPoints) {
                emptyElement.hidden = true;
                if (statusElement) {
                    statusElement.textContent = resolvedPoints.length > 1 ? resolvedPoints.length + " pontos na rota" : "Última posição";
                }

                var latestPoint = resolvedPoints[resolvedPoints.length - 1];
                var map = L.map(mapElement, {
                    zoomControl: true,
                    scrollWheelZoom: false
                }).setView([latestPoint.lat, latestPoint.lng], latestPoint.aproximado ? 12 : 13);

                L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
                    maxZoom: 19,
                    attribution: "&copy; OpenStreetMap"
                }).addTo(map);

                var bounds = [];

                resolvedPoints.forEach(function (point, index) {
                    var marker = L.marker([point.lat, point.lng]).addTo(map);
                    marker.bindPopup(criarPopup(point, index === resolvedPoints.length - 1));
                    bounds.push([point.lat, point.lng]);

                    if (index === resolvedPoints.length - 1) {
                        marker.openPopup();
                    }
                });

                if (resolvedPoints.length > 1) {
                    L.polyline(bounds, {
                        color: "#00C6FF",
                        weight: 4,
                        opacity: 0.86,
                        lineJoin: "round"
                    }).addTo(map);
                    map.fitBounds(bounds, { padding: [28, 28] });
                }

                setTimeout(function () {
                    map.invalidateSize();
                }, 120);
            });

            function criarPopup(point, principal) {
                var wrapper = document.createElement("div");
                wrapper.className = "tracking-popup";

                adicionarLinha(wrapper, principal ? "Frete principal" : "Frete", point.frete);
                adicionarLinha(wrapper, "Localização", point.localizacao);
                adicionarLinha(wrapper, "Data/hora", point.dataHora);
                adicionarLinha(wrapper, "Observação", point.observacao);
                if (point.aproximado) {
                    adicionarLinha(wrapper, "Mapa", "Posição aproximada pela localização informada");
                }

                return wrapper;
            }

            function adicionarLinha(wrapper, label, value) {
                var row = document.createElement("p");
                var strong = document.createElement("strong");
                var span = document.createElement("span");

                strong.textContent = label;
                span.textContent = value;
                row.appendChild(strong);
                row.appendChild(span);
                wrapper.appendChild(row);
            }

            function obterTexto(element, selector) {
                var child = element.querySelector(selector);
                return child ? child.textContent.trim() : "";
            }

            function resolverPontosSuspeitos(points) {
                if (typeof fetch !== "function") {
                    return Promise.resolve(points);
                }

                return Promise.all(points.map(function (point) {
                    if (Math.abs(point.lat) >= 0.01 || Math.abs(point.lng) >= 0.01 || !point.localizacao) {
                        return Promise.resolve(point);
                    }

                    var query = point.localizacao + ", Brasil";
                    var url = "https://nominatim.openstreetmap.org/search?format=json&limit=1&q="
                        + encodeURIComponent(query);

                    return fetch(url, {
                        headers: {
                            "Accept": "application/json"
                        }
                    })
                        .then(function (response) {
                            return response.ok ? response.json() : [];
                        })
                        .then(function (results) {
                            if (!results.length) {
                                return point;
                            }

                            return Object.assign({}, point, {
                                lat: Number(results[0].lat),
                                lng: Number(results[0].lon),
                                aproximado: true
                            });
                        })
                        .catch(function () {
                            return point;
                        });
                }));
            }
        })();
    </script>
</body>
</html>
