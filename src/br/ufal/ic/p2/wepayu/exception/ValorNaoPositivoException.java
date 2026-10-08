package br.ufal.ic.p2.wepayu.exception;

public class ValorNaoPositivoException extends Exception {
    public ValorNaoPositivoException() {
        super("Valor deve ser positivo.");
    }
}
