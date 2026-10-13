package br.ufal.ic.p2.wepayu.relatorios;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.ufal.ic.p2.wepayu.calculadora.CalculadoraFolhas;
import br.ufal.ic.p2.wepayu.models.Cartao;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Venda;

public class GeradorRelatorio {

    public double gerar(PrintWriter out, LocalDate data, Map<String, Empregado> empregados) {

        List<Empregado> horistas = new ArrayList<>();
        List<Empregado> assalariados = new ArrayList<>();
        List<Empregado> comissionados = new ArrayList<>();

        for (Empregado e : empregados.values()) {
            switch (e.getTipo()) {
                case "horista":      horistas.add(e); break;
                case "assalariado":  assalariados.add(e); break;
                case "comissionado": comissionados.add(e); break;
            }
        }
        horistas.sort(Comparator.comparing(Empregado::getNome));
        assalariados.sort(Comparator.comparing(Empregado::getNome));
        comissionados.sort(Comparator.comparing(Empregado::getNome));

        out.println("FOLHA DE PAGAMENTO DO DIA " + data.toString());
        out.println("=".repeat(36));
        out.println();

        double total = 0;
        total += escreverHoristas(out, horistas, data);
        total += escreverAssalariados(out, assalariados, data);
        total += escreverComissionados(out, comissionados, data);

        out.println("TOTAL FOLHA: " + String.format(Locale.GERMANY, "%.2f", total));
        return total;
    }

    private double escreverHoristas(PrintWriter out, List<Empregado> lista, LocalDate data) {

        String titulo = "===================== HORISTAS ";
        out.println("=".repeat(127));
        out.println(titulo + "=".repeat(127 - titulo.length()));
        out.println("=".repeat(127));
        out.println(String.format("%-36s %5s %5s %13s %9s %15s %s",
                "Nome", "Horas", "Extra", "Salario Bruto", "Descontos", "Salario Liquido", "Metodo"));
        out.println("==================================== ===== ===== ============= ========= =============== ======================================");

        double somaBruto = 0, somaDesc = 0, somaLiq = 0;
        int somaHoras = 0, somaExtra = 0;

        for (Empregado emp : lista) {
            if (!CalculadoraFolhas.deveSerPago(emp, data)) continue;

            int horas = 0, extra = 0;
            LocalDate inicio = data.minusDays(7);
            for (Cartao c : emp.getCartoes()) {
                if (!c.getData().isBefore(inicio) && c.getData().isBefore(data)) {
                    horas += (int) Math.min(c.getHoras(), 8);
                    extra += (int) Math.max(0, c.getHoras() - 8);
                }
            }

            double bruto = CalculadoraFolhas.calcular(emp, data);
            double desc  = CalculadoraFolhas.calcularDesconto(emp, data);
            double liq   = Math.max(0, bruto - desc);

            out.println(String.format("%-36s %5d %5d %13s %9s %15s %s",
                    emp.getNome(), horas, extra,
                    String.format(Locale.GERMANY, "%.2f", bruto),
                    String.format(Locale.GERMANY, "%.2f", desc),
                    String.format(Locale.GERMANY, "%.2f", liq),
                    formatarMetodo(emp)));

            somaBruto += bruto; somaDesc += desc; somaLiq += liq;
            somaHoras += horas; somaExtra += extra;
        }

        out.println();
        out.println(String.format("%-36s %5d %5d %13s %9s %15s",
                "TOTAL HORISTAS", somaHoras, somaExtra,
                String.format(Locale.GERMANY, "%.2f", somaBruto),
                String.format(Locale.GERMANY, "%.2f", somaDesc),
                String.format(Locale.GERMANY, "%.2f", somaLiq)));
        out.println();

        return somaBruto;
    }

    private double escreverAssalariados(PrintWriter out, List<Empregado> lista, LocalDate data) {
        String titulo = "===================== ASSALARIADOS ";
        out.println("=".repeat(127));
        out.println(titulo + "=".repeat(127 - titulo.length()));
        out.println("=".repeat(127));
        out.println(String.format("%-48s %13s %9s %15s %s",
                "Nome", "Salario Bruto", "Descontos", "Salario Liquido", "Metodo"));
        out.println("================================================ ============= ========= =============== ======================================");

        double somaBruto = 0, somaDesc = 0, somaLiq = 0;

        for (Empregado emp : lista) {
            if (!CalculadoraFolhas.deveSerPago(emp, data)) continue;

            double bruto = CalculadoraFolhas.calcular(emp, data);
            double desc  = CalculadoraFolhas.calcularDesconto(emp, data);
            double liq   = Math.max(0, bruto - desc);

            out.println(String.format("%-48s %13s %9s %15s %s",
                    emp.getNome(),
                    String.format(Locale.GERMANY, "%.2f", bruto),
                    String.format(Locale.GERMANY, "%.2f", desc),
                    String.format(Locale.GERMANY, "%.2f", liq),
                    formatarMetodo(emp)));

            somaBruto += bruto; somaDesc += desc; somaLiq += liq;
        }

        out.println();
        out.println(String.format("%-48s %13s %9s %15s",
                "TOTAL ASSALARIADOS",
                String.format(Locale.GERMANY, "%.2f", somaBruto),
                String.format(Locale.GERMANY, "%.2f", somaDesc),
                String.format(Locale.GERMANY, "%.2f", somaLiq)));
        out.println();

        return somaBruto;
    }

    private double escreverComissionados(PrintWriter out, List<Empregado> lista, LocalDate data) {

        String titulo = "===================== COMISSIONADOS ";
        out.println("=".repeat(127));
        out.println(titulo + "=".repeat(127 - titulo.length()));
        out.println("=".repeat(127));
        out.println(String.format("%-21s %-8s %-8s %-8s %13s %9s %15s %s",
                "Nome", "Fixo", "Vendas", "Comissao",
                "Salario Bruto", "Descontos", "Salario Liquido", "Metodo"));
        out.println("===================== ======== ======== ======== ============= ========= =============== ======================================");

        double somaFixo = 0, somaVendas = 0, somaComissao = 0;
        double somaBruto = 0, somaDesc = 0, somaLiq = 0;

        for (Empregado emp : lista) {
            if (!CalculadoraFolhas.deveSerPago(emp, data)) continue;

            double base = Math.floor(emp.getSalario() * 12.0 / 26.0 * 100) / 100;
            double vendas = 0;
            LocalDate inicio = data.minusDays(14);
            for (Venda v : emp.getVendas()) {
                if (!v.getData().isBefore(inicio) && v.getData().isBefore(data)) {
                    vendas += v.getValor();
                }
            }
            double comissao = vendas * (emp.getComissao() == null ? 0 : emp.getComissao());
            comissao = Math.floor(comissao * 100) / 100;

            double bruto = CalculadoraFolhas.calcular(emp, data);
            double desc  = CalculadoraFolhas.calcularDesconto(emp, data);
            double liq   = Math.max(0, bruto - desc);

            out.println(String.format("%-21s %8s %8s %8s %13s %9s %15s %s",
                    emp.getNome(),
                    String.format(Locale.GERMANY, "%.2f", base),
                    String.format(Locale.GERMANY, "%.2f", vendas),
                    String.format(Locale.GERMANY, "%.2f", comissao),
                    String.format(Locale.GERMANY, "%.2f", bruto),
                    String.format(Locale.GERMANY, "%.2f", desc),
                    String.format(Locale.GERMANY, "%.2f", liq),
                    formatarMetodo(emp)));

            somaFixo += base; somaVendas += vendas; somaComissao += comissao;
            somaBruto += bruto; somaDesc += desc; somaLiq += liq;
        }

        out.println();
        out.println(String.format("%-21s %8s %8s %8s %13s %9s %15s",
                "TOTAL COMISSIONADOS",
                String.format(Locale.GERMANY, "%.2f", somaFixo),
                String.format(Locale.GERMANY, "%.2f", somaVendas),
                String.format(Locale.GERMANY, "%.2f", somaComissao),
                String.format(Locale.GERMANY, "%.2f", somaBruto),
                String.format(Locale.GERMANY, "%.2f", somaDesc),
                String.format(Locale.GERMANY, "%.2f", somaLiq)));
        out.println();

        return somaBruto;
    }

    private String formatarMetodo(Empregado emp) {

        String m = emp.getMetodoPagamento();
        if ("emMaos".equals(m)) return "Em maos";
        if ("correios".equals(m)) return "Correios, " + emp.getEndereco();
        if ("banco".equals(m)) {
            return emp.getBanco() + ", Ag. " + emp.getAgencia() + " CC " + emp.getContaCorrente();
        }
        return m;
    }
}