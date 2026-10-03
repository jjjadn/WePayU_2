package br.ufal.ic.p2.wepayu.Exception;

public class BancoNaoNuloException extends Exception {
    public BancoNaoNuloException() {
        super("Banco nao pode ser nulo.");
    }
}
