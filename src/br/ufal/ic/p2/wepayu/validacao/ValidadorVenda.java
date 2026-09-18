package br.ufal.ic.p2.wepayu.validacao;

public class ValidadorVenda {

    public static double validarValor(String valorStr) throws Exception {

        if (valorStr == null || valorStr.trim().isEmpty()) {
            throw new Exception("Valor deve ser positivo.");
        }

        String tratado = valorStr.trim().replace(",", ".");

        double valor;

        try {
            valor = Double.parseDouble(tratado);
        } catch (NumberFormatException e) {
            throw new Exception("Valor deve ser positivo.");
        }

        if (valor <= 0) {
            throw new Exception("Valor deve ser positivo.");
        }

        return valor;
    }
}