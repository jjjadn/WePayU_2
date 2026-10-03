package br.ufal.ic.p2.wepayu.Exception;

public class ContaCorrenteNaoNulaException extends Exception {
    public ContaCorrenteNaoNulaException() {
        super("Conta corrente nao pode ser nulo.");
    }
}
