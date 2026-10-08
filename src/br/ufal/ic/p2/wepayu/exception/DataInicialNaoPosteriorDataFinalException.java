package br.ufal.ic.p2.wepayu.exception;

public class DataInicialNaoPosteriorDataFinalException extends Exception {
    public DataInicialNaoPosteriorDataFinalException() {
        super("Data inicial nao pode ser posterior aa data final.");
    }
}
