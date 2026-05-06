package br.com.gwfrete.model;

public enum PerfilUsuario {
    ADMIN("Administrador", "Acesso total e gerenciamento de usuários"),
    OPERADOR("Operador", "Emissão de fretes e registro de ocorrências"),
    GESTOR("Gestor", "Relatórios, dashboards e auditoria operacional");

    private final String descricao;
    private final String permissaoBase;

    PerfilUsuario(String descricao, String permissaoBase) {
        this.descricao = descricao;
        this.permissaoBase = permissaoBase;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPermissaoBase() {
        return permissaoBase;
    }
}
