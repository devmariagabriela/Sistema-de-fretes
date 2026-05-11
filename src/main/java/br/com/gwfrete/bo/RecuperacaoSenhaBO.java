package br.com.gwfrete.bo;

import br.com.gwfrete.dao.RecuperacaoSenhaDAO;
import br.com.gwfrete.dao.UsuarioDAO;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.RecuperacaoSenha;
import br.com.gwfrete.model.StatusUsuario;
import br.com.gwfrete.model.Usuario;
import br.com.gwfrete.util.CriptografiaUtil;
import br.com.gwfrete.util.EmailRecuperacaoSenhaService;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;

public class RecuperacaoSenhaBO {
    private static final int MINIMO_CARACTERES_SENHA = 6;
    private static final int MINUTOS_EXPIRACAO_TOKEN = 30;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UsuarioDAO usuarioDAO;
    private final RecuperacaoSenhaDAO recuperacaoSenhaDAO;
    private final EmailRecuperacaoSenhaService emailService;

    public RecuperacaoSenhaBO() {
        this.usuarioDAO = new UsuarioDAO();
        this.recuperacaoSenhaDAO = new RecuperacaoSenhaDAO();
        this.emailService = new EmailRecuperacaoSenhaService();
    }

    public void solicitarRecuperacao(String email) throws CadastroException {
        solicitarRecuperacao(email, null);
    }

    public void solicitarRecuperacao(String email, String urlBaseRedefinicao) throws CadastroException {
        String emailNormalizado = normalizarEmail(email);

        if (emailNormalizado == null) {
            return;
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorEmail(emailNormalizado);

            if (usuario == null || usuario.getStatus() == StatusUsuario.INATIVO) {
                return;
            }

            recuperacaoSenhaDAO.invalidarTokensAbertosDoUsuario(usuario.getId());

            RecuperacaoSenha recuperacao = new RecuperacaoSenha();
            recuperacao.setUsuario(usuario);
            recuperacao.setToken(gerarToken());
            recuperacao.setDataExpiracao(LocalDateTime.now().plusMinutes(MINUTOS_EXPIRACAO_TOKEN));
            recuperacao.setUsado(Boolean.FALSE);
            recuperacaoSenhaDAO.salvar(recuperacao);

            emailService.enviarLinkRecuperacao(usuario, recuperacao.getToken(),
                    montarLink(urlBaseRedefinicao, recuperacao.getToken()), MINUTOS_EXPIRACAO_TOKEN);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível processar a solicitação de recuperação.");
        }
    }

    public RecuperacaoSenha validarToken(String token) throws CadastroException {
        String tokenNormalizado = normalizarToken(token);

        if (tokenNormalizado == null) {
            throw new CadastroException("Link de recuperação inválido ou expirado.");
        }

        try {
            RecuperacaoSenha recuperacao = recuperacaoSenhaDAO.buscarPorToken(tokenNormalizado);

            if (!tokenValido(recuperacao)) {
                throw new CadastroException("Link de recuperação inválido ou expirado.");
            }

            return recuperacao;
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível validar o link de recuperação.");
        }
    }

    public void redefinirSenha(String token, String novaSenha, String confirmacaoSenha) throws CadastroException {
        validarNovaSenha(novaSenha, confirmacaoSenha);

        try {
            RecuperacaoSenha recuperacao = validarToken(token);
            String senhaCriptografada = CriptografiaUtil.gerarHashBCrypt(novaSenha);

            usuarioDAO.atualizarSenha(recuperacao.getUsuario().getId(), senhaCriptografada);
            recuperacaoSenhaDAO.invalidarToken(recuperacao.getId());
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível redefinir a senha.");
        }
    }

    private boolean tokenValido(RecuperacaoSenha recuperacao) {
        return recuperacao != null
                && !Boolean.TRUE.equals(recuperacao.getUsado())
                && recuperacao.getDataExpiracao() != null
                && recuperacao.getDataExpiracao().isAfter(LocalDateTime.now())
                && recuperacao.getUsuario() != null
                && recuperacao.getUsuario().getStatus() == StatusUsuario.ATIVO;
    }

    private void validarNovaSenha(String novaSenha, String confirmacaoSenha) throws CadastroException {
        if (novaSenha == null || novaSenha.length() < MINIMO_CARACTERES_SENHA) {
            throw new CadastroException("A nova senha deve ter pelo menos 6 caracteres.");
        }

        if (!novaSenha.equals(confirmacaoSenha)) {
            throw new CadastroException("A confirmação de senha não confere.");
        }
    }

    private String gerarToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String montarLink(String urlBaseRedefinicao, String token) {
        if (urlBaseRedefinicao == null || urlBaseRedefinicao.trim().isEmpty()) {
            return token;
        }

        return urlBaseRedefinicao + "?token=" + token;
    }

    private String normalizarEmail(String email) {
        if (email == null) {
            return null;
        }

        String emailNormalizado = email.trim().toLowerCase();
        return emailNormalizado.isEmpty() ? null : emailNormalizado;
    }

    private String normalizarToken(String token) {
        if (token == null) {
            return null;
        }

        String tokenNormalizado = token.trim();
        return tokenNormalizado.isEmpty() ? null : tokenNormalizado;
    }
}
