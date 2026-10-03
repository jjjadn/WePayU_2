package br.ufal.ic.p2.wepayu.Exception;

public class SalarioNaoNegativoException extends Exception {
    public SalarioNaoNegativoException() {
        super("Salario deve ser nao-negativo.");
    }
}
