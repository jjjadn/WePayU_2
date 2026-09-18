package br.ufal.ic.p2.wepayu.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Formatador {
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    public static String horas(double h) {
        DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(PT_BR));
        return df.format(h);
    }

    public static String dinheiro(double v) {
        return String.format(Locale.GERMANY, "%.2f", v);
    }
}