package br.ufal.ic.p2.wepayu.exception;

public class IdentificacaoSindicatoNaoNulaException extends Exception {
    public IdentificacaoSindicatoNaoNulaException() {
        super("Identificacao do sindicato nao pode ser nula.");
    }
}
