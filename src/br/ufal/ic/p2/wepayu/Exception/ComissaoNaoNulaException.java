package br.ufal.ic.p2.wepayu.Exception;

public class ComissaoNaoNulaException extends Exception {
    public ComissaoNaoNulaException() {
        super("Comissao nao pode ser nula.");
    }
}
