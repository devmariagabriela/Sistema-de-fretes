package br.com.gwfrete.usuario;

public enum StatusUsuario {
    ATIVO("Ativo"),
    INATIVO("Inativo"),
    BLOQUEADO("Bloqueado");

    private final String descricao;

    StatusUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
