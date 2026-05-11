package br.com.gwfrete.motorista;

public enum CategoriaCNH {
    A("Categoria A"),
    B("Categoria B"),
    C("Categoria C"),
    D("Categoria D"),
    E("Categoria E");

    private final String descricao;

    CategoriaCNH(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
