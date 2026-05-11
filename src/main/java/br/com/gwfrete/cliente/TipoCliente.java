package br.com.gwfrete.cliente;

public enum TipoCliente {
    PESSOA_FISICA("Pessoa física"),
    PESSOA_JURIDICA("Pessoa jurídica");

    private final String descricao;

    TipoCliente(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
