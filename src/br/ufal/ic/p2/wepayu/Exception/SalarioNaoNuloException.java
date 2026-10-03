package br.ufal.ic.p2.wepayu.Exception;

public class SalarioNaoNuloException extends Exception {
    public SalarioNaoNuloException() {
        super("Salario nao pode ser nulo.");
    }
}
