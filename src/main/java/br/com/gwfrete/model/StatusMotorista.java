package br.com.gwfrete.model;

public enum StatusMotorista {
    ATIVO("Ativo"),
    INATIVO("Inativo"),
    SUSPENSO("Suspenso");

    private final String descricao;

    StatusMotorista(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
