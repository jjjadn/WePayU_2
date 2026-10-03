package br.ufal.ic.p2.wepayu.Exception;

public class IdentificacaoSindicatoNaoNulaException extends Exception {
    public IdentificacaoSindicatoNaoNulaException() {
        super("Identificacao do sindicato nao pode ser nula.");
    }
}
