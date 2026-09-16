package br.ufal.ic.p2.wepayu.validacao;

public class ValidadorEmpregado {

    public static double validarSalario(String salarioString) throws Exception {
        if (salarioString == null || salarioString.trim().isEmpty()) {
            throw new Exception("Salario nao pode ser nulo.");
        }

        String salarioTratado = salarioString.trim().replace(",", ".");

        try {
            double salario = Double.parseDouble(salarioTratado);
            if (salario < 0) {
                throw new Exception("Salario deve ser nao-negativo.");
            }
            return salario;
        } catch (NumberFormatException e) {
            throw new Exception("Salario deve ser numerico.");
        }
    }

    public static void validarAtributos(String nome, String endereco, String tipo) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Nome nao pode ser nulo.");
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new Exception("Endereco nao pode ser nulo.");
        }
        if (tipo == null || (!tipo.equals("horista") &&
                !tipo.equals("assalariado") &&
                !tipo.equals("comissionado"))) {
            throw new Exception("Tipo invalido."); // Alterado de IllegalArgumentException para Exception
        }
    }

    public static double validarComissao(String comissaoString) throws Exception {
        if (comissaoString == null || comissaoString.trim().isEmpty()) {
            throw new Exception("Comissao nao pode ser nula.");
        }

        String comissaoTratada = comissaoString.trim().replace(",", ".");

        try {
            double comissao = Double.parseDouble(comissaoTratada);
            if (comissao < 0) {
                throw new Exception("Comissao deve ser nao-negativa.");
            }
            return comissao;
        } catch (NumberFormatException e) {
            throw new Exception("Comissao deve ser numerica.");
        }
    }
}