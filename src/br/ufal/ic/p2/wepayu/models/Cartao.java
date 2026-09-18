package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Cartao implements Serializable{
    private static final long serialVersionUID = 1L;

    private LocalDate data;
    private double horas;

    public Cartao(){}

    public Cartao(LocalDate data, double horas){
       this.data = data;
       this.horas = horas;
    }

    public LocalDate getData(){return data;}
    public double getHoras(){return horas;}

    public void setData(LocalDate data){this.data = data;}
    public void setHoras(double horas){this.horas = horas;}


}
