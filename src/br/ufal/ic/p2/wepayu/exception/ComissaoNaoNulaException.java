package br.ufal.ic.p2.wepayu.exception;

public class ComissaoNaoNulaException extends Exception {
    public ComissaoNaoNulaException() {
        super("Comissao nao pode ser nula.");
    }
}
