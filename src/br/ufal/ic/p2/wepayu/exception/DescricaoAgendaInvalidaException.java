package br.ufal.ic.p2.wepayu.exception;

public class DescricaoAgendaInvalidaException extends RuntimeException {
    public DescricaoAgendaInvalidaException() {
        super("Descricao de agenda invalida");
    }
}
