package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.time.LocalDate;

public class TaxaServico implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate data;
    private double taxa;

    public TaxaServico(){};

    public TaxaServico(LocalDate data, double taxa){
        this.data = data;
        this.taxa = taxa;
    }

    public LocalDate getData() {return data;}
    public double getTaxa() {return taxa;}

    public void setData(LocalDate data) {this.data = data;}
    public void setTaxa(double taxa) {this.taxa = taxa;}
}
