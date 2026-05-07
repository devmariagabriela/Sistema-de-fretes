package br.com.gwfrete.model;

public enum TipoOcorrenciaFrete {
    SAIDA_PATIO("Saída do pátio"),
    EM_ROTA("Em rota"),
    TENTATIVA_ENTREGA("Tentativa de entrega"),
    ENTREGA_REALIZADA("Entrega realizada"),
    AVARIA("Avaria"),
    EXTRAVIO("Extravio"),
    OUTROS("Outros");

    private final String descricao;

    TipoOcorrenciaFrete(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
