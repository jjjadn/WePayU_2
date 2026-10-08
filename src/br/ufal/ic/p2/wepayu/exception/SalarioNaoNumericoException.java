package br.ufal.ic.p2.wepayu.exception;

public class SalarioNaoNumericoException extends Exception {
    public SalarioNaoNumericoException() {
        super("Salario deve ser numerico.");
    }
}
