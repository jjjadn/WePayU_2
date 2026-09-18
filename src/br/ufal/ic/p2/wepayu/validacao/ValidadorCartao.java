package br.ufal.ic.p2.wepayu.validacao;

import java.time.LocalDate;

public class ValidadorCartao {


    public static LocalDate validarData(String texto, String rotulo) throws Exception {
        String mensagem = rotulo.isEmpty()
                ? "Data invalida."
                : "Data " + rotulo + " invalida.";

        if (texto == null || texto.trim().isEmpty()) {
            throw new Exception(mensagem);
        }

        String[] partes = texto.split("/");
        if(partes.length != 3)
            throw new Exception(mensagem);

        int dia, mes, ano;
        try {
            dia = Integer.parseInt(partes[0]);
            mes = Integer.parseInt(partes[1]);
            ano = Integer.parseInt(partes[2]);
        } catch (NumberFormatException e){
            throw new Exception(mensagem);
        }

        try{
            return LocalDate.of(ano, mes, dia);
        }catch(Exception e){
            throw new Exception(mensagem);
        }

    }

    public static double validarHoras(String horasSt) throws Exception {
        if(horasSt == null|| horasSt.trim().isEmpty()){
            throw new Exception("Horas devem ser positivas.");
        }
        String horasTratado = horasSt.trim().replace(",", ".");
        double horas;
        try {
            horas = Double.parseDouble(horasTratado);
        } catch (NumberFormatException e) {
            throw new Exception("Horas devem ser positivas.");
        }
        if(horas <= 0){
            throw new Exception("Horas devem ser positivas.");
        }

        return horas;
    }


}
