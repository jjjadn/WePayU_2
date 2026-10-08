package br.ufal.ic.p2.wepayu.validacao;

import br.ufal.ic.p2.wepayu.exception.*;

public class ValidadorEmpregado {

    public static double validarSalario(String salarioString) throws Exception {
        if (salarioString == null || salarioString.trim().isEmpty()) {
            throw new SalarioNaoNuloException();
        }

        String salarioTratado = salarioString.trim().replace(",", ".");

        try {
            double salario = Double.parseDouble(salarioTratado);
            if (salario < 0) {
                throw new SalarioNaoNegativoException();
            }
            return salario;
        } catch (NumberFormatException e) {
            throw new SalarioNaoNumericoException();
        }
    }

    public static void validarAtributos(String nome, String endereco, String tipo) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new NomeNaoNuloException();
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new EnderecoNaoNuloException();
        }
        if (tipo == null || (!tipo.equals("horista") &&
                !tipo.equals("assalariado") &&
                !tipo.equals("comissionado"))) {
            throw new TipoEmpregadoNaoValidoException();
        }
    }
    // testando
    public static double validarComissao(String comissaoString) throws Exception {
        if (comissaoString == null || comissaoString.trim().isEmpty()) {
            throw new ComissaoNaoNulaException();
        }

        String comissaoTratada = comissaoString.trim().replace(",", ".");

        try {
            double comissao = Double.parseDouble(comissaoTratada);
            if (comissao < 0) {
                throw new ComissaoNaoNegativaException();
            }
            return comissao;
        } catch (NumberFormatException e) {
            throw new ComissaoNaoNumericaException();
        }
    }
}