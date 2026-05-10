package br.com.gwfrete.model;

public enum StatusFatura {
    PENDENTE("Pendente"),
    PAGO("Pago"),
    VENCIDO("Vencido"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusFatura(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
