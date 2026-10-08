package br.ufal.ic.p2.wepayu.exception;

public class TaxaSindicalNaoNegativaException extends Exception {
    public TaxaSindicalNaoNegativaException() {
        super("Taxa sindical deve ser nao-negativa.");
    }
}
