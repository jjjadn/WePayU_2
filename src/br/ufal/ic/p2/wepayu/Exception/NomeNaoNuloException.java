package br.ufal.ic.p2.wepayu.Exception;

public class NomeNaoNuloException extends Exception {
    public NomeNaoNuloException() {
        super("Nome nao pode ser nulo.");
    }
}
