package br.ufal.ic.p2.wepayu.Exception;

public class TaxaSindicalNaoNulaException extends Exception {
    public TaxaSindicalNaoNulaException() {
        super("Taxa sindical nao pode ser nula.");
    }
}
