package br.ufal.ic.p2.wepayu.servicos;

import br.ufal.ic.p2.wepayu.exception.DescricaoAgendaInvalidaException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class AgendaPagamento {
    private String descricao;
    private boolean semanal;
    private int freq = 1;
    private int diaSemana;
    private int diaMes;
    private boolean ultimoDia;

    public AgendaPagamento(String descricao){
        this.descricao = descricao;
        parse();
    }

    private void parse(){
        if(descricao.startsWith("semanal ")){
            semanal = true;
            String[] partes = descricao.substring(8).split(" ");

            if(partes.length == 1){
                freq = 1;
                diaSemana = Integer.parseInt(partes[0]);

            }else{
                freq = Integer.parseInt(partes[0]);
                diaSemana = Integer.parseInt(partes[1]);
            }

        }else if(descricao.startsWith("mensal ")){
            semanal = false;
            String v = descricao.substring(7);
            if("$".equals(v)){
                ultimoDia = true;
            }else{
                diaMes = Integer.parseInt(v);
            }
        }
    }

    public boolean devePagar(LocalDate data){

        if(semanal){
            if(data.getDayOfWeek().getValue() != diaSemana) return false;
            long semanas = ChronoUnit.WEEKS.between(LocalDate.of(2005, 1, 1), data);
            return semanas % freq == freq -1;
        }

        if(ultimoDia) return data.getDayOfMonth() == data.lengthOfMonth();
        return data.getDayOfMonth() == diaMes;

    }

    public boolean isSemanal() {
        return semanal;
    }
    public boolean isMensal(){return !semanal; }

    public int getFreq(){return freq; }

    public static void validarDescricao(String descricao) throws Exception {

        if (descricao == null || descricao.trim().isEmpty()) {
            throw new DescricaoAgendaInvalidaException();
        }

        if (descricao.startsWith("semanal ")) {
            String[] partes = descricao.substring(8).split(" ");
            try {
                if (partes.length == 1) {

                    int dia = Integer.parseInt(partes[0]);
                    if (dia < 1 || dia > 7) throw new DescricaoAgendaInvalidaException();

                } else if (partes.length == 2) {

                    int f = Integer.parseInt(partes[0]);
                    int dia = Integer.parseInt(partes[1]);

                    if (f < 1 || f > 52) throw new DescricaoAgendaInvalidaException();
                    if (dia < 1 || dia > 7) throw new DescricaoAgendaInvalidaException();

                } else {
                    throw new DescricaoAgendaInvalidaException();
                }
            } catch (NumberFormatException e) {
                throw new DescricaoAgendaInvalidaException();
            }

        }
        else if (descricao.startsWith("mensal ")) {
            String v = descricao.substring(7);

            if ("$".equals(v)) return;

            try {
                int dia = Integer.parseInt(v);
                if (dia < 1 || dia > 28) throw new DescricaoAgendaInvalidaException();
            } catch (NumberFormatException e) {
                throw new DescricaoAgendaInvalidaException();
            }

        } else {
            throw new DescricaoAgendaInvalidaException();
        }
    }
}