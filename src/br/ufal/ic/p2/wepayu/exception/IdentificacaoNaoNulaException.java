package br.ufal.ic.p2.wepayu.exception;

public class IdentificacaoNaoNulaException extends Exception {
    public IdentificacaoNaoNulaException() {
        super("Identificacao do empregado nao pode ser nula.");
    }
}
