package br.com.gwfrete.model;

public enum TipoVeiculo {
    CAMINHAO("Caminhão"),
    CARRETA("Carreta"),
    VUC("VUC"),
    UTILITARIO("Utilitário");

    private final String descricao;

    TipoVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
