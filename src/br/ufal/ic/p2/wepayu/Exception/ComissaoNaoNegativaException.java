package br.ufal.ic.p2.wepayu.Exception;

public class ComissaoNaoNegativaException extends Exception {
    public ComissaoNaoNegativaException() {
        super("Comissao deve ser nao-negativa.");
    }
}
