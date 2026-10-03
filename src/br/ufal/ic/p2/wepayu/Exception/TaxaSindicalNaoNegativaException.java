package br.ufal.ic.p2.wepayu.Exception;

public class TaxaSindicalNaoNegativaException extends Exception {
    public TaxaSindicalNaoNegativaException() {
        super("Taxa sindical deve ser nao-negativa.");
    }
}
