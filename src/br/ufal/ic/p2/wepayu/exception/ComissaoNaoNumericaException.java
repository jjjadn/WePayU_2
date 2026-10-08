package br.ufal.ic.p2.wepayu.exception;

public class ComissaoNaoNumericaException extends Exception {
    public ComissaoNaoNumericaException() {
        super("Comissao deve ser numerica.");
    }
}
