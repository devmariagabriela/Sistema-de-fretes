package br.com.gwfrete.ocorrencia;

public enum TipoDocumentoRecebedor {
    CPF("CPF"),
    CNPJ("CNPJ"),
    OUTRO("Outro");

    private final String descricao;

    TipoDocumentoRecebedor(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
