package br.com.gwfrete.exception;

public class CadastroException extends Exception {
    private static final long serialVersionUID = 1L;

    public CadastroException(String mensagem) {
        super(mensagem);
    }
}
