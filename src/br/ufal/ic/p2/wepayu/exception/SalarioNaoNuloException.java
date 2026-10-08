package br.ufal.ic.p2.wepayu.exception;

public class SalarioNaoNuloException extends Exception {
    public SalarioNaoNuloException() {
        super("Salario nao pode ser nulo.");
    }
}
