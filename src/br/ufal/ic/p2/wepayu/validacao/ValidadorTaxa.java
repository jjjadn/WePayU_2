package br.ufal.ic.p2.wepayu.validacao;

public class ValidadorTaxa {

    public static double validarValor(String taxaStr) throws Exception {

        if (taxaStr == null || taxaStr.trim().isEmpty()) {
            throw new Exception("Valor deve ser positivo.");
        }

        String tratado = taxaStr.trim().replace(",", ".");

        double taxa;

        try {
            taxa = Double.parseDouble(tratado);
        } catch (NumberFormatException e) {
            throw new Exception("Valor deve ser positivo.");
        }

        if (taxa <= 0) {
            throw new Exception("Valor deve ser positivo.");
        }

        return taxa;
    }
}