package br.com.gwfrete.model;

public enum StatusContrato {
    ATIVO("Ativo"),
    ENCERRADO("Encerrado"),
    SUSPENSO("Suspenso"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusContrato(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
