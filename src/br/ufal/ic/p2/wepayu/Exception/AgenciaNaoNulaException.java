package br.ufal.ic.p2.wepayu.Exception;

public class AgenciaNaoNulaException extends Exception {
    public AgenciaNaoNulaException() {
        super("Agencia nao pode ser nulo.");
    }
}
