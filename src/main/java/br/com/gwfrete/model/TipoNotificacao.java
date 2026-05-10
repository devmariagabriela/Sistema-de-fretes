package br.com.gwfrete.model;

public enum TipoNotificacao {
    CNH_VENCENDO("CNH vencendo"),
    VEICULO_MANUTENCAO("Veículo em manutenção"),
    FATURA_VENCIDA("Fatura vencida"),
    FRETE_CANCELADO("Frete cancelado"),
    OCORRENCIA_CRITICA("Ocorrência crítica"),
    OUTROS("Outros");

    private final String descricao;

    TipoNotificacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
