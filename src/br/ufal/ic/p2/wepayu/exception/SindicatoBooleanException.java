package br.ufal.ic.p2.wepayu.exception;

public class SindicatoBooleanException extends Exception {
    public SindicatoBooleanException() {
        super("Valor deve ser true ou false.");
    }
}
