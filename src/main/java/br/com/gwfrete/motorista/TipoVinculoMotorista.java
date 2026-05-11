package br.com.gwfrete.motorista;

public enum TipoVinculoMotorista {
    FUNCIONARIO("Funcionário"),
    AGREGADO("Agregado"),
    TERCEIRO("Terceiro");

    private final String descricao;

    TipoVinculoMotorista(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
