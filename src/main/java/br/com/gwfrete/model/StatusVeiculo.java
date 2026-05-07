package br.com.gwfrete.model;

public enum StatusVeiculo {
    DISPONIVEL("Disponível"),
    EM_ROTA("Em rota"),
    MANUTENCAO("Manutenção"),
    INATIVO("Inativo");

    private final String descricao;

    StatusVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
