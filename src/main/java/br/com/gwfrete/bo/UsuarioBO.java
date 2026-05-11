package br.com.gwfrete.bo;

import br.com.gwfrete.dao.UsuarioDAO;
import br.com.gwfrete.exception.AutenticacaoException;
import br.com.gwfrete.exception.CadastroException;
import br.com.gwfrete.model.PerfilUsuario;
import br.com.gwfrete.model.StatusUsuario;
import br.com.gwfrete.model.Usuario;
import br.com.gwfrete.util.CriptografiaUtil;

import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class UsuarioBO {
    private static final Pattern EMAIL_VALIDO = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE);

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

            if (usuario.getStatus() == StatusUsuario.BLOQUEADO) {
                throw new AutenticacaoException("Usuário bloqueado");
            }

            if (usuario.getStatus() == StatusUsuario.INATIVO) {
                throw new AutenticacaoException("Usuário inativo");
            }

            usuario.setSenha(null);
            return usuario;
        } catch (SQLException e) {
            throw new AutenticacaoException("Não foi possível autenticar agora. Tente novamente em instantes.");
        }
    }

    public void salvar(Usuario usuario) throws CadastroException {
        validarCadastro(usuario);

        try {
            if (usuarioDAO.buscarPorEmail(usuario.getEmail().trim()) != null) {
                throw new CadastroException("Já existe usuário cadastrado com este e-mail.");
            }

            prepararDadosUsuario(usuario);
            usuario.setSenha(CriptografiaUtil.gerarHashBCrypt(usuario.getSenha()));
            usuarioDAO.salvar(usuario);
            usuario.setSenha(null);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível salvar o usuário.");
        }
    }

    public List<Usuario> listarTodos() throws CadastroException {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();

            for (Usuario usuario : usuarios) {
                usuario.setSenha(null);
            }

            return usuarios;
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os usuários.");
        }
    }

    public List<Usuario> listarComFiltros(String nome, String email, PerfilUsuario perfil, StatusUsuario status)
            throws CadastroException {
        try {
            List<Usuario> usuarios = usuarioDAO.listarComFiltros(normalizarFiltro(nome), normalizarFiltro(email),
                    perfil, status);

            for (Usuario usuario : usuarios) {
                usuario.setSenha(null);
            }

            return usuarios;
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível listar os usuários.");
        }
    }

    public Usuario buscarPorId(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Usuário inválido.");
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorId(id);

            if (usuario != null) {
                usuario.setSenha(null);
            }

            return usuario;
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível consultar o usuário.");
        }
    }

    public void atualizar(Usuario usuario) throws CadastroException {
        validarAtualizacao(usuario);

        try {
            Usuario usuarioAtual = usuarioDAO.buscarPorId(usuario.getId());

            if (usuarioAtual == null) {
                throw new CadastroException("Usuário não encontrado.");
            }

            Usuario usuarioComMesmoEmail = usuarioDAO.buscarPorEmail(usuario.getEmail().trim());
            if (usuarioComMesmoEmail != null && !usuarioComMesmoEmail.getId().equals(usuario.getId())) {
                throw new CadastroException("Já existe usuário cadastrado com este e-mail.");
            }

            prepararDadosUsuario(usuario);

            if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
                usuario.setSenha(usuarioAtual.getSenha());
            } else {
                usuario.setSenha(CriptografiaUtil.gerarHashBCrypt(usuario.getSenha()));
            }

            usuarioDAO.atualizar(usuario);
            usuario.setSenha(null);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível atualizar o usuário.");
        }
    }

    public void inativar(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Usuário inválido.");
        }

        try {
            if (usuarioDAO.buscarPorId(id) == null) {
                throw new CadastroException("Usuário não encontrado.");
            }

            usuarioDAO.inativar(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível desativar o usuário.");
        }
    }

    public void ativar(Long id) throws CadastroException {
        if (id == null || id <= 0) {
            throw new CadastroException("Usuário inválido.");
        }

        try {
            if (usuarioDAO.buscarPorId(id) == null) {
                throw new CadastroException("Usuário não encontrado.");
            }

            usuarioDAO.ativar(id);
        } catch (SQLException e) {
            throw new CadastroException("Não foi possível ativar o usuário.");
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
        return CriptografiaUtil.verificarSenhaBCrypt(senhaInformada, senhaArmazenada);
    }

    private void validarCadastro(Usuario usuario) throws CadastroException {
        validarCamposComuns(usuario);

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new CadastroException("Senha é obrigatória.");
        }
    }

    private void validarAtualizacao(Usuario usuario) throws CadastroException {
        if (usuario == null || usuario.getId() == null || usuario.getId() <= 0) {
            throw new CadastroException("Usuário inválido.");
        }

        validarCamposComuns(usuario);
    }

    private void validarCamposComuns(Usuario usuario) throws CadastroException {
        if (usuario == null) {
            throw new CadastroException("Usuário inválido.");
        }

        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new CadastroException("Nome é obrigatório.");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new CadastroException("E-mail é obrigatório.");
        }

        if (!EMAIL_VALIDO.matcher(usuario.getEmail().trim()).matches()) {
            throw new CadastroException("E-mail inválido.");
        }

        if (usuario.getPerfil() == null) {
            throw new CadastroException("Perfil é obrigatório.");
        }

        if (usuario.getStatus() == null) {
            throw new CadastroException("Status é obrigatório.");
        }
    }

    private void prepararDadosUsuario(Usuario usuario) {
        usuario.setNome(usuario.getNome().trim());
        usuario.setEmail(usuario.getEmail().trim().toLowerCase());
    }

    private String normalizarFiltro(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
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
