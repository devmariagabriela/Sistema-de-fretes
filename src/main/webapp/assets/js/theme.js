(function () {
    const storageKey = "theme";
    const legacyStorageKey = "gwfrete-theme";
    const themes = ["theme-dark", "theme-light", "theme-high-contrast"];
    const fallbackTheme = "theme-dark";

    function normalizeTheme(value) {
        return themes.indexOf(value) >= 0 ? value : fallbackTheme;
    }

    function currentTheme() {
        try {
            return normalizeTheme(localStorage.getItem(storageKey) || localStorage.getItem(legacyStorageKey));
        } catch (error) {
            return fallbackTheme;
        }
    }

    function applyTheme(theme) {
        const normalizedTheme = normalizeTheme(theme);

        if (!document.body) {
            return;
        }

        document.body.classList.remove(...themes);
        document.body.classList.add(normalizedTheme);

        document.querySelectorAll("[data-theme-select]").forEach((select) => {
            select.value = normalizedTheme;
        });
    }

    function persistTheme(theme) {
        const normalizedTheme = normalizeTheme(theme);

        try {
            localStorage.setItem(storageKey, normalizedTheme);
            localStorage.setItem(legacyStorageKey, normalizedTheme);
        } catch (error) {
            // Prefer a working visual theme over surfacing storage failures to users.
        }

        applyTheme(normalizedTheme);
    }

    document.addEventListener("DOMContentLoaded", function () {
        applyTheme(currentTheme());

        document.querySelectorAll("[data-theme-select]").forEach((select) => {
            select.addEventListener("change", function () {
                persistTheme(select.value);
            });
        });
    });

    window.addEventListener("storage", function (event) {
        if (event.key === storageKey || event.key === legacyStorageKey) {
            applyTheme(currentTheme());
        }
    });
})();
