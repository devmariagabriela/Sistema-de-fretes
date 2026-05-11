package br.com.gwfrete.notificacao;

public enum StatusNotificacao {
    NAO_LIDA("Não lida"),
    LIDA("Lida"),
    ARQUIVADA("Arquivada");

    private final String descricao;

    StatusNotificacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
