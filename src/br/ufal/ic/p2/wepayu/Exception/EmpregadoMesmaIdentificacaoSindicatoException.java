package br.ufal.ic.p2.wepayu.Exception;

public class EmpregadoMesmaIdentificacaoSindicatoException extends Exception {
    public EmpregadoMesmaIdentificacaoSindicatoException() {
        super("Ha outro empregado com esta identificacao de sindicato");
    }
}
