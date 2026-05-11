package br.com.gwfrete.manutencao;

public enum TipoManutencao {
    PREVENTIVA("Preventiva"),
    CORRETIVA("Corretiva");

    private final String descricao;

    TipoManutencao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
