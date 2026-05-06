package br.com.gwfrete.exception;

public class AutenticacaoException extends Exception {
    private static final long serialVersionUID = 1L;

    public AutenticacaoException(String mensagem) {
        super(mensagem);
    }
}
