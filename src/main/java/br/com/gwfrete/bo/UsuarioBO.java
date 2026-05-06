package br.com.gwfrete.bo;

import br.com.gwfrete.dao.UsuarioDAO;
import br.com.gwfrete.exception.AutenticacaoException;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.Usuario;
import br.com.gwfrete.util.CriptografiaUtil;

import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Set;

public class UsuarioBO {
    private final UsuarioDAO usuarioDAO;

    public UsuarioBO() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticar(String email, String senha) throws AutenticacaoException {
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            throw new AutenticacaoException("Informe usuário e senha para acessar o sistema.");
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorEmail(email.trim());

            if (usuario == null || !senhaConfere(senha, usuario.getSenha())) {
                throw new AutenticacaoException("Usuário ou senha inválidos");
            }

            if (!usuario.isAtivo()) {
                throw new AutenticacaoException("Usuário inativo");
            }

            usuario.setSenha(null);
            return usuario;
        } catch (SQLException e) {
            throw new AutenticacaoException("Não foi possível autenticar agora. Tente novamente em instantes.");
        }
    }

    public Set<String> obterPermissoes(PerfilUsuario perfil) {
        if (perfil == null) {
            return java.util.Collections.emptySet();
        }

        switch (perfil) {
            case ADMIN:
                return PermissaoSistema.todas();
            case OPERADOR:
                return PermissaoSistema.operador();
            case GESTOR:
                return PermissaoSistema.gestor();
            default:
                return java.util.Collections.emptySet();
        }
    }

    private boolean senhaConfere(String senhaInformada, String senhaArmazenada) {
        return CriptografiaUtil.gerarSha256(senhaInformada).equals(senhaArmazenada);
    }

    private enum PermissaoSistema {
        GERENCIAR_USUARIOS,
        EMITIR_FRETES,
        REGISTRAR_OCORRENCIAS,
        ACESSAR_RELATORIOS,
        ACESSAR_DASHBOARDS,
        ACESSAR_AUDITORIA;

        private static Set<String> todas() {
            return nomes(EnumSet.allOf(PermissaoSistema.class));
        }

        private static Set<String> operador() {
            return nomes(EnumSet.of(EMITIR_FRETES, REGISTRAR_OCORRENCIAS));
        }

        private static Set<String> gestor() {
            return nomes(EnumSet.of(ACESSAR_RELATORIOS, ACESSAR_DASHBOARDS, ACESSAR_AUDITORIA));
        }

        private static Set<String> nomes(Set<PermissaoSistema> permissoes) {
            Set<String> nomes = new java.util.HashSet<>();
            for (PermissaoSistema permissao : permissoes) {
                nomes.add(permissao.name());
            }
            return nomes;
        }
    }
}
