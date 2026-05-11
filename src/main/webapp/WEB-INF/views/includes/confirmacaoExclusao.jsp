<div class="confirm-modal" data-confirm-modal hidden>
    <div class="confirm-modal-backdrop" data-confirm-cancel></div>
    <section class="confirm-modal-panel" role="dialog" aria-modal="true" aria-labelledby="confirmModalTitle">
        <form method="post" data-confirm-form>
            <h2 id="confirmModalTitle">Confirmar ação</h2>
            <p data-confirm-message>Deseja continuar?</p>
            <input type="hidden" name="id" data-confirm-id>
            <div class="confirm-modal-actions">
                <button class="button button-secondary" type="button" data-confirm-cancel>Cancelar</button>
                <button class="button button-danger" type="submit" data-confirm-submit>Confirmar</button>
            </div>
        </form>
    </section>
</div>
<script>
    (function () {
        const modal = document.querySelector("[data-confirm-modal]");
        if (!modal) {
            return;
        }

        const form = modal.querySelector("[data-confirm-form]");
        const title = modal.querySelector("#confirmModalTitle");
        const message = modal.querySelector("[data-confirm-message]");
        const idInput = modal.querySelector("[data-confirm-id]");
        const submit = modal.querySelector("[data-confirm-submit]");

        const close = function () {
            modal.hidden = true;
            form.removeAttribute("action");
        };

        document.querySelectorAll("[data-soft-delete-button]").forEach(function (button) {
            button.addEventListener("click", function () {
                form.action = button.dataset.action;
                idInput.value = button.dataset.id;
                title.textContent = button.dataset.title || "Confirmar ação";
                message.textContent = button.dataset.message || "Deseja continuar?";
                submit.textContent = button.dataset.submit || "Confirmar";
                modal.hidden = false;
                submit.focus();
            });
        });

        modal.querySelectorAll("[data-confirm-cancel]").forEach(function (button) {
            button.addEventListener("click", close);
        });

        document.addEventListener("keydown", function (event) {
            if (event.key === "Escape" && !modal.hidden) {
                close();
            }
        });
    })();
</script>
