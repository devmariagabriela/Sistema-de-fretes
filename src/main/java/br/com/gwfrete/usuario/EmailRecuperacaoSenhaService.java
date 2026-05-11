package br.com.gwfrete.usuario;

import br.com.gwfrete.usuario.Usuario;

import java.util.logging.Logger;

public class EmailRecuperacaoSenhaService {
    private static final Logger LOGGER = Logger.getLogger(EmailRecuperacaoSenhaService.class.getName());

    public void enviarLinkRecuperacao(Usuario usuario, String token, String linkRecuperacao, int minutosExpiracao) {
        if (usuario == null || token == null || linkRecuperacao == null) {
            return;
        }

        if (ambienteProducao()) {
            LOGGER.info("Solicitação de recuperação de senha registrada para " + usuario.getEmail()
                    + ". Configure SMTP para envio real.");
            return;
        }

        System.out.println("=== RECUPERAÇÃO DE SENHA ===");
        System.out.println("Usuário: " + usuario.getEmail());
        System.out.println("Token: " + token);
        System.out.println("Link: " + linkRecuperacao);
        System.out.println("Expira em: " + minutosExpiracao + " minutos");
        System.out.println("===========================");
    }

    private boolean ambienteProducao() {
        String ambiente = System.getenv("GWFRETE_AMBIENTE");
        return ambiente != null && "producao".equalsIgnoreCase(ambiente.trim());
    }
}
