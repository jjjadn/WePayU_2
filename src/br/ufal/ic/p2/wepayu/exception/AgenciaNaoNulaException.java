package br.ufal.ic.p2.wepayu.exception;

public class AgenciaNaoNulaException extends Exception {
    public AgenciaNaoNulaException() {
        super("Agencia nao pode ser nulo.");
    }
}
