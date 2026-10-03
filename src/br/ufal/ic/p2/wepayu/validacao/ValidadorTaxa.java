package br.ufal.ic.p2.wepayu.validacao;

import br.ufal.ic.p2.wepayu.Exception.ValorNaoPositivoException;

public class ValidadorTaxa {

    public static double validarValor(String taxaStr) throws Exception {

        if (taxaStr == null || taxaStr.trim().isEmpty()) {
            throw new ValorNaoPositivoException();
        }

        String tratado = taxaStr.trim().replace(",", ".");

        double taxa;

        try {
            taxa = Double.parseDouble(tratado);
        } catch (NumberFormatException e) {
            throw new ValorNaoPositivoException();
        }

        if (taxa <= 0) {
            throw new ValorNaoPositivoException();
        }

        return taxa;
    }
}