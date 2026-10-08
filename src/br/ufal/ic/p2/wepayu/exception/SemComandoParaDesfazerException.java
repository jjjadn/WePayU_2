package br.ufal.ic.p2.wepayu.exception;

public class SemComandoParaDesfazerException extends Exception {
    public SemComandoParaDesfazerException() {
        super("Nao ha comando a desfazer.");
    }
}
