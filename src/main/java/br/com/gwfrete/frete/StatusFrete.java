package br.com.gwfrete.frete;

public enum StatusFrete {
    AGENDADO("Agendado"),
    EM_COLETA("Em coleta"),
    EM_TRANSITO("Em trânsito"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado"),
    OCORRENCIA("Ocorrência");

    private final String descricao;

    StatusFrete(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
