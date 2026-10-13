package br.ufal.ic.p2.wepayu.servicos;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

import br.ufal.ic.p2.wepayu.calculadora.CalculadoraFolhas;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.relatorios.GeradorRelatorio;
import br.ufal.ic.p2.wepayu.validacao.ValidadorCartao;

public class ServicoFolha {

    private Map<String, Empregado> empregados;
    private GeradorRelatorio gerador = new GeradorRelatorio();

    public ServicoFolha(Map<String, Empregado> empregados) {
        this.empregados = empregados;
    }

    public String totalFolha(String dataStr) throws Exception {

        LocalDate data = ValidadorCartao.validarData(dataStr, "");

        double total = 0;

        for (Empregado emp : empregados.values()) {
            total += CalculadoraFolhas.calcular(emp, data);
        }
        return String.format(Locale.GERMANY, "%.2f", total);
    }

    public void rodaFolha(String dataStr, String saida) throws Exception {

        LocalDate data = ValidadorCartao.validarData(dataStr, "");

        try (PrintWriter out = new PrintWriter(new FileWriter(saida))) {
            gerador.gerar(out, data, empregados);
        }
    }
}