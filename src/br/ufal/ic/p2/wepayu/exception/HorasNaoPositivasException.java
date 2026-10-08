package br.ufal.ic.p2.wepayu.exception;

public class HorasNaoPositivasException extends Exception {
    public HorasNaoPositivasException() {
        super("Horas devem ser positivas.");
    }
}
