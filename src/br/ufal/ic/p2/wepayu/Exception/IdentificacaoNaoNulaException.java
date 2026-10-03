package br.ufal.ic.p2.wepayu.Exception;

public class IdentificacaoNaoNulaException extends Exception {
    public IdentificacaoNaoNulaException() {
        super("Identificacao do empregado nao pode ser nula.");
    }
}
