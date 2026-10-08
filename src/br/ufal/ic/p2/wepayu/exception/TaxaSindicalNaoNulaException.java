package br.ufal.ic.p2.wepayu.exception;

public class TaxaSindicalNaoNulaException extends Exception {
    public TaxaSindicalNaoNulaException() {
        super("Taxa sindical nao pode ser nula.");
    }
}
