package br.ufal.ic.p2.wepayu.exception;

public class AgendaJaExisteException extends RuntimeException {
    public AgendaJaExisteException() {
        super("Agenda de pagamentos ja existe");
    }
}
