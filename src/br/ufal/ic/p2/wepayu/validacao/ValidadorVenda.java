package br.ufal.ic.p2.wepayu.validacao;

import br.ufal.ic.p2.wepayu.Exception.ValorNaoPositivoException;

public class ValidadorVenda {

    public static double validarValor(String valorStr) throws Exception {

        if (valorStr == null || valorStr.trim().isEmpty()) {
            throw new ValorNaoPositivoException();
        }

        String tratado = valorStr.trim().replace(",", ".");

        double valor;

        try {
            valor = Double.parseDouble(tratado);
        } catch (NumberFormatException e) {
            throw new ValorNaoPositivoException();
        }

        if (valor <= 0) {
            throw new ValorNaoPositivoException();
        }

        return valor;
    }
}