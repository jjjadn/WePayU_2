package br.ufal.ic.p2.wepayu.exception;

public class BancoNaoNuloException extends Exception {
    public BancoNaoNuloException() {
        super("Banco nao pode ser nulo.");
    }
}
